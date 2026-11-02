package com.picke.presentation.ui.classroom.classmyopinion.model

data class ClassMyOpinionUiState(
    val classId: Long = 0L,
    val perspectives: List<ClassMyPerspectiveUiModel> = emptyList(),
    val comments: List<ClassMyCommentUiModel> = emptyList(),
    val myDisplayName: String = "",
    val nextCursor: String? = null,
    val hasNext: Boolean = false,
    val isRequesting: Boolean = false
) {
    internal val canLoadMoreComments: Boolean
        get() = hasNext && !isRequesting
}