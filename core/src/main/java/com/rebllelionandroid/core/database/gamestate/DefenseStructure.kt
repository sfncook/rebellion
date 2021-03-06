package com.rebllelionandroid.core.database.gamestate

import androidx.room.*
import com.rebllelionandroid.core.database.gamestate.enums.DefenseStructureType

@Entity(
        tableName = "defense_structures",
        foreignKeys = [
                ForeignKey(entity = Planet::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("planet_id"),
                        onDelete = ForeignKey.CASCADE)
        ]
)
data class DefenseStructure(
        @PrimaryKey var id: Long = 0,
        var defenseStructureType: DefenseStructureType = DefenseStructureType.OrbitalBattery,

        var attackStrength: Int = 0,
        var defenseStrength: Int = 0,

        @ColumnInfo(name = "planet_id", index = true) var locationPlanetId: Long? = null,
        var isTraveling: Boolean = false, // Travelling for delivery
        var dayArrival: Long? = null,
        var destroyed: Boolean = false,
        var maxHealthPoints: Int = 0,
        var healthPoints: Int = 0,

        @Ignore var updated: Boolean = false,
        @Ignore var created: Boolean = false
)
