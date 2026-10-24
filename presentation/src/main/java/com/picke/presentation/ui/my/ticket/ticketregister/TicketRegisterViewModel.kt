package com.picke.presentation.ui.my.ticket.ticketregister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.ui.classroom.model.ClassErrorCode
import com.picke.presentation.ui.my.ticket.model.toUiModel
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterDialog
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterMutation
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterStep
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiAction
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiEvent
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiState
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
class TicketRegisterViewModel @Inject constructor(
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

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
                verifyCode(state.code)
            }

            TicketRegisterUiAction.RegisterClick -> if (state.isRegisterEnabled) {
                registerTicket(state.code)
            }

            TicketRegisterUiAction.DialogBackClick -> sendEvent(TicketRegisterUiEvent.NavigateBack)
        }
    }

    private fun sendEvent(event: TicketRegisterUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun verifyCode(code: String) {
        viewModelScope.launch {
            classroomUseCases.previewClassTicketUseCase(code)
                .onSuccess { preview ->
                    if (preview.registeredByMe) {
                        sendEvent(TicketRegisterUiEvent.NavigateToTicket)
                    } else {
                        _uiState.update {
                            reduce(it, TicketRegisterMutation.CodeVerified(preview.toUiModel()))
                        }
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        reduce(
                            it,
                            TicketRegisterMutation.RequestFailed(exception.toRegisterDialog())
                        )
                    }
                }
        }
    }

    private fun registerTicket(code: String) {
        viewModelScope.launch {
            classroomUseCases.registerClassTicketUseCase(code)
                .onSuccess { ticket ->
                    sendEvent(TicketRegisterUiEvent.NavigateToRegisterComplete(ticket.ticketId))
                }
                .onFailure { exception ->
                    _uiState.update {
                        reduce(
                            it,
                            TicketRegisterMutation.RequestFailed(exception.toRegisterDialog())
                        )
                    }
                }
        }
    }

    private fun Throwable.toRegisterDialog(): TicketRegisterDialog =
        when ((this as? ApiErrorException)?.code) {
            ClassErrorCode.TICKET_409_CLAIMED -> TicketRegisterDialog.ALREADY_REGISTERED
            ClassErrorCode.TICKET_404_CODE, ClassErrorCode.TICKET_409_REVOKED -> TicketRegisterDialog.INVALID_CODE
            ClassErrorCode.TICKET_409_REG_EXP -> TicketRegisterDialog.EXPIRED
            else -> TicketRegisterDialog.REGISTER_FAILED
        }
}