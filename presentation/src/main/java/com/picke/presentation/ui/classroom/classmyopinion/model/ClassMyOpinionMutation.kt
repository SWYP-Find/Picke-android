package com.picke.presentation.ui.classroom.classmyopinion.model

internal sealed interface ClassMyOpinionMutation {

    sealed interface RequestFinished : ClassMyOpinionMutation

    data object RequestFailed : RequestFinished

    data class MyOpinionsLoaded(
        val perspectives: List<ClassMyPerspectiveUiModel>,
        val myDisplayName: String,
        val comments: List<ClassMyCommentUiModel>,
        val nextCursor: String?,
        val hasNext: Boolean
    ) : ClassMyOpinionMutation

    data object MyOpinionsLoadFailed : ClassMyOpinionMutation

    data class MoreMyCommentsLoaded(
        val comments: List<ClassMyCommentUiModel>,
        val nextCursor: String?,
        val hasNext: Boolean
    ) : RequestFinished
}