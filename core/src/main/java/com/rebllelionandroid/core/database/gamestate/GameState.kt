package com.rebllelionandroid.core.database.gamestate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty
import java.util.*

@Entity(tableName = "game_state")
data class GameState(
    @PrimaryKey var id: Long,
    @ColumnInfo(name = "gameInProgress") var gameInProgress: Boolean,
    @ColumnInfo(name = "gameTime") var gameTime: Long,
    var myTeam: TeamLoyalty,
    var gameStartedTime: Date
)
