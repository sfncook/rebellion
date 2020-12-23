package com.rebllelionandroid.core.gameUpdater

import com.rebllelionandroid.core.Utilities
import com.rebllelionandroid.core.database.gamestate.PlanetWithUnits
import com.rebllelionandroid.core.database.staticTypes.enums.TeamLoyalty

class UprisingEval {
    companion object {
        private val garrToUpris_0_24: Map<Int, Pair<UprisingRank, Int>> = mapOf(
            0 to Pair(UprisingRank.Civil, 0),
            1 to Pair(UprisingRank.Uprising, 0),
            2 to Pair(UprisingRank.Rebellion, 0)
        )
        private val garrToUpris_25_49: Map<Int, Pair<UprisingRank, Int>> = mapOf(
            0 to Pair(UprisingRank.Civil,0),
            1 to Pair(UprisingRank.Unrest,0),
            2 to Pair(UprisingRank.Uprising,0),
            3 to Pair(UprisingRank.Rebellion,0),
        )
        private val garrToUpris_50_74: Map<Int, Pair<UprisingRank, Int>> = mapOf(
            0 to Pair(UprisingRank.Civil,0),
            1 to Pair(UprisingRank.Civil,0),
            2 to Pair(UprisingRank.Unrest,0),
            3 to Pair(UprisingRank.Unrest,0),
            4 to Pair(UprisingRank.Uprising,0),
            5 to Pair(UprisingRank.Uprising,0),
            6 to Pair(UprisingRank.Rebellion,0),
        )
        private val garrToUpris_75_100: Map<Int, Pair<UprisingRank, Int>> = mapOf(
            0 to Pair(UprisingRank.Civil, 0),
            1 to Pair(UprisingRank.Civil, 0),
            2 to Pair(UprisingRank.Civil, 0),
            3 to Pair(UprisingRank.Civil, 0),
            4 to Pair(UprisingRank.Unrest, 0),
            5 to Pair(UprisingRank.Unrest, 0),
            8 to Pair(UprisingRank.Uprising, 0),
        )

        private val loytaltyToGarrisonToUprisingRank: Array<Map<Int, Pair<UprisingRank, Int>>> = arrayOf(
            *Array(25) {garrToUpris_0_24},
            *Array(25) {garrToUpris_25_49},
            *Array(25) {garrToUpris_50_74},
            *Array(25) {garrToUpris_75_100}
        )


        fun getUprisingRank(loyalty: Int, manyGarrisonUnits: Int): UprisingRank {
            return loytaltyToGarrisonToUprisingRank[loyalty][manyGarrisonUnits]?.first ?: UprisingRank.Rebellion
        }

        fun getSuppressionUnits(loyalty: Int, manyGarrisonUnits: Int): Int {
            return loytaltyToGarrisonToUprisingRank[loyalty][manyGarrisonUnits]?.second ?: 8
        }
    }
}
