package com.picke.presentation.ui.classroom.classjoin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinMutation
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinStep
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiAction
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiEvent
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiState
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
class ClassJoinViewModel @Inject constructor() : ViewModel() {

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
                    ClassJoinStep.CODE -> verifyCode()
                    ClassJoinStep.NAME -> enterClass()
                }
            }

            is ClassJoinUiAction.StateOnly -> Unit
        }
    }

    private fun sendEvent(event: ClassJoinUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun verifyCode() {
        // TODO: API 연동 - 참여 코드 검증 요청 성공 시 반영, 실패 시 isCodeInvalid 표시
        _uiState.update { reduce(it, ClassJoinMutation.CodeVerified) }
    }

    private fun enterClass() {
        // TODO: API 연동 - 이름과 함께 클래스 참여 요청 후 이동, 이름 중복 시 isNameDuplicated 표시
        sendEvent(ClassJoinUiEvent.NavigateToMyClass)
    }
}