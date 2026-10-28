package com.picke.presentation.ui.classroom.classresult.model

data class ClassResultReportUiModel(
    val preVoteStance: String,
    val postVoteStance: String,
    val commentCount: Int,
    val replyCount: Int,
    val receivedLikeCount: Int,
    val judgmentBasis: String,
    val strength: String,
    val nextFocus: String,
    val nextQuestion: String
) {
    val isStanceChanged: Boolean
        get() = preVoteStance != postVoteStance

    val myOpinionCount: Int
        get() = commentCount + replyCount
}