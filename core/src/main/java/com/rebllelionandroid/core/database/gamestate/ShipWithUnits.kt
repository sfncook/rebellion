package com.rebllelionandroid.core.database.gamestate

import androidx.room.Embedded
import androidx.room.Relation

data class ShipWithUnits(
        @Embedded val ship: Ship,
        @Relation(
                parentColumn = "id",
                entityColumn = "ship_id"
        )
        val units: List<Unit>,
)