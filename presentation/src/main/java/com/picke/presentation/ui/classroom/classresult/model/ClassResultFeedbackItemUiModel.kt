package com.picke.presentation.ui.classroom.classresult.model

import androidx.annotation.StringRes
import com.picke.domain.feature.classroom.model.ClassResultFeedbackBoard
import com.picke.domain.feature.classroom.model.ClassResultFeedbackPointBoard
import com.picke.presentation.R

data class ClassResultFeedbackItemUiModel(
    @param:StringRes val titleRes: Int,
    val content: String
)

fun ClassResultFeedbackBoard.toFeedbackItems(): List<ClassResultFeedbackItemUiModel> =
    listOfNotNull(
        strength?.toFeedbackItem(R.string.class_result_strength),
        nextFocus?.toFeedbackItem(R.string.class_result_next_focus),
        nextQuestion?.let { ClassResultFeedbackItemUiModel(R.string.class_result_next_question, it) }
    )

private fun ClassResultFeedbackPointBoard.toFeedbackItem(@StringRes titleRes: Int) =
    ClassResultFeedbackItemUiModel(titleRes, body)