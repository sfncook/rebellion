package com.rebllelionandroid.core

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
import javax.inject.Inject
import kotlin.random.Random

class GameStateViewModel @Inject constructor(
    private val gameStateRepository: GameStateRepository,
    private val staticTypesRepository: StaticTypesRepository
) : ViewModel() {
    lateinit var timerJob: Job
    val gameStateLive = gameStateRepository.getGameStateLive()

    fun startTimer() {
        timerJob = viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.setGameInProgress(1)
            while (true) {
                val gameState = gameStateRepository.getGameState()
                val timeVal = gameState.gameTime.plus(1)
                timeVal.let { gameStateRepository.updateGameTime(it) }
                println("my thread i:$timeVal")
                delay(2000)
            }
        }
    }

    fun stopTimer() {
        if(this::timerJob.isInitialized) {
            timerJob.cancel()
        }
        viewModelScope.launch(Dispatchers.IO) {
            gameStateRepository.setGameInProgress(0)
        }
    }

    fun createNewGameState() {
        val gameState = GameState(Random.nextLong(), false, 1, TeamLoyalty.TeamA)
        gameStateRepository.createNewGameState(gameState)

        val allPlanets = ArrayList<Planet>()
        val planetsTeamA = ArrayList<Planet>()
        val planetsTeamB = ArrayList<Planet>()

        val allSectorTypes = staticTypesRepository.getAllSectorTypes()
        for(sectorType in allSectorTypes) {
            val sector = Sector(Random.nextLong(), sectorType.name, gameState.id)
            gameStateRepository.insertNewSector(sector)
            val allPlanetTypesForSector = staticTypesRepository.getAllPlanetTypesForSector(sectorType.id)
            for(planetType in allPlanetTypesForSector) {
                var loyaltyMinTeamA = 0
                var loyaltyMaxTeamA = 20
                var loyaltyMinTeamB = 0
                var loyaltyMaxTeamB = 20
                if(sectorType.initTeamLoyalty == TeamLoyalty.TeamA) {
                    loyaltyMinTeamA = 10
                    loyaltyMaxTeamA = 50
                    loyaltyMaxTeamB = 10
                } else  if(sectorType.initTeamLoyalty == TeamLoyalty.TeamB) {
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
                if(sectorType.initTeamLoyalty == TeamLoyalty.TeamA) {
                    planetsTeamA.add(planet)
                } else if(sectorType.initTeamLoyalty == TeamLoyalty.TeamB) {
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
        for(u in 1..manyInitFactories) {
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
        for(u in 1..manyInitShips) {
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
        for(u in 1..manyInitUnits) {
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
    }

    fun getCurrentGameStateWithSectors() = gameStateRepository.getCurrentGameStateWithSectors()

    fun getManyGameStates() = gameStateRepository.getManyGameStates()

    fun getSectorWithPlanets(sectorId: Long) = gameStateRepository.getSectorWithPlanets(sectorId)
}