package com.picke.presentation.ui.classroom.classresult.model

import com.picke.domain.feature.classroom.model.ClassResultMyReportBoard

data class ClassResultReportUiModel(
    val state: ClassReportState,
    val isRetryable: Boolean,
    val preVoteStance: String,
    val postVoteStance: String,
    val perspectiveCount: Int,
    val commentCount: Int,
    val judgmentBasis: String,
    val feedbackItems: List<ClassResultFeedbackItemUiModel>,
    val coverage: ClassFeedbackCoverage,
    val limited: ClassResultLimitedUiModel? = null
) {
    val isStanceChanged: Boolean
        get() = preVoteStance != postVoteStance

    val myOpinionCount: Int
        get() = perspectiveCount + commentCount

    internal val isRetryVisible: Boolean
        get() = state == ClassReportState.FAILED && isRetryable
}

fun ClassResultMyReportBoard.toUiModel() = ClassResultReportUiModel(
    state = ClassReportState.from(state),
    isRetryable = retryable == true,
    preVoteStance = preOption?.title ?: "",
    postVoteStance = postOption?.title ?: "",
    perspectiveCount = perspectiveCount,
    commentCount = commentCount,
    judgmentBasis = feedback?.judgmentBasis ?: "",
    feedbackItems = feedback?.toFeedbackItems() ?: emptyList(),
    coverage = ClassFeedbackCoverage.from(feedback?.coverage ?: ""),
    limited = feedback?.toLimitedUiModel()
)