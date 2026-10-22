package com.picke.presentation.ui.classroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.ui.classroom.model.ClassErrorCode
import com.picke.presentation.ui.classroom.model.ClassMutation
import com.picke.presentation.ui.classroom.model.ClassTicketStatus
import com.picke.presentation.ui.classroom.model.ClassUiAction
import com.picke.presentation.ui.classroom.model.ClassUiEvent
import com.picke.presentation.ui.classroom.model.ClassUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
class ClassViewModel @Inject constructor(
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClassUiState())
    val uiState: StateFlow<ClassUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassUiEvent> = _uiEvent.receiveAsFlow()

    private var ticketCheckJob: Job? = null

    fun onAction(action: ClassUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassUiState, action: ClassUiAction) {
        when (action) {
            is ClassUiAction.StateOnly -> Unit

            ClassUiAction.BackClick -> sendEvent(ClassUiEvent.NavigateBack)

            ClassUiAction.JoinClick -> sendEvent(ClassUiEvent.NavigateToJoin)

            ClassUiAction.MyClassClick -> sendEvent(ClassUiEvent.NavigateToMyClass)

            ClassUiAction.CreateClick -> checkTicketStatus()

            ClassUiAction.TicketClick -> sendEvent(ClassUiEvent.NavigateToTicket)

            ClassUiAction.TicketDialogRegisterClick -> sendEvent(ClassUiEvent.NavigateToTicketRegister)
        }
    }

    private fun sendEvent(event: ClassUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun checkTicketStatus() {
        if (ticketCheckJob?.isActive == true) return
        ticketCheckJob = viewModelScope.launch {
            classroomUseCases.getMyClassTicketUseCase()
                .onSuccess { ticket ->
                    applyTicketStatus(ClassTicketStatus.from(ticket.status))
                }
                .onFailure { exception ->
                    if ((exception as? ApiErrorException)?.code == ClassErrorCode.TICKET_404) {
                        applyTicketStatus(ClassTicketStatus.NONE)
                    } else {
                        sendEvent(ClassUiEvent.ShowLoadFailedToast)
                    }
                }
        }
    }

    private fun applyTicketStatus(ticketStatus: ClassTicketStatus) {
        _uiState.update { reduce(it, ClassMutation.TicketStatusLoaded(ticketStatus)) }
        if (ticketStatus == ClassTicketStatus.ACTIVE) sendEvent(ClassUiEvent.NavigateToCreate)
    }
}