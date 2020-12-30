package com.rebllelionandroid.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebllelionandroid.core.database.gamestate.*
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.gamestate.enums.DefenseStructureType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryBuildTargetType
import com.rebllelionandroid.core.database.gamestate.enums.FactoryType
import com.rebllelionandroid.core.database.gamestate.enums.UnitType
import com.rebllelionandroid.core.database.staticTypes.StaticTypesRepository
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import com.rebllelionandroid.core.gameUpdater.GameUpdater
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

    private val _planetIdsToPlanets = mutableMapOf<Long, Planet>()
    val planetIdsToPlanets: Map<Long, Planet>
        get() = _planetIdsToPlanets


    fun loadAllGameStateWithSectors(gameId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val gameState = gameStateRepository.getGameStateWithSectors(gameId)
            _planetIdsToPlanets.clear()
            for(sectorWithPlanets in gameState.sectors) {
                for(planetWithUnits in sectorWithPlanets.planets) {
                    _planetIdsToPlanets.put(planetWithUnits.planet.id, planetWithUnits.planet)
                }
            }
            _gameState.postValue(gameState)
        }
    }
    fun getAllGameStates(callback: (allGameStates: List<GameState>) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val allGameStates = gameStateRepository.getAllGameStates()
            callback(allGameStates)
        }
    }
    fun getGameStateWithSectors(gameStateId: Long) = gameStateRepository.getGameStateWithSectors(gameStateId)
    private fun getGameState(gameStateId: Long) = gameStateRepository.getGameState(gameStateId)
    fun getGameStateWithSectors(gameStateId: Long, callback: (gameStateWithSectors: GameStateWithSectors) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val gameStateWithSectors = gameStateRepository.getGameStateWithSectors(gameStateId)
            callback(gameStateWithSectors)
        }
    }
    fun getSector(sectorId: Long) = gameStateRepository.getSector(sectorId)
    fun getSector(sectorId: Long, callback: (sector: Sector) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val sector = gameStateRepository.getSector(sectorId)
            callback(sector)
        }
    }

    fun getPlanetWithUnits(planetId: Long) = gameStateRepository.getPlanetWithUnits(planetId)
    fun getPlanetWithUnits(planetId: Long, callback: (planetWithUnits: PlanetWithUnits) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val planetWithUnits = gameStateRepository.getPlanetWithUnits(planetId)
            callback(planetWithUnits)
        }
    }
    fun getPlanet(planetId: Long) = gameStateRepository.getPlanet(planetId)
    fun getPlanet(planetId: Long, callback: (planet: Planet) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val planet = gameStateRepository.getPlanet(planetId)
            callback(planet)
        }
    }
    fun getShip(shipId: Long) = gameStateRepository.getShip(shipId)
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
    fun getAllPlanets(callback: (planets: List<Planet>) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val planets = gameStateRepository.getAllPlanets()
            callback(planets)
        }
    }
    fun getUnit(unitId: Long, callback: (unit: Unit) -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val unit = gameStateRepository.getUnit(unitId)
            callback(unit)
        }
    }
    fun getFactory(factoryId: Long) = gameStateRepository.getFactory(factoryId)


    // **** Updates *****
    fun moveUnitToShip(
        unitId: Long,
        shipId: Long,
        gameStateId: Long,
        srcPlanetId: Long? = null,
        newTeamALoyalty: Int? = null,
        newTeamBLoyalty: Int? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (srcPlanetId!=null && newTeamALoyalty!=null && newTeamBLoyalty!=null) {
                gameStateRepository.updatePlanetLoyalty(srcPlanetId, newTeamALoyalty, newTeamBLoyalty)
            }
            gameStateRepository.moveUnitToShip(unitId, shipId)
            postUpdate(gameStateId)
        }
    }
    fun moveUnitToPlanet(
        unitId: Long,
        planetId: Long,
        gameStateId: Long,
        newTeamALoyalty: Int,
        newTeamBLoyalty: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.updatePlanetLoyalty(planetId, newTeamALoyalty, newTeamBLoyalty)
            gameStateRepository.moveUnitToPlanet(unitId, planetId)
            postUpdate(gameStateId)
        }
    }
    fun startShipJourneyToPlanet(shipId: Long, destPlanetId: Long, gameStateId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val ship = getShip(shipId)
            val srcPlanet = getPlanet(ship.locationPlanetId)
            val dstPlanet = getPlanet(destPlanetId)
            val gameState = getGameState(gameStateId)
            val tripArrivalDay = Utilities.getTravelArrivalDay(srcPlanet, dstPlanet, gameState.gameTime)
            gameStateRepository.startShipJourneyToPlanet(shipId, destPlanetId, tripArrivalDay)
            postUpdate(gameStateId)
        }
    }
    fun setFactoryBuildOrder(factoryId: Long, destPlanetId: Long, buildTargetType: FactoryBuildTargetType, gameStateId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val gameState = getGameState(gameStateId)
            val dayBuildComplete = gameState.gameTime + 3
            gameStateRepository.setFactoryBuildOrder(factoryId, dayBuildComplete, buildTargetType, destPlanetId)
            postUpdate(gameStateId)
        }
    }


    // **** Game Play ****
    fun stopAllGameStates() {
        viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.stopAllGameStates()
        }
    }

    fun toggleTimer(gameStateId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val gameState2 = gameStateRepository.getGameState(gameStateId)
            if(gameState2.gameInProgress) {
                stopTimer(gameStateId)
            } else {
                startTimer(gameStateId)
            }
        }
    }

    private fun startTimer(gameStateId: Long) {
        timerJob = viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.setGameInProgress(gameStateId, 1)
            while (true) {
                val gameStateWithSectors = getGameStateWithSectors(gameStateId)
                val (newGameStateWithSectors, updateEvents, newFactories, newShips, newUnits) = GameUpdater.updateGameState(gameStateWithSectors.deepCopy())
                deepUpdateGameState(newGameStateWithSectors)
                newFactories.forEach { gameStateRepository.insert(it) }
                newShips.forEach { gameStateRepository.insert(it) }
                newUnits.forEach { gameStateRepository.insert(it) }
                postUpdate(gameStateId)
                updateEvents.forEach { println(it.getEventMessage()) }
                delay(1500)
            }
        }
    }

    private fun stopTimer(gameStateId: Long) {
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

    private fun deepUpdateGameState(gameStateWithSectors: GameStateWithSectors) {
        gameStateRepository.update(gameStateWithSectors.gameState)
        gameStateWithSectors.sectors.forEach { sectorWithPlanets ->
            sectorWithPlanets.planets.forEach { planetWithUnits ->
                if(planetWithUnits.planet.updated) {
                    planetWithUnits.planet.updated = false
                    gameStateRepository.update(planetWithUnits.planet)
                }
                planetWithUnits.shipsWithUnits.forEach { shipWithUnits ->
                    if(shipWithUnits.ship.destroyed) gameStateRepository.delete(shipWithUnits.ship)
                    else if(shipWithUnits.ship.updated) {
                        shipWithUnits.ship.updated = false
                        gameStateRepository.update(shipWithUnits.ship)
                    }
                }
                planetWithUnits.factories.forEach { factory ->
                    if(factory.updated) {
                        factory.updated = false
                        gameStateRepository.update(factory)
                    }
                }
            }
        }
    }

    fun createNewGameState(callback: () -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val gameState = GameState(Random.nextLong(), false, 1, TeamLoyalty.TeamA, Date())
            gameStateRepository.insert(gameState)

            val teamsToPlanets = mapOf(
                TeamLoyalty.TeamA to ArrayList(),
                TeamLoyalty.TeamB to ArrayList<Planet>(),
            )

            val allShipTypes = staticTypesRepository.getAllShipTypes()

            val allSectorTypes = staticTypesRepository.getAllSectorTypes()
            for (sectorType in allSectorTypes) {
                val sector = Sector(Random.nextLong(), sectorType.name, gameState.id)
                gameStateRepository.insert(sector)
                val allPlanetTypesForSector = staticTypesRepository.getAllPlanetTypesForSector(sectorType.id)
                for (planetType in allPlanetTypesForSector) {
                    var loyaltyMinTeamA = 0
                    var loyaltyMaxTeamA = 20
                    var loyaltyMinTeamB = 0
                    var loyaltyMaxTeamB = 20
                    if (sectorType.initTeamLoyalty == TeamLoyalty.TeamA) {
                        loyaltyMinTeamA = 25
                        loyaltyMaxTeamA = 100
                        loyaltyMaxTeamB = 10
                    } else if (sectorType.initTeamLoyalty == TeamLoyalty.TeamB) {
                        loyaltyMaxTeamA = 10
                        loyaltyMinTeamB = 25
                        loyaltyMaxTeamB = 100
                    }
                    val planet = Planet(
                        id = Random.nextLong(),
                        name = planetType.name,
                        sectorId = sector.id,
                        teamALoyalty = Random.nextInt(loyaltyMinTeamA, loyaltyMaxTeamA),
                        teamBLoyalty = Random.nextInt(loyaltyMinTeamB, loyaltyMaxTeamB),
                        isExplored = sectorType.initTeamLoyalty == gameState.myTeam,
                        energyCap = Random.nextInt(10),
                        locationIndex = planetType.locationIndex
                    )
                    gameStateRepository.insert(planet)
                    teamsToPlanets[sectorType.initTeamLoyalty]?.add(planet)
                }
            }

            // Setup Team Head Quarters
            arrayListOf(TeamLoyalty.TeamA, TeamLoyalty.TeamB).forEach { teamLoyalty ->
                val planetsForTeam = teamsToPlanets[teamLoyalty]
                val planetForTeam = planetsForTeam?.get(Random.nextInt(planetsForTeam.size))

                if(planetForTeam != null) {
                    planetForTeam.teamALoyalty = if(teamLoyalty==TeamLoyalty.TeamA) 100 else 0
                    planetForTeam.teamBLoyalty = if(teamLoyalty==TeamLoyalty.TeamB) 100 else 0
                    gameStateRepository.update(planetForTeam)

                    // Orbital battery
                    val orbitalBattery = DefenseStructure(
                        id = Random.nextLong(),
                        defenseStructureType = DefenseStructureType.OrbitalBattery,
                        locationPlanetId = planetForTeam.id,
                        isTraveling = false,
                        dayArrival = 0
                    )
                    gameStateRepository.insert(orbitalBattery)

                    // Planetary shield
                    val planetaryShield = DefenseStructure(
                        id = Random.nextLong(),
                        defenseStructureType = DefenseStructureType.PlanetaryShield,
                        locationPlanetId = planetForTeam.id,
                        isTraveling = false,
                        dayArrival = 0
                    )
                    gameStateRepository.insert(planetaryShield)

                    val manyInitFactories = planetForTeam.energyCap.coerceAtMost(3)
                    for (u in 1..manyInitFactories) {
                        val planetId = planetForTeam.id
                        val factory = Factory(
                            id = Random.nextLong(),
                            factoryType = FactoryType.values().random(),
                            planetId,
                            team = teamLoyalty,
                            buildTargetType = null,
                            dayBuildComplete = 0,
                            isTraveling = false,
                            dayArrival = 0
                        )
                        gameStateRepository.insert(factory)
                    }

                    val manyInitShips = 5
                    val manyInitUnitsPerShip = 4
                    for (s in 1..manyInitShips) {
                        val shipType = allShipTypes.random()
                        val ship = Ship(
                            id = Random.nextLong(),
                            locationPlanetId = planetForTeam.id,
                            shipType = shipType.shipType,
                            isTraveling = false,
                            dayArrival = 0,
                            team = teamLoyalty,
                            attackStrength = shipType.attackStrength.toInt(),
                            defenseStrength = shipType.defenseStrength.toInt(),
                            destroyed = false,
                            healthPoints = shipType.defenseStrength.toInt()
                        )
                        gameStateRepository.insert(ship)

                        for (uPs in 1..manyInitUnitsPerShip) {
                            val unit = Unit(
                                id = Random.nextLong(),
                                unitType = UnitType.values().random(),
                                locationPlanetId = null,
                                locationShip = ship.id,
                                mission = null,
                                dayMissionComplete = 0,
                                missionTargetType = null,
                                missionTargetId = null,
                                team = teamLoyalty,
                                updated = false
                            )
                            Utilities.setUnitStrengthValues(unit)
                            gameStateRepository.insert(unit)
                        }
                    }// ships
                }
            }

            callback()
        }// launch thread
    }

}