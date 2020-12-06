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
    fun getGameState(gameStateId: Long) = gameStateRepository.getGameState(gameStateId)
    fun getSectorWithPlanets(sectorId: Long) = gameStateRepository.getSectorWithPlanets(sectorId)
    fun getPlanetWithUnits(planetId: Long, callback: (planetWithUnits: PlanetWithUnits) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val planetWithUnits = gameStateRepository.getPlanetWithUnits(planetId)
            callback(planetWithUnits)
        }
    }
    fun getAllUnitsOnTheSurfaceOfPlanet(planetId: Long, callback: (units: List<Unit>) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val units = gameStateRepository.getAllUnitsOnTheSurfaceOfPlanet(planetId)
            callback(units)
        }
    }
    fun getAllUnitsOnShip(shipId: Long, callback: (units: List<Unit>) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val units = gameStateRepository.getAllUnitsOnShip(shipId)
            callback(units)
        }
    }


    fun startTimer(gameStateId: Long) {
        timerJob = viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.setGameInProgress(gameStateId, 1)
            while (true) {
                val gameState = gameStateRepository.getGameState(gameStateId)
                val timeVal = gameState.gameTime.plus(1)
                timeVal.let { gameStateRepository.updateGameTime(gameStateId, it) }
                println("my thread i:$timeVal")
                updateGameState()
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
        }
    }

    fun createNewGameState(callback: () -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val gameState = GameState(Random.nextLong(), false, 1, TeamLoyalty.TeamA, Date())
            gameStateRepository.createNewGameState(gameState)

            val allPlanets = ArrayList<Planet>()
            val planetsTeamA = ArrayList<Planet>()
            val planetsTeamB = ArrayList<Planet>()

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
                    allPlanets.add(planet)
                    if (sectorType.initTeamLoyalty == TeamLoyalty.TeamA) {
                        planetsTeamA.add(planet)
                    } else if (sectorType.initTeamLoyalty == TeamLoyalty.TeamB) {
                        planetsTeamB.add(planet)
                    }
                }
            }

            // Create units
            // Orbital battery
            val orbitalBattery = DefenseStructure(
                    id = Random.nextLong(),
                    defenseStructureType = DefenseStructureType.OrbitalBattery,
                    locationPlanetId = planetsTeamA[Random.nextInt(planetsTeamA.size)].id,
                    isTravelling = false,
                    dayArrival = 0
            )
            gameStateRepository.insertNewDefenseStructure(orbitalBattery)

            // Planetary shield
            val planetaryShield = DefenseStructure(
                    id = Random.nextLong(),
                    defenseStructureType = DefenseStructureType.PlanetaryShield,
                    locationPlanetId = planetsTeamA[Random.nextInt(planetsTeamA.size)].id,
                    isTravelling = false,
                    dayArrival = 0
            )
            gameStateRepository.insertNewDefenseStructure(planetaryShield)

            val manyInitFactories = 3
            for (u in 1..manyInitFactories) {
                val planetId = planetsTeamA[Random.nextInt(planetsTeamA.size)].id
                val factory = Factory(
                        id = Random.nextLong(),
                        factoryType = FactoryType.ConstructionYard,
                        planetId,
                        buildTargetType = null,
                        dayBuildComplete = 0,
                        isTravelling = false,
                        dayArrival = 0
                )
                gameStateRepository.insertNewFactory(factory)
            }

            val manyInitShips = 5
            for (u in 1..manyInitShips) {
                val ship = Ship(
                        id = Random.nextLong(),
                        locationPlanetId = planetsTeamA[Random.nextInt(planetsTeamA.size)].id,
                        shipType = ShipType.Biremes,
                        isTraveling = false,
                        dayArrival = 0
                )
                gameStateRepository.insertNewShip(ship)
            }

            val manyInitUnits = 5
            for (u in 1..manyInitUnits) {
                val unit = Unit(
                        id = Random.nextLong(),
                        unitType = UnitType.Garrison,
                        locationPlanetId = planetsTeamA[Random.nextInt(planetsTeamA.size)].id,
                        locationShip = null,
                        mission = null,
                        dayMissionComplete = 0,
                        missionTargetType = null,
                        missionTargetId = null
                )
                gameStateRepository.insertNewUnit(unit)
            }

            callback()
        }
    }


    private fun updateGameState() {
//        val gameStateWithSectors = getCurrentGameStateWithSectors()
//        val sectorsWithPlanets = gameStateWithSectors.sectors
//        for(sectorWithPlanets in sectorsWithPlanets) {
//            print("Sector: ")
//            for(planetWithUnits in sectorWithPlanets.planets)  {
//                val planet = planetWithUnits.planet
//                var teamALoyalty = planetWithUnits.planet.teamALoyalty
//                if(teamALoyalty>90) {
//                    teamALoyalty = 0
//                } else {
//                    teamALoyalty+=10
//                }
//                print("${teamALoyalty} ")
//                gameStateRepository.updatePlanetLoyalty(planet.id, teamALoyalty)
//            }
//            println()
//        }
    }
}