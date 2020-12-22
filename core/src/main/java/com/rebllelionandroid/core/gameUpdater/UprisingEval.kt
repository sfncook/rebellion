package com.rebllelionandroid.core.gameUpdater

class UprisingEval {
    companion object {
        private val garrToUpris_0_24: Map<Int, Pair<UprisingRank, Int>> = mapOf(
            0 to Pair(UprisingRank.Civil, 0),
            1 to Pair(UprisingRank.Uprising, 0),
            2 to Pair(UprisingRank.Rebellion, 0)
        )
        private val garrToUpris_25_49: Map<Int, UprisingRank> = mapOf(
            0 to UprisingRank.Civil,
            1 to UprisingRank.Unrest,
            2 to UprisingRank.Uprising,
            3 to UprisingRank.Rebellion
        )
        private val garrToUpris_50_74: Map<Int, UprisingRank> = mapOf(
            0 to UprisingRank.Civil,
            1 to UprisingRank.Civil,
            2 to UprisingRank.Unrest,
            3 to UprisingRank.Unrest,
            4 to UprisingRank.Uprising,
            5 to UprisingRank.Uprising,
            6 to UprisingRank.Rebellion
        )
        private val garrToUpris_75_100: Map<Int, UprisingRank> = mapOf(
            0 to UprisingRank.Civil,
            1 to UprisingRank.Civil,
            2 to UprisingRank.Civil,
            3 to UprisingRank.Civil,
            4 to UprisingRank.Unrest,
            5 to UprisingRank.Unrest,
            8 to UprisingRank.Uprising,
        )

        private val loytaltyToGarrisonToUprisingRank: Array<Map<Int, UprisingRank>> = arrayOf(
            *Array(25) {garrToUpris_0_24},
            *Array(25) {garrToUpris_25_49},
            *Array(25) {garrToUpris_50_74},
            *Array(25) {garrToUpris_75_100}
        )


        fun getUprisingRank(loyalty: Int, manyGarrisonUnits: Int): UprisingRank {
            return loytaltyToGarrisonToUprisingRank[loyalty][manyGarrisonUnits] ?: UprisingRank.Rebellion
        }

        fun getSuppressionUnits(loyalty: Int, manyGarrisonUnits: Int): UprisingRank {
            return loytaltyToGarrisonToUprisingRank[loyalty][manyGarrisonUnits] ?: UprisingRank.Rebellion
        }
    }
}
