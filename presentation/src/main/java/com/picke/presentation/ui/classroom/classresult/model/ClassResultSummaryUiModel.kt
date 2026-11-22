package com.picke.presentation.ui.classroom.classresult.model

import com.picke.domain.feature.classroom.model.ClassResultStatsBoard

data class ClassResultSummaryUiModel(
    val memberCount: Int,
    val participatedCount: Int,
    val commentedCount: Int,
    val stances: List<ClassResultStanceUiModel>
)

fun ClassResultStatsBoard.toUiModel() = ClassResultSummaryUiModel(
    memberCount = activeMemberCount,
    participatedCount = completedCount,
    commentedCount = commentWriterCount,
    stances = options.map { it.toUiModel() }
)