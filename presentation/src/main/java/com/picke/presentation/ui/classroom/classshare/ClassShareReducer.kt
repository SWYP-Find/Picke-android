package com.picke.presentation.ui.classroom.classshare

import com.picke.presentation.ui.classroom.classshare.model.ClassShareMutation
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiState

internal fun reduce(state: ClassShareUiState, mutation: ClassShareMutation): ClassShareUiState =
    when (mutation) {
        is ClassShareMutation.ClassShareLoaded -> state.copy(
            isLoading = false,
            className = mutation.className,
            deadline = mutation.deadline,
            code = mutation.code
        )

        ClassShareMutation.ClassShareLoadFailed -> state.copy(isLoading = false)

        is ClassShareMutation.BattleLoaded -> state.copy(battle = mutation.battle)
    }