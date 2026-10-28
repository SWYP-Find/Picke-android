package com.picke.presentation.ui.classroom.classresult.model

import java.time.LocalDateTime

internal sealed interface ClassResultMutation {

    data class ClassResultLoaded(
        val className: String,
        val isInProgress: Boolean,
        val tag: String,
        val battleTitle: String,
        val deadline: LocalDateTime?,
        val hasSummaryTab: Boolean,
        val hasReportTab: Boolean,
        val summary: ClassResultSummaryUiModel?
    ) : ClassResultMutation

    data class InsightLoaded(
        val aiInsight: String,
        val bestComment: String,
        val mostDebatedComment: String
    ) : ClassResultMutation

    data class MyReportLoaded(val report: ClassResultReportUiModel) : ClassResultMutation
}