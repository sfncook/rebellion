package com.rebllelionandroid.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rebllelionandroid.core.database.gamestate.*
import com.rebllelionandroid.core.database.gamestate.Unit
import com.rebllelionandroid.core.database.staticTypes.StaticTypesRepository
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
        val gameState = GameState(Random.nextLong(), false, 1)
        gameStateRepository.createNewGameState(gameState)

        val allPlanets = ArrayList<Planet>()

        val allSectorTypes = staticTypesRepository.getAllSectorTypes()
        for(sectorType in allSectorTypes) {
            val sector = Sector(Random.nextLong(), sectorType.name, gameState.id)
            gameStateRepository.insertNewSector(sector)
            val allPlanetTypesForSector = staticTypesRepository.getAllPlanetTypesForSector(sectorType.id)
            for(planetType in allPlanetTypesForSector) {
                val planet = Planet(Random.nextLong(), planetType.name, sector.id)
                gameStateRepository.insertNewPlanet(planet)
                allPlanets.add(planet)
            }
        }

        val allUnitTypes = staticTypesRepository.getAllUnitTypes()
        for(unitType in allUnitTypes) {
            val planet = allPlanets.random()
            val unit = Unit(Random.nextLong(), unitType.name, planet.id)
            gameStateRepository.insertNewUnit(unit)
        }
    }

    fun getGameState(): GameState {
        return gameStateRepository.getGameState()
    }

    fun getCurrentGameStateWithSectors() = gameStateRepository.getCurrentGameStateWithSectors()
}