package com.picke.presentation.ui.classroom.classresult.model

import java.time.LocalDateTime

data class ClassResultUiState(
    val classId: Long = 0L,
    val className: String = "",
    val isInProgress: Boolean = false,
    val tag: String = "",
    val battleTitle: String = "",
    val deadline: LocalDateTime? = null,
    val hasSummaryTab: Boolean = false,
    val hasReportTab: Boolean = false,
    val summary: ClassResultSummaryUiModel? = null,
    val report: ClassResultReportUiModel? = null,
    val selectedTab: ClassResultTab = ClassResultTab.SUMMARY
) {
    internal val isTabVisible: Boolean
        get() = hasSummaryTab && hasReportTab

    internal val visibleTab: ClassResultTab
        get() = when {
            !hasSummaryTab -> ClassResultTab.REPORT
            !hasReportTab -> ClassResultTab.SUMMARY
            else -> selectedTab
        }
}