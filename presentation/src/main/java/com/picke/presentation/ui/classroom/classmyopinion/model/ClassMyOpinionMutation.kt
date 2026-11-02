package com.picke.presentation.ui.classroom.classmyopinion.model

internal sealed interface ClassMyOpinionMutation {

    sealed interface RequestFinished : ClassMyOpinionMutation

    data object RequestFailed : RequestFinished

    data class MyCommentsLoaded(val comments: List<ClassMyCommentUiModel>) : ClassMyOpinionMutation

    data class RepliesLoaded(
        val myDisplayName: String,
        val replies: List<ClassMyReplyUiModel>,
        val nextCursor: String?,
        val hasNext: Boolean
    ) : ClassMyOpinionMutation

    data class MoreRepliesLoaded(
        val replies: List<ClassMyReplyUiModel>,
        val nextCursor: String?,
        val hasNext: Boolean
    ) : RequestFinished
}