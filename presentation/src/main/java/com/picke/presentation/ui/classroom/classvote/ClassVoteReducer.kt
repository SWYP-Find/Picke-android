package com.picke.presentation.ui.classroom.classvote

import com.picke.presentation.ui.classroom.classvote.model.ClassVoteMutation
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiState

internal fun reduce(state: ClassVoteUiState, mutation: ClassVoteMutation): ClassVoteUiState =
    when (mutation) {
        is ClassVoteMutation.ClassVoteLoaded -> state.copy(
            className = mutation.className,
            deadline = mutation.deadline,
            battleId = mutation.battleId,
            isAiBattle = mutation.isAiBattle,
            commentCount = mutation.commentCount,
            battle = mutation.battle
        )
    }