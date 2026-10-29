package com.picke.presentation.ui.classroom.classresult.model

import com.picke.domain.feature.classroom.model.ClassResultMyReportBoard

data class ClassResultReportUiModel(
    val state: ClassReportState,
    val preVoteStance: String,
    val postVoteStance: String,
    val perspectiveCount: Int,
    val commentCount: Int,
    val receivedLikeCount: Int?,
    val judgmentBasis: String,
    val feedbackItems: List<ClassResultFeedbackItemUiModel>,
    val coverage: ClassFeedbackCoverage
) {
    val isStanceChanged: Boolean
        get() = preVoteStance != postVoteStance

    val myOpinionCount: Int
        get() = perspectiveCount + commentCount
}

fun ClassResultMyReportBoard.toUiModel() = ClassResultReportUiModel(
    state = ClassReportState.from(state),
    preVoteStance = preOption?.title ?: "",
    postVoteStance = postOption?.title ?: "",
    perspectiveCount = perspectiveCount,
    commentCount = commentCount,
    // TODO: API 연동 - 내 AI 피드백에 받은 추천 수 필드가 생기면 연결
    receivedLikeCount = null,
    judgmentBasis = feedback?.judgmentBasis ?: "",
    feedbackItems = feedback?.toFeedbackItems() ?: emptyList(),
    coverage = ClassFeedbackCoverage.from(feedback?.coverage ?: "")
)