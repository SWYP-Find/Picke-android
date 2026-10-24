package com.picke.presentation.ui.classroom.model

sealed interface ClassUiEvent {

    data object NavigateBack : ClassUiEvent

    data object NavigateToJoin : ClassUiEvent

    data object NavigateToMyClass : ClassUiEvent

    data object NavigateToCreate : ClassUiEvent

    data object NavigateToTicket : ClassUiEvent

    data object NavigateToTicketRegister : ClassUiEvent

    data object ShowLoadFailedToast : ClassUiEvent
}