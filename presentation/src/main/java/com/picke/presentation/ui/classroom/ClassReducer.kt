package com.picke.presentation.ui.classroom

import com.picke.presentation.ui.classroom.model.ClassCreateDialog
import com.picke.presentation.ui.classroom.model.ClassMutation
import com.picke.presentation.ui.classroom.model.ClassTicketStatus
import com.picke.presentation.ui.classroom.model.ClassUiAction
import com.picke.presentation.ui.classroom.model.ClassUiState

internal fun reduce(state: ClassUiState, action: ClassUiAction): ClassUiState =
    when (action) {
        is ClassUiAction.SideEffectOnly -> state

        ClassUiAction.CreateClick -> state.copy(
            createDialog = when (state.ticketStatus) {
                ClassTicketStatus.NONE -> ClassCreateDialog.NO_PERMISSION
                ClassTicketStatus.UNREGISTERED -> ClassCreateDialog.UNREGISTERED
                ClassTicketStatus.EXPIRED -> ClassCreateDialog.EXPIRED
                ClassTicketStatus.ACTIVE, null -> null
            }
        )

        ClassUiAction.CreateDialogRegisterClick -> state.copy(createDialog = null)

        ClassUiAction.CreateDialogDismiss -> state.copy(createDialog = null)
    }

internal fun reduce(state: ClassUiState, mutation: ClassMutation): ClassUiState =
    when (mutation) {
        is ClassMutation.TicketStatusLoaded -> state.copy(ticketStatus = mutation.ticketStatus)
    }