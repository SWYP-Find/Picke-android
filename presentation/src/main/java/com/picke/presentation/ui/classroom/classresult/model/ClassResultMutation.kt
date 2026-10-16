package com.picke.presentation.ui.classroom.classresult.model

import java.time.LocalDateTime

internal sealed interface ClassResultMutation {
    data class ClassResultLoaded(
        val className: String,
        val isInProgress: Boolean,
        val tag: String,
        val battleTitle: String,
        val deadline: LocalDateTime?,
        val summary: ClassResultSummaryUiModel?,
        val report: ClassResultReportUiModel?
    ) : ClassResultMutation
}