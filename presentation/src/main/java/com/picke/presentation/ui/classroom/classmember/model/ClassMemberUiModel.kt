package com.picke.presentation.ui.classroom.classmember.model

import com.picke.domain.feature.classroom.model.ClassMemberBoard
import com.picke.presentation.ui.classroom.model.ClassRole

data class ClassMemberUiModel(
    val memberId: Long,
    val name: String,
    val imageUrl: String?,
    val isOperator: Boolean,
    val isMe: Boolean
)

fun ClassMemberBoard.toUiModel() = ClassMemberUiModel(
    memberId = memberId,
    name = displayName,
    imageUrl = characterUrl.ifBlank { null },
    isOperator = ClassRole.from(role).isOwner,
    isMe = me
)