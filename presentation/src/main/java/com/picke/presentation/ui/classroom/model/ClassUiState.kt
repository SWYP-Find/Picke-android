package com.picke.presentation.ui.classroom.model

data class ClassUiState(
    val ticketStatus: ClassTicketStatus? = null,
    val ticketDialog: ClassTicketDialog? = null,
    val isRequesting: Boolean = false
)