package com.picke.data.feature.classroom.model

import com.picke.data.common.error.requireField
import com.picke.domain.feature.classroom.model.ClassDebateBoard
import com.picke.domain.feature.classroom.model.ClassDebateMessageBoard
import com.picke.domain.feature.classroom.model.ClassDebatePhilosopherBoard
import com.picke.domain.feature.classroom.model.ClassDebateSideBoard
import kotlinx.serialization.Serializable

@Serializable
data class ClassDebateReplyRequestDto(
    val roundIndex: Int,
    val content: String,
    val requestKey: String
)

@Serializable
data class ClassDebateResponseDto(
    val sessionId: Long,
    val status: String,
    val round: Int,
    val canReply: Boolean,
    val canRetry: Boolean,
    val philosopher: ClassDebatePhilosopherDto,
    val participantOption: ClassDebateSideDto,
    val opponentOption: ClassDebateSideDto,
    val messages: List<ClassDebateMessageDto>,
    val aiNotice: String,
    val replyMinLength: Int,
    val replyMaxLength: Int
)

@Serializable
data class ClassDebatePhilosopherDto(
    val code: String,
    val name: String,
    val typeName: String,
    val imageKey: String
)

@Serializable
data class ClassDebateSideDto(
    val optionId: Long,
    val position: String,
    val title: String,
    val stance: String?
)

@Serializable
data class ClassDebateMessageDto(
    val messageId: Long,
    val round: Int,
    val speaker: String,
    val content: String,
    val bubbles: List<String>?,
    val focus: String?,
    val focusLabel: String?,
    val replyToMessageId: Long?,
    val createdAt: String
)

fun ClassDebateResponseDto.toDomainModel() = ClassDebateBoard(
    sessionId = sessionId,
    status = status,
    round = round,
    canReply = canReply,
    canRetry = canRetry,
    philosopher = philosopher.toDomainModel(),
    participantOption = participantOption.toDomainModel(),
    opponentOption = opponentOption.toDomainModel(),
    messages = messages.map { it.toDomainModel() },
    aiNotice = aiNotice,
    replyMinLength = replyMinLength,
    replyMaxLength = replyMaxLength
)

fun ClassDebatePhilosopherDto.toDomainModel() = ClassDebatePhilosopherBoard(
    code = code,
    name = name,
    typeName = typeName,
    imageKey = imageKey
)

fun ClassDebateSideDto.toDomainModel() = ClassDebateSideBoard(
    optionId = optionId,
    position = position,
    title = title,
    stance = stance.requireField("ClassDebateSideDto.stance")
)

fun ClassDebateMessageDto.toDomainModel() = ClassDebateMessageBoard(
    messageId = messageId,
    round = round,
    speaker = speaker,
    content = content,
    bubbles = bubbles ?: emptyList(),
    focus = focus,
    focusLabel = focusLabel,
    replyToMessageId = replyToMessageId,
    createdAt = createdAt
)