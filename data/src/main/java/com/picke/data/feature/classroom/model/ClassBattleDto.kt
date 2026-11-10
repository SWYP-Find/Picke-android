package com.picke.data.feature.classroom.model

import com.picke.data.common.error.requireField
import com.picke.data.feature.battle.model.BattleDetailDto
import com.picke.data.feature.battle.model.toDomainModel
import com.picke.domain.feature.classroom.model.ClassBattleDetailBoard
import kotlinx.serialization.Serializable

@Serializable
data class ClassBattleDetailResponseDto(
    val classId: Long,
    val myRole: String,
    val closeAt: String?,
    val closed: Boolean,
    val commentRequired: Boolean,
    val canParticipate: Boolean,
    val participationCompleted: Boolean,
    val midVoteEnabled: Boolean,
    val battle: BattleDetailDto
)

@Serializable
data class ClassMidVoteRequestDto(
    val checkpointId: Long,
    val optionId: Long
)

fun ClassBattleDetailResponseDto.toDomainModel() = ClassBattleDetailBoard(
    classId = classId,
    myRole = myRole,
    closeAt = closeAt.requireField("ClassBattleDetailResponseDto.closeAt"),
    closed = closed,
    commentRequired = commentRequired,
    canParticipate = canParticipate,
    participationCompleted = participationCompleted,
    midVoteEnabled = midVoteEnabled,
    battle = battle.toDomainModel()
)