package com.picke.presentation.ui.classroom

import com.picke.presentation.ui.classroom.model.ClassMutation
import com.picke.presentation.ui.classroom.model.ClassTicketDialog
import com.picke.presentation.ui.classroom.model.ClassTicketStatus
import com.picke.presentation.ui.classroom.model.ClassUiAction
import com.picke.presentation.ui.classroom.model.ClassUiState

internal fun reduce(state: ClassUiState, action: ClassUiAction): ClassUiState =
    when (action) {
        is ClassUiAction.SideEffectOnly -> state

        ClassUiAction.TicketDialogRegisterClick -> state.copy(ticketDialog = null)

        ClassUiAction.TicketDialogDismiss -> state.copy(ticketDialog = null)
    }

internal fun reduce(state: ClassUiState, mutation: ClassMutation): ClassUiState =
    when (mutation) {
        is ClassMutation.TicketStatusLoaded -> state.copy(
            ticketStatus = mutation.ticketStatus,
            ticketDialog = when (mutation.ticketStatus) {
                ClassTicketStatus.NONE -> ClassTicketDialog.NO_PERMISSION
                ClassTicketStatus.UNREGISTERED -> ClassTicketDialog.UNREGISTERED
                ClassTicketStatus.EXPIRED -> ClassTicketDialog.EXPIRED
                ClassTicketStatus.ACTIVE -> null
            }
        )
    }