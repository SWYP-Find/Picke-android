package com.picke.presentation.ui.classroom.classdebate

import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateMutation
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateStatus
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiAction
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiState

internal fun reduce(state: ClassDebateUiState, action: ClassDebateUiAction): ClassDebateUiState =
    when (action) {
        is ClassDebateUiAction.SideEffectOnly -> state

        ClassDebateUiAction.RetryClick -> if (state.isRetryVisible) state.copy(isRequesting = true) else state

        is ClassDebateUiAction.InputChange -> if (state.isReplyLengthWithinMax(action.input)) {
            state.copy(
                input = action.input,
                replyRequestKey = if (action.input == state.input) state.replyRequestKey else null
            )
        } else {
            state
        }

        ClassDebateUiAction.SendClick -> if (state.isSendEnabled && state.isReplyLengthEnough) {
            state.copy(isRequesting = true)
        } else {
            state
        }

        ClassDebateUiAction.FinalVoteDialogReplayClick -> state.copy(isFinalVoteDialogVisible = false)

        ClassDebateUiAction.FinalVoteDialogVoteClick -> state.copy(isFinalVoteDialogVisible = false)
    }

internal fun reduce(state: ClassDebateUiState, mutation: ClassDebateMutation): ClassDebateUiState {
    val reduced = when (mutation) {
        ClassDebateMutation.RequestFailed -> state

        is ClassDebateMutation.DebateLoaded -> state.copy(
            battleTitle = mutation.battleTitle,
            debate = mutation.debate
        )

        is ClassDebateMutation.DebateRetried -> state.copy(debate = mutation.debate)

        is ClassDebateMutation.ReplySubmitted -> state.copy(
            debate = mutation.debate,
            input = "",
            replyRequestKey = null,
            isFinalVoteDialogVisible = mutation.debate.status == ClassDebateStatus.COMPLETED
        )

        is ClassDebateMutation.ReplyFailed -> state.copy(replyRequestKey = mutation.requestKey)
    }

    return if (mutation is ClassDebateMutation.RequestFinished) reduced.copy(isRequesting = false) else reduced
}