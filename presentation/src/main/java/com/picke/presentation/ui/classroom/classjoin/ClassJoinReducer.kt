package com.picke.presentation.ui.classroom.classjoin

import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinDialog
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinMutation
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinStep
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiAction
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiState

internal fun reduce(state: ClassJoinUiState, action: ClassJoinUiAction): ClassJoinUiState =
    when (action) {
        is ClassJoinUiAction.SideEffectOnly -> state

        ClassJoinUiAction.BackClick -> if (state.step == ClassJoinStep.NAME) state.copy(step = ClassJoinStep.CODE) else state

        is ClassJoinUiAction.CodeChange -> state.copy(
            code = action.code.toClassCode(),
            isCodeInvalid = false
        )

        is ClassJoinUiAction.NameChange -> state.copy(
            name = action.name.take(ClassJoinUiState.NAME_MAX_LENGTH),
            isNameDuplicated = false
        )

        ClassJoinUiAction.DialogDismiss -> state.copy(dialog = null)
    }

internal fun reduce(state: ClassJoinUiState, mutation: ClassJoinMutation): ClassJoinUiState =
    when (mutation) {
        ClassJoinMutation.CodeVerified -> state.copy(step = ClassJoinStep.NAME)

        is ClassJoinMutation.JoinFailed -> state.copy(
            isCodeInvalid = mutation.dialog == ClassJoinDialog.INVALID_CODE,
            dialog = mutation.dialog
        )
    }

private fun String.toClassCode(): String =
    uppercase()
        .filter { it in 'A'..'Z' || it in '0'..'9' }
        .take(ClassJoinUiState.CODE_LENGTH)