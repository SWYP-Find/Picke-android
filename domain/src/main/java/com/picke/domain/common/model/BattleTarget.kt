package com.picke.domain.common.model

sealed interface BattleTarget {
    data class General(val battleId: Long) : BattleTarget
    data class Class(val classId: Long) : BattleTarget
}