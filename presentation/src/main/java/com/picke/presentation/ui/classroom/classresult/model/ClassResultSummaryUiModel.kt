package com.picke.presentation.ui.classroom.classresult.model

data class ClassResultSummaryUiModel(
    val aiInsight: String,
    val memberCount: Int,
    val participatedCount: Int,
    val commentedCount: Int,
    val stances: List<ClassResultStanceUiModel>,
    val bestComment: String,
    val mostDebatedComment: String
)