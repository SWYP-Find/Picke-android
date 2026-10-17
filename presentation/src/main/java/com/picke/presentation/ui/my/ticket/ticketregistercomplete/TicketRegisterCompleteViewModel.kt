package com.picke.presentation.ui.my.ticket.ticketregistercomplete

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.my.ticket.ticketregistercomplete.model.TicketRegisterCompleteMutation
import com.picke.presentation.ui.my.ticket.ticketregistercomplete.model.TicketRegisterCompleteUiAction
import com.picke.presentation.ui.my.ticket.ticketregistercomplete.model.TicketRegisterCompleteUiEvent
import com.picke.presentation.ui.my.ticket.ticketregistercomplete.model.TicketRegisterCompleteUiState
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
class TicketRegisterCompleteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TicketRegisterCompleteUiState(
            ticketId = savedStateHandle.get<Long>(AppRoute.ARG_TICKET_ID) ?: 0L
        )
    )
    val uiState: StateFlow<TicketRegisterCompleteUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<TicketRegisterCompleteUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<TicketRegisterCompleteUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchTicket()
    }

    fun onAction(action: TicketRegisterCompleteUiAction) {
        when (action) {
            TicketRegisterCompleteUiAction.BackClick -> sendEvent(TicketRegisterCompleteUiEvent.NavigateBack)

            TicketRegisterCompleteUiAction.TicketCheckClick ->
                sendEvent(TicketRegisterCompleteUiEvent.NavigateToTicket)
        }
    }

    private fun sendEvent(event: TicketRegisterCompleteUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchTicket() {
        // TODO: API 연동 - 등록한 이용권 조회로 교체
        val ticket =
            DummyData.dummyRegisteredTicket.takeIf { it.ticketId == _uiState.value.ticketId }
                ?: return
        _uiState.update { reduce(it, TicketRegisterCompleteMutation.TicketLoaded(ticket)) }
    }
}