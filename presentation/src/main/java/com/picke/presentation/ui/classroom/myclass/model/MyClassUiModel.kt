package com.picke.presentation.ui.classroom.myclass.model

data class MyClassUiModel(
    val id: Long,
    val title: String,
    val topic: String,
    val isInProgress: Boolean,
    val memberCount: Int,
    val deadlineText: String,
    val isOperator: Boolean = false
)