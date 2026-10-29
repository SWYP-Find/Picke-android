package com.picke.presentation.ui.classroom.classresult.model

import com.picke.domain.feature.classroom.model.ClassResultInsightBoard

data class ClassResultInsightUiModel(
    val state: ClassReportState,
    val insightText: String,
    val isRetryable: Boolean,
    val bestComment: String,
    val mostDebatedComment: String
) {
    internal val isRetryVisible: Boolean
        get() = state == ClassReportState.FAILED && isRetryable

    internal val isOpinionVisible: Boolean
        get() = bestComment.isNotBlank() && mostDebatedComment.isNotBlank()
}

fun ClassResultInsightBoard.toUiModel() = ClassResultInsightUiModel(
    state = ClassReportState.from(state),
    insightText = insightText,
    isRetryable = retryable,
    // TODO: API 연동 - 대표 의견(highlights) 항목 구조를 확인한 뒤 주요 의견 채우기
    bestComment = "",
    mostDebatedComment = ""
)