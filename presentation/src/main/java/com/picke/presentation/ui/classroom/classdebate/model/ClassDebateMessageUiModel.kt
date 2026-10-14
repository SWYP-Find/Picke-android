package com.picke.presentation.ui.classroom.classdebate.model

data class ClassDebateMessageUiModel(
    val messageId: Long,
    val speaker: ClassDebateSpeaker,
    val text: String
)
