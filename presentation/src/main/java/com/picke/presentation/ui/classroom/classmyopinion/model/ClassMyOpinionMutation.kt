package com.picke.presentation.ui.classroom.classmyopinion.model

internal sealed interface ClassMyOpinionMutation {

    sealed interface RequestFinished : ClassMyOpinionMutation

    data object RequestFailed : RequestFinished

    data class MyPerspectivesLoaded(val perspectives: List<ClassMyPerspectiveUiModel>) : ClassMyOpinionMutation

    data class MyCommentsLoaded(
        val myDisplayName: String,
        val comments: List<ClassMyCommentUiModel>,
        val nextCursor: String?,
        val hasNext: Boolean
    ) : ClassMyOpinionMutation

    data class MoreMyCommentsLoaded(
        val comments: List<ClassMyCommentUiModel>,
        val nextCursor: String?,
        val hasNext: Boolean
    ) : RequestFinished
}