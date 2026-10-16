package com.picke.presentation.ui.classroom.classresult

import com.picke.presentation.ui.classroom.classresult.model.ClassResultMutation
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiAction
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiState

internal fun reduce(state: ClassResultUiState, action: ClassResultUiAction): ClassResultUiState =
    when (action) {
        is ClassResultUiAction.SideEffectOnly -> state

        is ClassResultUiAction.TabSelect -> state.copy(selectedTab = action.tab)
    }

internal fun reduce(state: ClassResultUiState, mutation: ClassResultMutation): ClassResultUiState =
    when (mutation) {
        is ClassResultMutation.ClassResultLoaded -> state.copy(
            className = mutation.className,
            isInProgress = mutation.isInProgress,
            tag = mutation.tag,
            battleTitle = mutation.battleTitle,
            deadline = mutation.deadline,
            summary = mutation.summary,
            report = mutation.report
        )
    }