package com.rebllelionandroid.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebllelionandroid.core.database.gamestate.*
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.gamestate.enums.DefenseStructureType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType
import com.rebllelionandroid.core.database.gamestate.enums.ShipType
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
import com.rebllelionandroid.core.database.staticTypes.StaticTypesRepository
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.random.Random

class GameStateViewModel @Inject constructor(
    private val gameStateRepository: GameStateRepository,
    private val staticTypesRepository: StaticTypesRepository
) : ViewModel() {

    lateinit var timerJob: Job
    private val _gameState = MutableLiveData<GameStateWithSectors>()
    val gameState: LiveData<GameStateWithSectors>
        get() = _gameState


    fun loadAllGameStateWithSectors(gameId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val gameState = gameStateRepository.getGameStateWithSectors(gameId)
            _gameState.postValue(gameState)
        }
    }
    fun getAllGameStates(callback: (allGameStates: List<GameState>) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val allGameStates = gameStateRepository.getAllGameStates()
            callback(allGameStates)
        }
    }
    fun getGameStateWithSectorsLive(gameStateId: Long) = gameStateRepository.getGameStateWithSectorsLive(gameStateId)
    fun getSectorWithPlanets(sectorId: Long) = gameStateRepository.getSectorWithPlanets(sectorId)
    fun getPlanetWithUnits(planetId: Long, callback: (planetWithUnits: PlanetWithUnits) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val planetWithUnits = gameStateRepository.getPlanetWithUnits(planetId)
            callback(planetWithUnits)
        }
    }
    fun getShipWithUnits(shipId: Long, callback: (shipWithUnits: ShipWithUnits) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val shipWithUnits = gameStateRepository.getShipWithUnits(shipId)
            callback(shipWithUnits)
        }
    }
    fun getAllUnitsOnTheSurfaceOfPlanet(planetId: Long, callback: (units: List<Unit>) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val units = gameStateRepository.getAllUnitsOnTheSurfaceOfPlanet(planetId)
            callback(units)
        }
    }
    fun stopAllGameStates() {
        viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.stopAllGameStates()
        }
    }

    fun getAllPlanets(callback: (planets: List<Planet>) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val planets = gameStateRepository.getAllPlanets()
            callback(planets)
        }
    }

    fun moveUnitToShip(unitId: Long, shipId: Long, gameStateId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.moveUnitToShip(unitId, shipId)
            postUpdate(gameStateId)
        }
    }
    fun moveUnitToPlanet(unitId: Long, planetId: Long, gameStateId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.moveUnitToPlanet(unitId, planetId)
            postUpdate(gameStateId)
        }
    }


    fun toggleTimer(gameStateId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val _gameState = gameStateRepository.getGameState(gameStateId)
            if(_gameState.gameInProgress) {
                stopTimer(gameStateId)
            } else {
                startTimer(gameStateId)
            }
        }
    }

    fun startTimer(gameStateId: Long) {
        timerJob = viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.setGameInProgress(gameStateId, 1)
            while (true) {
                updateGameState(gameStateId)
                delay(1500)
            }
        }
    }

    fun stopTimer(gameStateId: Long) {
        if(this::timerJob.isInitialized) {
            timerJob.cancel()
        }
        viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.setGameInProgress(gameStateId, 0)
            postUpdate(gameStateId)
        }
    }

    private fun postUpdate(gameStateId: Long) {
        val newGameStateWithSectors = gameStateRepository.getGameStateWithSectors(gameStateId)
        _gameState.postValue(newGameStateWithSectors)
    }

    fun createNewGameState(callback: () -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val gameState = GameState(Random.nextLong(), false, 1, TeamLoyalty.TeamA, Date())
            gameStateRepository.createNewGameState(gameState)

            val teamsToPlanets = mapOf(
                TeamLoyalty.TeamA to ArrayList<Planet>(),
                TeamLoyalty.TeamB to ArrayList<Planet>(),
            )

            val allSectorTypes = staticTypesRepository.getAllSectorTypes()
            for (sectorType in allSectorTypes) {
                val sector = Sector(Random.nextLong(), sectorType.name, gameState.id)
                gameStateRepository.insertNewSector(sector)
                val allPlanetTypesForSector = staticTypesRepository.getAllPlanetTypesForSector(sectorType.id)
                for (planetType in allPlanetTypesForSector) {
                    var loyaltyMinTeamA = 0
                    var loyaltyMaxTeamA = 20
                    var loyaltyMinTeamB = 0
                    var loyaltyMaxTeamB = 20
                    if (sectorType.initTeamLoyalty == TeamLoyalty.TeamA) {
                        loyaltyMinTeamA = 10
                        loyaltyMaxTeamA = 50
                        loyaltyMaxTeamB = 10
                    } else if (sectorType.initTeamLoyalty == TeamLoyalty.TeamB) {
                        loyaltyMaxTeamA = 10
                        loyaltyMinTeamB = 10
                        loyaltyMaxTeamB = 50
                    }
                    val planet = Planet(
                            id = Random.nextLong(),
                            name = planetType.name,
                            sectorId = sector.id,
                            teamALoyalty = Random.nextInt(loyaltyMinTeamA, loyaltyMaxTeamA),
                            teamBLoyalty = Random.nextInt(loyaltyMinTeamB, loyaltyMaxTeamB),
                            isExplored = sectorType.initTeamLoyalty == gameState.myTeam,
                            energyCap = Random.nextInt(10)
                    )
                    gameStateRepository.insertNewPlanet(planet)
                    teamsToPlanets[sectorType.initTeamLoyalty]?.add(planet)

                    val manyInitUnitsPerPlanet = 5
                    for (u in 1..manyInitUnitsPerPlanet) {
                        val unit = Unit(
                            id = Random.nextLong(),
                            unitType = UnitType.values().random(),
                            locationPlanetId = planet.id,
                            locationShip = null,
                            mission = null,
                            dayMissionComplete = 0,
                            missionTargetType = null,
                            missionTargetId = null,
                            sectorType.initTeamLoyalty
                        )
                        gameStateRepository.insertNewUnit(unit)
                    }
                }
            }

            arrayListOf(TeamLoyalty.TeamA, TeamLoyalty.TeamB).forEach { teamLoyalty ->
                val planetsForTeam = teamsToPlanets[teamLoyalty]
                val planetForTeam = planetsForTeam?.get(Random.nextInt(planetsForTeam.size))

                if(planetForTeam != null) {
                    // Orbital battery
                    val orbitalBattery = DefenseStructure(
                        id = Random.nextLong(),
                        defenseStructureType = DefenseStructureType.OrbitalBattery,
                        locationPlanetId = planetForTeam.id,
                        isTravelling = false,
                        dayArrival = 0
                    )
                    gameStateRepository.insertNewDefenseStructure(orbitalBattery)

                    // Planetary shield
                    val planetaryShield = DefenseStructure(
                        id = Random.nextLong(),
                        defenseStructureType = DefenseStructureType.PlanetaryShield,
                        locationPlanetId = planetForTeam.id,
                        isTravelling = false,
                        dayArrival = 0
                    )
                    gameStateRepository.insertNewDefenseStructure(planetaryShield)

                    val manyInitFactories = 3
                    for (u in 1..manyInitFactories) {
                        val planetId = planetForTeam.id
                        val factory = Factory(
                            id = Random.nextLong(),
                            factoryType = FactoryType.values().random(),
                            planetId,
                            buildTargetType = null,
                            dayBuildComplete = 0,
                            isTravelling = false,
                            dayArrival = 0
                        )
                        gameStateRepository.insertNewFactory(factory)
                    }

                    val manyInitShips = 5
                    val manyInitUnitsPerShip = 4
                    for (u in 1..manyInitShips) {
                        val ship = Ship(
                            id = Random.nextLong(),
                            locationPlanetId = planetForTeam.id,
                            shipType = ShipType.values().random(),
                            isTraveling = false,
                            dayArrival = 0,
                            teamLoyalty
                        )
                        gameStateRepository.insertNewShip(ship)

                        for (u in 1..manyInitUnitsPerShip) {
                            val unit = Unit(
                                id = Random.nextLong(),
                                unitType = UnitType.values().random(),
                                locationPlanetId = null,
                                locationShip = ship.id,
                                mission = null,
                                dayMissionComplete = 0,
                                missionTargetType = null,
                                missionTargetId = null,
                                teamLoyalty
                            )
                            gameStateRepository.insertNewUnit(unit)
                        }
                    }// ships
                }
            }

            callback()
        }// launch thread
    }


    private fun updateGameState(gameStateId: Long) {
        val gameStateWithSectors = gameStateRepository.getGameStateWithSectors(gameStateId)
        val timeVal = gameStateWithSectors.gameState.gameTime.plus(1)
        timeVal.let { gameStateRepository.updateGameTime(gameStateId, it) }
        println("my thread i:$timeVal")

        val sectorsWithPlanets = gameStateWithSectors.sectors
        for(sectorWithPlanets in sectorsWithPlanets) {
//            print("Sector: ")
            for(planetWithUnits in sectorWithPlanets.planets)  {
                val planet = planetWithUnits.planet
                var teamALoyalty = planetWithUnits.planet.teamALoyalty
                if(teamALoyalty>90) {
                    teamALoyalty = 0
                } else {
                    teamALoyalty+=10
                }
//                print("${teamALoyalty} ")
                gameStateRepository.updatePlanetLoyalty(planet.id, teamALoyalty)
            }
//            println()
        }

        postUpdate(gameStateId)
    }
}