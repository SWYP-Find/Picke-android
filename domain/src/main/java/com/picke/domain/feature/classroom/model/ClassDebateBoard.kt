package com.picke.domain.feature.classroom.model

data class ClassDebateBoard(
    val sessionId: Long,
    val status: String,
    val round: Int,
    val canReply: Boolean,
    val canRetry: Boolean,
    val philosopher: ClassDebatePhilosopherBoard,
    val participantOption: ClassDebateSideBoard,
    val opponentOption: ClassDebateSideBoard,
    val messages: List<ClassDebateMessageBoard>,
    val aiNotice: String,
    val replyMinLength: Int,
    val replyMaxLength: Int
)

data class ClassDebatePhilosopherBoard(
    val code: String,
    val name: String,
    val typeName: String,
    val imageKey: String
)

data class ClassDebateSideBoard(
    val optionId: Long,
    val position: String,
    val title: String,
    val stance: String
)

data class ClassDebateMessageBoard(
    val messageId: Long,
    val round: Int,
    val speaker: String,
    val content: String,
    val focus: String?,
    val focusLabel: String?,
    val replyToMessageId: Long?,
    val createdAt: String
)