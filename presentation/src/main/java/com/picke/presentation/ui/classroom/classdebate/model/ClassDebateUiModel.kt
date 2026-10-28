package com.picke.presentation.ui.classroom.classdebate.model

import com.picke.domain.feature.classroom.model.ClassDebateBoard

data class ClassDebateUiModel(
    val round: Int = 1,
    val status: ClassDebateStatus = ClassDebateStatus.WAITING_USER,
    val canReply: Boolean = false,
    val canRetry: Boolean = false,
    val myChoice: String = "",
    val opponentName: String = "",
    val opponentImageUrl: String? = null,
    val messages: List<ClassDebateMessageUiModel> = emptyList(),
    val replyMinLength: Int = 0,
    val replyMaxLength: Int = 0
)

fun ClassDebateBoard.toUiModel(): ClassDebateUiModel = ClassDebateUiModel(
    round = round,
    status = ClassDebateStatus.from(status),
    canReply = canReply,
    canRetry = canRetry,
    myChoice = participantOption.title,
    opponentName = philosopher.name,
    // TODO: API 연동 - 응답에 철학자 이미지 URL이 생기면 연결 (지금은 imageKey만 옴)
    opponentImageUrl = null,
    messages = messages.map { it.toUiModel() },
    replyMinLength = replyMinLength,
    replyMaxLength = replyMaxLength
)