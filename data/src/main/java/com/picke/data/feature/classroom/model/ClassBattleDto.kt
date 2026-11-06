package com.picke.data.feature.classroom.model

import com.picke.data.feature.battle.model.BattleDetailDto
import com.picke.data.feature.battle.model.toDomainModel
import com.picke.domain.feature.battle.model.BattleDetailBoard
import com.picke.domain.feature.battle.model.BattleInfoBoard
import com.picke.domain.feature.classroom.model.ClassBattleDetailBoard
import kotlinx.serialization.Serializable

@Serializable
data class ClassBattleDetailResponseDto(
    val classId: Long?,
    val myRole: String?,
    val closeAt: String?,
    val closed: Boolean?,
    val commentRequired: Boolean?,
    val canParticipate: Boolean?,
    val participationCompleted: Boolean?,
    val midVoteEnabled: Boolean?,
    val battle: BattleDetailDto?
)

@Serializable
data class ClassMidVoteRequestDto(
    val checkpointId: Long,
    val optionId: Long
)

fun ClassBattleDetailResponseDto.toDomainModel() = ClassBattleDetailBoard(
    classId = classId ?: 0L,
    myRole = myRole ?: "",
    closeAt = closeAt ?: "",
    closed = closed ?: false,
    commentRequired = commentRequired ?: false,
    canParticipate = canParticipate ?: false,
    participationCompleted = participationCompleted ?: false,
    midVoteEnabled = midVoteEnabled ?: false,
    battle = battle?.toDomainModel() ?: BattleDetailBoard(
        battleInfo = BattleInfoBoard.empty(),
        description = "",
        shareUrl = "",
        userVoteStatus = "NONE",
        currentStep = "",
        categoryTags = emptyList(),
        philosopherTags = emptyList(),
        valueTags = emptyList()
    )
)