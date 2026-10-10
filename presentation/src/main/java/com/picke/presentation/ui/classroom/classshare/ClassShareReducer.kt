package com.picke.presentation.ui.classroom.classshare

import com.picke.presentation.ui.classroom.classshare.model.ClassShareMutation
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiState

internal fun reduce(state: ClassShareUiState, mutation: ClassShareMutation): ClassShareUiState =
    when (mutation) {
        is ClassShareMutation.ClassShareLoaded -> state.copy(
            className = mutation.className,
            deadline = mutation.deadline,
            code = mutation.code,
            battle = mutation.battle
        )
    }