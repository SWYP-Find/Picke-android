package com.picke.domain.feature.classroom.model

import com.picke.domain.feature.battle.model.BattleDetailBoard

data class ClassBattleDetailBoard(
    val classId: Long,
    val myRole: String,
    val closeAt: String,
    val closed: Boolean,
    val commentRequired: Boolean,
    val canParticipate: Boolean,
    val participationCompleted: Boolean,
    val battle: BattleDetailBoard
)