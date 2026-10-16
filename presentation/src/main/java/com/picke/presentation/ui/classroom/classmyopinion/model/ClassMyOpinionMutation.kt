package com.picke.presentation.ui.classroom.classmyopinion.model

internal sealed interface ClassMyOpinionMutation {
    data class ClassMyOpinionLoaded(
        val comments: List<ClassMyCommentUiModel>,
        val replies: List<ClassMyReplyUiModel>
    ) : ClassMyOpinionMutation
}