package com.picke.presentation.ui.classroom.classresult.model

import com.picke.domain.feature.classroom.model.ClassResultStatsBoard

data class ClassResultSummaryUiModel(
    val aiInsight: String,
    val memberCount: Int,
    val participatedCount: Int,
    val commentedCount: Int,
    val stances: List<ClassResultStanceUiModel>,
    val bestComment: String,
    val mostDebatedComment: String
)

fun ClassResultStatsBoard.toUiModel() = ClassResultSummaryUiModel(
    aiInsight = "",
    memberCount = activeMemberCount,
    participatedCount = completedCount,
    commentedCount = commentWriterCount,
    stances = options.map { it.toUiModel() },
    bestComment = "",
    mostDebatedComment = ""
)