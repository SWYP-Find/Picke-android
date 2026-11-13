package com.picke.presentation.ui.classroom.classresult.model

import com.picke.domain.feature.classroom.model.ClassResultFeedbackBoard

data class ClassResultLimitedUiModel(
    val headline: String,
    val message: String,
    val notice: String
)

fun ClassResultFeedbackBoard.toLimitedUiModel(): ClassResultLimitedUiModel? {
    val message = limitedMessage
    val notice = limitedNotice

    if (limitedReason == null || message == null || notice == null) return null

    return ClassResultLimitedUiModel(
        headline = headline,
        message = message,
        notice = notice
    )
}