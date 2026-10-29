package com.picke.presentation.ui.classroom.classresult

import com.picke.presentation.ui.classroom.classresult.model.ClassResultMutation
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiAction
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiState

internal fun reduce(state: ClassResultUiState, action: ClassResultUiAction): ClassResultUiState =
    when (action) {
        is ClassResultUiAction.SideEffectOnly -> state

        is ClassResultUiAction.TabSelect -> state.copy(selectedTab = action.tab)

        ClassResultUiAction.InsightRetryClick ->
            if (state.isInsightRetryEnabled) state.copy(isRequesting = true) else state
    }

internal fun reduce(state: ClassResultUiState, mutation: ClassResultMutation): ClassResultUiState {
    val reduced = when (mutation) {
        ClassResultMutation.RequestFailed -> state

        is ClassResultMutation.ClassResultLoaded -> state.copy(
            className = mutation.className,
            isInProgress = mutation.isInProgress,
            tag = mutation.tag,
            battleTitle = mutation.battleTitle,
            deadline = mutation.deadline,
            hasSummaryTab = mutation.hasSummaryTab,
            hasReportTab = mutation.hasReportTab,
            summary = mutation.summary
        )

        is ClassResultMutation.InsightLoaded -> state.copy(insight = mutation.insight)

        is ClassResultMutation.MyReportLoaded -> state.copy(report = mutation.report)

        is ClassResultMutation.InsightRetried -> state.copy(insight = mutation.insight)
    }
    return if (mutation is ClassResultMutation.RequestFinished) reduced.copy(isRequesting = false) else reduced
}