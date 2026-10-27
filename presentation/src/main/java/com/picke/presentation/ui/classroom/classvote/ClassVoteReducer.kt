package com.picke.presentation.ui.classroom.classvote

import com.picke.presentation.ui.classroom.classvote.model.ClassVoteMutation
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiAction
import com.picke.presentation.ui.classroom.classvote.model.ClassVoteUiState

internal fun reduce(state: ClassVoteUiState, action: ClassVoteUiAction): ClassVoteUiState =
    when (action) {
        is ClassVoteUiAction.SideEffectOnly -> state

        is ClassVoteUiAction.VoteClick -> state.copy(isRequesting = true)
    }

internal fun reduce(state: ClassVoteUiState, mutation: ClassVoteMutation): ClassVoteUiState {
    val reduced = when (mutation) {
        ClassVoteMutation.RequestFailed -> state

        is ClassVoteMutation.ClassVoteLoaded -> state.copy(
            className = mutation.className,
            deadline = mutation.deadline,
            battleId = mutation.battleId,
            isAiBattle = mutation.isAiBattle,
            battle = mutation.battle
        )
    }

    return if (mutation is ClassVoteMutation.RequestFinished) reduced.copy(isRequesting = false) else reduced
}