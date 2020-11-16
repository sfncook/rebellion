package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "game_state")
data class GameState(
    @PrimaryKey(autoGenerate = true) val id: Int = 100,
    @ColumnInfo(name = "gameInProgress") val gameInProgress: Boolean = false,
    @ColumnInfo(name = "gameTime") val gameTime: Int = 0
)
