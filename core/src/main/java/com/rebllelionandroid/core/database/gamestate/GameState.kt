package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import java.util.*

@Entity(tableName = "game_state")
data class GameState(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "gameInProgress") val gameInProgress: Boolean,
    @ColumnInfo(name = "gameTime") val gameTime: Int,
    val myTeam: TeamLoyalty,
    val gameStartedTime: Date
)
