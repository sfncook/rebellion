package com.rebllelionandroid.core.database.gamestate

import androidx.room.Embedded
import androidx.room.Relation

data class GameStateWithSectors(
        @Embedded val gameState: GameState,
        @Relation(
                entity = Sector::class,
                parentColumn = "id",
                entityColumn = "game_state_id"
        )
        val sectors: List<SectorWithPlanets>
)