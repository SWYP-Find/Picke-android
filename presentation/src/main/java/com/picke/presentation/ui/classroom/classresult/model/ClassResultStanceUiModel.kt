package com.picke.presentation.ui.classroom.classresult.model

import com.picke.domain.feature.classroom.model.ClassResultOptionBoard

data class ClassResultStanceUiModel(
    val label: String,
    val count: Int
)

fun ClassResultOptionBoard.toUiModel() = ClassResultStanceUiModel(
    label = title,
    count = postVoteCount
)