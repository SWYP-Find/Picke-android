package com.picke.presentation.ui.classroom.classresult.model

import java.time.LocalDateTime

data class ClassResultUiState(
    val classId: Long = 0L,
    val className: String = "",
    val isInProgress: Boolean = false,
    val tag: String = "",
    val battleTitle: String = "",
    val deadline: LocalDateTime? = null,
    val summary: ClassResultSummaryUiModel? = null,
    val report: ClassResultReportUiModel? = null,
    val selectedTab: ClassResultTab = ClassResultTab.SUMMARY
) {
    internal val isTabVisible: Boolean
        get() = summary != null && report != null

    internal val visibleTab: ClassResultTab
        get() = when {
            summary == null -> ClassResultTab.REPORT
            report == null -> ClassResultTab.SUMMARY
            else -> selectedTab
        }
}