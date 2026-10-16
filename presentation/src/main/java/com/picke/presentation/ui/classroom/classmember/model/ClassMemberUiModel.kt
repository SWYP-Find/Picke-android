package com.picke.presentation.ui.classroom.classmember.model

data class ClassMemberUiModel(
    val memberId: Long,
    val name: String,
    val imageUrl: String?,
    val isOperator: Boolean,
    val isMe: Boolean
)