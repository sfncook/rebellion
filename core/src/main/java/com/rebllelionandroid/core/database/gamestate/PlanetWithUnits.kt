package com.rebllelionandroid.core.database.gamestate

import androidx.room.Embedded
import androidx.room.Relation

data class PlanetWithUnits(
        @Embedded val planet: Planet,
        @Relation(
                parentColumn = "id",
                entityColumn = "planet_id"
        )
        val units: MutableList<Unit>,

        @Relation(
                entity = Ship::class,
                parentColumn = "id",
                entityColumn = "planet_id"
        )
        val shipsWithUnits: MutableList<ShipWithUnits>,

        @Relation(
                parentColumn = "id",
                entityColumn = "planet_id"
        )
        val defenseStructures: MutableList<DefenseStructure>,

        @Relation(
                parentColumn = "id",
                entityColumn = "planet_id"
        )
        val factories: MutableList<Factory>
)