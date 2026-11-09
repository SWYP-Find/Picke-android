package com.picke.presentation.ui.classroom.classresult.model

import java.time.LocalDateTime

internal sealed interface ClassResultMutation {

    sealed interface RequestFinished : ClassResultMutation

    data object RequestFailed : RequestFinished

    data class ClassResultLoaded(
        val className: String,
        val isInProgress: Boolean,
        val tag: String,
        val battleTitle: String,
        val deadline: LocalDateTime?,
        val hasSummaryTab: Boolean,
        val hasReportTab: Boolean,
        val summary: ClassResultSummaryUiModel,
        val insight: ClassResultInsightUiModel?,
        val report: ClassResultReportUiModel?,
        val receivedLikeCount: Int?
    ) : ClassResultMutation

    data object ClassResultLoadFailed : ClassResultMutation

    data class InsightRetried(val insight: ClassResultInsightUiModel) : RequestFinished
}