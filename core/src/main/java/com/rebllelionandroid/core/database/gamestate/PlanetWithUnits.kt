package com.rebllelionandroid.core.database.gamestate

import androidx.room.Embedded
import androidx.room.Relation

data class PlanetWithUnits(
        @Embedded val planet: Planet,
        @Relation(
                parentColumn = "id",
                entityColumn = "planet_id"
        )
        val units: List<Unit>
)