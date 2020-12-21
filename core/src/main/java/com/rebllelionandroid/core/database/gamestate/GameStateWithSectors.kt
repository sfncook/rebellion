package com.rebllelionandroid.core.database.gamestate

import androidx.room.Embedded
import androidx.room.Relation
import com.google.gson.Gson

data class GameStateWithSectors(
        @Embedded var gameState: GameState,
        @Relation(
                entity = Sector::class,
                parentColumn = "id",
                entityColumn = "game_state_id"
        )
        var sectors: List<SectorWithPlanets>
) {
    fun deepCopy() : GameStateWithSectors {
        val json = Gson().toJson(this)
        return Gson().fromJson(json, this.javaClass)
    }
}
