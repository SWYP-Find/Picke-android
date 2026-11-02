package com.picke.presentation.ui.classroom.classmyopinion.model

data class ClassMyOpinionUiState(
    val classId: Long = 0L,
    val comments: List<ClassMyCommentUiModel> = emptyList(),
    val replies: List<ClassMyReplyUiModel> = emptyList(),
    val myDisplayName: String = "",
    val nextCursor: String? = null,
    val hasNext: Boolean = false,
    val isRequesting: Boolean = false
) {
    internal val canLoadMoreReplies: Boolean
        get() = hasNext && !isRequesting
}