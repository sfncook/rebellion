package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        tableName = "sectors",
        foreignKeys = [ForeignKey(entity = GameState::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("game_state_id"),
                onDelete = ForeignKey.CASCADE)])
data class Sector(
        @PrimaryKey val id: Long,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "game_state_id") val gameStateId: Long
)
