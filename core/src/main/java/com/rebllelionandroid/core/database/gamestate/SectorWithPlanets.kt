package com.rebllelionandroid.core.database.gamestate

import androidx.room.Embedded
import androidx.room.Relation

data class SectorWithPlanets(
        @Embedded val sector: Sector,
        @Relation(
                parentColumn = "id",
                entityColumn = "sector_id"
        )
        val planets: List<Planet>
)