package com.picke.presentation.ui.classroom.classmyopinion

import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionMutation
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiState

internal fun reduce(state: ClassMyOpinionUiState, mutation: ClassMyOpinionMutation): ClassMyOpinionUiState =
    when (mutation) {
        is ClassMyOpinionMutation.ClassMyOpinionLoaded -> state.copy(
            comments = mutation.comments,
            replies = mutation.replies
        )
    }