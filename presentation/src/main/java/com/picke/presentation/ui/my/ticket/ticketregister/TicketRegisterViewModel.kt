package com.picke.presentation.ui.my.ticket.ticketregister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterMutation
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterStep
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiAction
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiEvent
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiState
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
class TicketRegisterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TicketRegisterUiState())
    val uiState: StateFlow<TicketRegisterUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<TicketRegisterUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<TicketRegisterUiEvent> = _uiEvent.receiveAsFlow()

    fun onAction(action: TicketRegisterUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: TicketRegisterUiState, action: TicketRegisterUiAction) {
        when (action) {
            is TicketRegisterUiAction.StateOnly -> Unit

            TicketRegisterUiAction.BackClick -> if (state.step == TicketRegisterStep.CODE) {
                sendEvent(TicketRegisterUiEvent.NavigateBack)
            }

            TicketRegisterUiAction.CodeCheckClick -> if (state.isCodeCheckEnabled) {
                verifyCode()
            }

            TicketRegisterUiAction.RegisterClick -> state.ticket?.let { ticket ->
                registerTicket(ticket.ticketId)
            }

            TicketRegisterUiAction.DialogBackClick -> sendEvent(TicketRegisterUiEvent.NavigateBack)
        }
    }

    private fun sendEvent(event: TicketRegisterUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun verifyCode() {
        // TODO: API 연동 - 이용권 코드 조회로 교체, 실패 시 사유에 맞는 dialog 표시 (이미 등록됨·코드 틀림·기간 만료)
        _uiState.update { reduce(it, TicketRegisterMutation.CodeVerified(DummyData.dummyTicket)) }
    }

    private fun registerTicket(ticketId: Long) {
        // TODO: API 연동 - 이용권 등록 요청 후 성공 시 이동, 실패 시 REGISTER_FAILED dialog 표시
        sendEvent(TicketRegisterUiEvent.NavigateToRegisterComplete(ticketId))
    }
}