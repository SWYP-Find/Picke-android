package com.picke.presentation.ui.classroom.classdebate

import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateMessageUiModel
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateMutation
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateSpeaker
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateStatus
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiAction
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiState

internal fun reduce(state: ClassDebateUiState, action: ClassDebateUiAction): ClassDebateUiState =
    when (action) {
        is ClassDebateUiAction.SideEffectOnly -> state

        is ClassDebateUiAction.InputChange -> state.copy(input = action.input)

        ClassDebateUiAction.SendClick -> if (state.isSendEnabled) {
            val message = ClassDebateMessageUiModel(
                messageId = state.nextMessageId,
                speaker = ClassDebateSpeaker.USER,
                text = state.input.trim()
            )
            state.copy(
                debate = state.debate.copy(messages = state.debate.messages + message),
                input = "",
                isReplying = true
            )
        } else {
            state
        }

        ClassDebateUiAction.FinalVoteDialogReplayClick -> state.copy(isFinalVoteDialogVisible = false)

        ClassDebateUiAction.FinalVoteDialogVoteClick -> state.copy(isFinalVoteDialogVisible = false)
    }

internal fun reduce(state: ClassDebateUiState, mutation: ClassDebateMutation): ClassDebateUiState =
    when (mutation) {
        is ClassDebateMutation.DebateLoaded -> state.copy(
            battleTitle = mutation.battleTitle,
            debate = mutation.debate
        )

        is ClassDebateMutation.OpponentReplied -> {
            val nextRound = state.debate.round + 1
            val isFinished = nextRound > ClassDebateUiState.TOTAL_ROUNDS
            state.copy(
                debate = state.debate.copy(
                    round = nextRound,
                    status = if (isFinished) ClassDebateStatus.COMPLETED else state.debate.status,
                    messages = state.debate.messages + mutation.messages
                ),
                isReplying = false,
                isFinalVoteDialogVisible = isFinished
            )
        }
    }