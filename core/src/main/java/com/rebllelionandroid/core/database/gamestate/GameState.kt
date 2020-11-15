package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_state")
data class GameState(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "gameInProgress", defaultValue = "false") val gameInProgress: Boolean,
    @ColumnInfo(name = "gameTime", defaultValue = "0") val gameTime: Int
)
