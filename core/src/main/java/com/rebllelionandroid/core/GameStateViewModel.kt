package com.rebllelionandroid.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebllelionandroid.core.database.gamestate.*
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.staticTypes.StaticTypesRepository
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
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
    val time: MutableLiveData<Int> = MutableLiveData(0)
    val gameStateLive = gameStateRepository.getGameStateLive()

    fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                val timeVal = time.value?.plus(1)
                time.value = timeVal
                println("my thread i:$timeVal")
                delay(2000)
            }
        }
    }

    fun stopTimer() {
        if(this::timerJob.isInitialized) {
            timerJob.cancel()
        }
    }

    fun createNewGameState() {
        val gameState = GameState(Random.nextLong(), false, 1, TeamLoyalty.TeamA)
        gameStateRepository.createNewGameState(gameState)

        val allPlanets = ArrayList<Planet>()

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
            }
        }

//        val allUnitTypes = staticTypesRepository.getAllUnitTypes()
//        for(unitType in allUnitTypes) {
//            val planet = allPlanets.random()
//            val unit = Unit(Random.nextLong(), unitType.name, planet.id)
//            gameStateRepository.insertNewUnit(unit)
//        }
    }

    fun getCurrentGameStateWithSectors() = gameStateRepository.getCurrentGameStateWithSectors()

    fun getManyGameStates() = gameStateRepository.getManyGameStates()

    fun getSectorWithPlanets(sectorId: Long) = gameStateRepository.getSectorWithPlanets(sectorId)
}