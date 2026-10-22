package com.picke.presentation.ui.my.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.ui.classroom.model.ClassErrorCode
import com.picke.presentation.ui.classroom.model.ClassTicketStatus
import com.picke.presentation.ui.my.ticket.model.TicketMutation
import com.picke.presentation.ui.my.ticket.model.TicketUiAction
import com.picke.presentation.ui.my.ticket.model.TicketUiEvent
import com.picke.presentation.ui.my.ticket.model.TicketUiState
import com.picke.presentation.ui.my.ticket.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState: StateFlow<TicketUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<TicketUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<TicketUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchTickets()
    }

    fun onAction(action: TicketUiAction) {
        when (action) {
            TicketUiAction.BackClick -> sendEvent(TicketUiEvent.NavigateBack)

            TicketUiAction.AddRegisterClick -> sendEvent(TicketUiEvent.NavigateToRegister)
        }
    }

    private fun sendEvent(event: TicketUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchTickets() {
        viewModelScope.launch {
            classroomUseCases.getMyClassTicketUseCase()
                .onSuccess { ticket ->
                    if (ClassTicketStatus.from(ticket.status) == ClassTicketStatus.ACTIVE) {
                        _uiState.update {
                            reduce(it, TicketMutation.TicketsLoaded(listOf(ticket.toUiModel())))
                        }
                    } else {
                        sendEvent(TicketUiEvent.ReplaceWithRegister)
                    }
                }
                .onFailure { exception ->
                    if ((exception as? ApiErrorException)?.code == ClassErrorCode.TICKET_404) {
                        sendEvent(TicketUiEvent.ReplaceWithRegister)
                    } else {
                        _uiState.update { reduce(it, TicketMutation.TicketsLoadFailed) }
                        sendEvent(TicketUiEvent.ShowLoadFailedToast)
                    }
                }
        }
    }
}