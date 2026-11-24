package com.picke.presentation.ui.classroom.classresult.model

import com.picke.domain.feature.classroom.model.ClassResultInsightBoard

data class ClassResultInsightUiModel(
    val state: ClassReportState,
    val insightText: String?,
    val isRetryable: Boolean,
    val bestComment: String?,
    val mostDebatedComment: String?
) {
    internal val isRetryVisible: Boolean
        get() = state == ClassReportState.FAILED && isRetryable
}

fun ClassResultInsightBoard.toUiModel() = ClassResultInsightUiModel(
    state = ClassReportState.from(state),
    insightText = insightText,
    isRetryable = retryable,
    bestComment = bestComment,
    mostDebatedComment = mostDebatedComment
)