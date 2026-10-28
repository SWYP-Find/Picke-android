package com.picke.data.feature.classroom.model

import com.picke.domain.feature.classroom.model.ClassDebateBoard
import com.picke.domain.feature.classroom.model.ClassDebateMessageBoard
import com.picke.domain.feature.classroom.model.ClassDebatePhilosopherBoard
import com.picke.domain.feature.classroom.model.ClassDebateSideBoard

data class ClassDebateResponseDto(
    val sessionId: Long?,
    val status: String?,
    val round: Int?,
    val canReply: Boolean?,
    val canRetry: Boolean?,
    val philosopher: ClassDebatePhilosopherDto?,
    val participantOption: ClassDebateSideDto?,
    val opponentOption: ClassDebateSideDto?,
    val messages: List<ClassDebateMessageDto>?,
    val aiNotice: String?,
    val replyMinLength: Int?,
    val replyMaxLength: Int?
)

data class ClassDebatePhilosopherDto(
    val code: String?,
    val name: String?,
    val typeName: String?,
    val imageKey: String?
)

data class ClassDebateSideDto(
    val optionId: Long?,
    val position: String?,
    val title: String?,
    val stance: String?
)

data class ClassDebateMessageDto(
    val messageId: Long?,
    val round: Int?,
    val speaker: String?,
    val content: String?,
    val focus: String?,
    val focusLabel: String?,
    val replyToMessageId: Long?,
    val createdAt: String?
)

fun ClassDebateResponseDto.toDomainModel() = ClassDebateBoard(
    sessionId = sessionId ?: 0L,
    status = status ?: "",
    round = round ?: 1,
    canReply = canReply ?: false,
    canRetry = canRetry ?: false,
    philosopher = philosopher?.toDomainModel() ?: ClassDebatePhilosopherBoard("", "", "", ""),
    participantOption = participantOption?.toDomainModel() ?: ClassDebateSideBoard(0L, "", "", ""),
    opponentOption = opponentOption?.toDomainModel() ?: ClassDebateSideBoard(0L, "", "", ""),
    messages = messages?.map { it.toDomainModel() } ?: emptyList(),
    aiNotice = aiNotice ?: "",
    replyMinLength = replyMinLength ?: 0,
    replyMaxLength = replyMaxLength ?: 0
)

fun ClassDebatePhilosopherDto.toDomainModel() = ClassDebatePhilosopherBoard(
    code = code ?: "",
    name = name ?: "",
    typeName = typeName ?: "",
    imageKey = imageKey ?: ""
)

fun ClassDebateSideDto.toDomainModel() = ClassDebateSideBoard(
    optionId = optionId ?: 0L,
    position = position ?: "",
    title = title ?: "",
    stance = stance ?: ""
)

fun ClassDebateMessageDto.toDomainModel() = ClassDebateMessageBoard(
    messageId = messageId ?: 0L,
    round = round ?: 0,
    speaker = speaker ?: "",
    content = content ?: "",
    focus = focus,
    focusLabel = focusLabel,
    replyToMessageId = replyToMessageId,
    createdAt = createdAt ?: ""
)