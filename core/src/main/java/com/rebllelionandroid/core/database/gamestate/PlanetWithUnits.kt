package com.rebllelionandroid.core.database.gamestate

import androidx.room.Embedded
import androidx.room.Relation

data class PlanetWithUnits(
        @Embedded val planet: Planet,
        @Relation(
                parentColumn = "id",
                entityColumn = "planet_id"
        )
        val units: List<Unit>,

        @Relation(
                entity = Ship::class,
                parentColumn = "id",
                entityColumn = "planet_id"
        )
        val shipsWithUnits: List<ShipWithUnits>,

        @Relation(
                parentColumn = "id",
                entityColumn = "planet_id"
        )
        val defenseStructures: List<DefenseStructure>,

        @Relation(
                parentColumn = "id",
                entityColumn = "planet_id"
        )
        val factories: List<Factory>
)