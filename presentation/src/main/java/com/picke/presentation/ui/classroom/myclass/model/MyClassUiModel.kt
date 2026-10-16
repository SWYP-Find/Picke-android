package com.picke.presentation.ui.classroom.myclass.model

import java.time.LocalDateTime

data class MyClassUiModel(
    val id: Long,
    val title: String,
    val topic: String,
    val isInProgress: Boolean,
    val memberCount: Int,
    val deadline: LocalDateTime?,
    val isOperator: Boolean = false
)