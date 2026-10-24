package com.picke.presentation.ui.classroom.classjoin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinDialog
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinMutation
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinStep
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiAction
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiEvent
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiState
import com.picke.presentation.ui.classroom.model.ClassErrorCode
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
class ClassJoinViewModel @Inject constructor(
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClassJoinUiState())
    val uiState: StateFlow<ClassJoinUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassJoinUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassJoinUiEvent> = _uiEvent.receiveAsFlow()

    fun onAction(action: ClassJoinUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassJoinUiState, action: ClassJoinUiAction) {
        when (action) {
            ClassJoinUiAction.BackClick -> if (state.step == ClassJoinStep.CODE) {
                sendEvent(ClassJoinUiEvent.NavigateBack)
            }

            ClassJoinUiAction.NextClick -> if (state.isNextEnabled) {
                when (state.step) {
                    ClassJoinStep.CODE -> verifyCode(state.code)
                    ClassJoinStep.NAME -> enterClass(state.code, state.name)
                }
            }

            is ClassJoinUiAction.StateOnly -> Unit
        }
    }

    private fun sendEvent(event: ClassJoinUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun verifyCode(code: String) {
        viewModelScope.launch {
            classroomUseCases.previewClassJoinUseCase(code)
                .onSuccess { _uiState.update { reduce(it, ClassJoinMutation.CodeVerified) } }
                .onFailure { exception -> handleJoinFailure(exception) }
        }
    }

    private fun enterClass(code: String, name: String) {
        viewModelScope.launch {
            classroomUseCases.joinClassUseCase(code, name.trim())
                .onSuccess { classDetail ->
                    sendEvent(ClassJoinUiEvent.NavigateToClassDetail(classDetail.classId))
                }
                .onFailure { exception -> handleJoinFailure(exception) }
        }
    }

    private fun handleJoinFailure(exception: Throwable) {
        val code = (exception as? ApiErrorException)?.code
        if (code == ClassErrorCode.CLASS_409_NAME) {
            _uiState.update { reduce(it, ClassJoinMutation.NameDuplicated) }
            return
        }
        val dialog = when (code) {
            ClassErrorCode.CLASS_404_CODE, ClassErrorCode.CLASS_403_REMOVED -> ClassJoinDialog.INVALID_CODE
            ClassErrorCode.CLASS_409_FULL -> ClassJoinDialog.CLASS_FULL
            ClassErrorCode.CLASS_409_CLOSED, ClassErrorCode.CLASS_409_TICKET_ENDED -> ClassJoinDialog.CLASS_CLOSED
            ClassErrorCode.CLASS_409_DELETED -> ClassJoinDialog.CLASS_DELETED
            ClassErrorCode.CLASS_409_JOINED, ClassErrorCode.CLASS_409_OWNER -> ClassJoinDialog.ALREADY_JOINED
            else -> null
        }
        if (dialog != null) {
            _uiState.update { reduce(it, ClassJoinMutation.JoinFailed(dialog)) }
        } else {
            _uiState.update { reduce(it, ClassJoinMutation.RequestFailed) }
            sendEvent(ClassJoinUiEvent.ShowActionFailedToast)
        }
    }
}