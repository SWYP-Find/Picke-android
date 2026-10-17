package com.picke.presentation.ui.my.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.ui.my.ticket.model.TicketMutation
import com.picke.presentation.ui.my.ticket.model.TicketUiAction
import com.picke.presentation.ui.my.ticket.model.TicketUiEvent
import com.picke.presentation.ui.my.ticket.model.TicketUiState
import com.picke.presentation.util.DummyData
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
class TicketViewModel @Inject constructor() : ViewModel() {

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
        // TODO: API 연동 - 등록된 이용권 목록 조회로 교체
        val tickets = DummyData.dummyRegisteredTickets
        if (tickets.isEmpty()) {
            sendEvent(TicketUiEvent.ReplaceWithRegister)
            return
        }
        _uiState.update { reduce(it, TicketMutation.TicketsLoaded(tickets)) }
    }
}