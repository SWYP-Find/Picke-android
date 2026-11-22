package com.picke.presentation.ui.classroom.classdebate.model

import com.picke.domain.feature.classroom.model.ClassDebateMessageBoard

data class ClassDebateMessageUiModel(
    val messageId: Long,
    val speaker: ClassDebateSpeaker,
    val bubbles: List<String>
)

fun ClassDebateMessageBoard.toUiModel(): ClassDebateMessageUiModel = ClassDebateMessageUiModel(
    messageId = messageId,
    speaker = ClassDebateSpeaker.from(speaker),
    bubbles = bubbles.ifEmpty { listOf(content) }
)