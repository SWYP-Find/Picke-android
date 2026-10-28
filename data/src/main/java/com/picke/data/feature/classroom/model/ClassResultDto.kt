package com.picke.data.feature.classroom.model

import com.picke.domain.feature.classroom.model.ClassResultBoard
import com.picke.domain.feature.classroom.model.ClassResultOptionBoard
import com.picke.domain.feature.classroom.model.ClassResultStatsBoard

data class ClassResultResponseDto(
    val classId: Long?,
    val myRole: String?,
    val closeAt: String?,
    val closed: Boolean?,
    val viewableUntil: String?,
    val statsBasis: String?,
    val cutoffAt: String?,
    val stats: ClassResultStatsDto?,
    val myReportState: String?,
    val insightState: String?
)

data class ClassResultStatsDto(
    val activeMemberCount: Int?,
    val completedCount: Int?,
    val participationRate: Double?,
    val commentWriterCount: Int?,
    val commentRate: Double?,
    val postVoteCount: Int?,
    val options: List<ClassResultOptionDto>?
)

data class ClassResultOptionDto(
    val optionId: Long?,
    val title: String?,
    val stance: String?,
    val preVoteCount: Int?,
    val postVoteCount: Int?,
    val postVoteRatio: Double?
)

fun ClassResultResponseDto.toDomainModel() = ClassResultBoard(
    classId = classId ?: 0L,
    myRole = myRole ?: "",
    closeAt = closeAt ?: "",
    closed = closed ?: false,
    viewableUntil = viewableUntil ?: "",
    statsBasis = statsBasis ?: "",
    cutoffAt = cutoffAt,
    stats = stats?.toDomainModel() ?: ClassResultStatsBoard(
        activeMemberCount = 0,
        completedCount = 0,
        participationRate = 0.0,
        commentWriterCount = 0,
        commentRate = 0.0,
        postVoteCount = 0,
        options = emptyList()
    ),
    myReportState = myReportState,
    insightState = insightState
)

fun ClassResultStatsDto.toDomainModel() = ClassResultStatsBoard(
    activeMemberCount = activeMemberCount ?: 0,
    completedCount = completedCount ?: 0,
    participationRate = participationRate ?: 0.0,
    commentWriterCount = commentWriterCount ?: 0,
    commentRate = commentRate ?: 0.0,
    postVoteCount = postVoteCount ?: 0,
    options = options?.map { it.toDomainModel() } ?: emptyList()
)

fun ClassResultOptionDto.toDomainModel() = ClassResultOptionBoard(
    optionId = optionId ?: 0L,
    title = title ?: "",
    stance = stance ?: "",
    preVoteCount = preVoteCount ?: 0,
    postVoteCount = postVoteCount ?: 0,
    postVoteRatio = postVoteRatio ?: 0.0
)