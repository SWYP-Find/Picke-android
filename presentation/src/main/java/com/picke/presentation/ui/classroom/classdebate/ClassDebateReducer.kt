package com.picke.presentation.ui.classroom.classdebate

import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateMessageUiModel
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateMutation
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateSpeaker
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiAction
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiState

internal fun reduce(state: ClassDebateUiState, action: ClassDebateUiAction): ClassDebateUiState =
    when (action) {
        is ClassDebateUiAction.SideEffectOnly -> state

        is ClassDebateUiAction.QuickReplyClick -> state.copy(input = action.reply)

        is ClassDebateUiAction.InputChange -> state.copy(input = action.input)

        ClassDebateUiAction.SendClick -> if (state.isSendEnabled) {
            val message = ClassDebateMessageUiModel(
                messageId = state.nextMessageId,
                speaker = ClassDebateSpeaker.ME,
                text = state.input.trim()
            )
            state.copy(messages = state.messages + message, input = "", isReplying = true)
        } else {
            state
        }
    }

internal fun reduce(state: ClassDebateUiState, mutation: ClassDebateMutation): ClassDebateUiState =
    when (mutation) {
        is ClassDebateMutation.DebateLoaded -> state.copy(
            battleId = mutation.battleId,
            battleTitle = mutation.battleTitle,
            myChoice = mutation.myChoice,
            opponentName = mutation.opponentName,
            opponentImageUrl = mutation.opponentImageUrl,
            messages = mutation.messages,
            quickReplies = mutation.quickReplies
        )

        is ClassDebateMutation.OpponentReplied -> state.copy(
            currentRound = state.currentRound + 1,
            messages = state.messages + mutation.messages,
            isReplying = false
        )
    }