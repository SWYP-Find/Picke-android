package com.picke.presentation.ui.classroom.classedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classedit.model.ClassEditMutation
import com.picke.presentation.ui.classroom.classedit.model.ClassEditUiAction
import com.picke.presentation.ui.classroom.classedit.model.ClassEditUiEvent
import com.picke.presentation.ui.classroom.classedit.model.ClassEditUiState
import com.picke.presentation.ui.classroom.model.ClassBattleType
import com.picke.presentation.ui.classroom.model.ClassErrorCode
import com.picke.presentation.ui.classroom.model.toUiModel
import com.picke.presentation.util.formatServerDateTime
import com.picke.presentation.util.parseServerDateTime
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
class ClassEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassEditUiState(classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L)
    )
    val uiState: StateFlow<ClassEditUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassEditUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassEditUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchClass()
    }

    fun onAction(action: ClassEditUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassEditUiState, action: ClassEditUiAction) {
        when (action) {
            is ClassEditUiAction.StateOnly -> Unit

            ClassEditUiAction.BackClick -> sendEvent(ClassEditUiEvent.NavigateBack)

            ClassEditUiAction.SaveClick -> if (state.isSaveEnabled) updateClass()
        }
    }

    private fun sendEvent(event: ClassEditUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchClass() {
        val classId = _uiState.value.classId
        viewModelScope.launch {
            val classDetail = classroomUseCases.getClassDetailUseCase(classId).getOrElse {
                sendEvent(ClassEditUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val mutation = ClassEditMutation.ClassLoaded(
                className = classDetail.name,
                deadline = parseServerDateTime(classDetail.deadline)
                    ?: parseServerDateTime(classDetail.closeAt),
                ticketEndDate = parseServerDateTime(classDetail.ticketExpiresAt)?.toLocalDate()
            )
            _uiState.update { reduce(it, mutation) }

            val battleType = ClassBattleType.from(classDetail.battleType)
            classroomUseCases.getClassBattleUseCase(classId)
                .onSuccess { classBattle ->
                    val battle = classBattle.toUiModel(battleType)
                    _uiState.update { reduce(it, ClassEditMutation.BattleLoaded(battle)) }
                }
                .onFailure { sendEvent(ClassEditUiEvent.ShowLoadFailedToast) }
        }
    }

    private fun updateClass() {
        val state = _uiState.value
        val deadline = state.deadline
        val originalDeadline = state.originalDeadline
        val ticketEndDate = state.ticketEndDate
        val isDeadlineChanged = deadline != originalDeadline
        val isNotExtended =
            deadline != null && originalDeadline != null && !deadline.isAfter(originalDeadline)
        val isAfterTicketEnd = deadline != null && ticketEndDate != null
                && deadline.toLocalDate().isAfter(ticketEndDate)
        val isClassNameInvalid = state.className.isBlank()
        val isDeadlineOutOfRange = isDeadlineChanged && (isNotExtended || isAfterTicketEnd)
        if (isClassNameInvalid || isDeadlineOutOfRange) {
            _uiState.update {
                reduce(
                    it,
                    ClassEditMutation.SettingInvalid(isClassNameInvalid, isDeadlineOutOfRange)
                )
            }
            return
        }

        viewModelScope.launch {
            classroomUseCases.updateClassUseCase(
                classId = state.classId,
                name = state.className.trim(),
                deadline = if (isDeadlineChanged) deadline?.let(::formatServerDateTime) else null
            ).onSuccess {
                sendEvent(ClassEditUiEvent.NavigateBackAfterUpdated)
            }.onFailure { exception ->
                when ((exception as? ApiErrorException)?.code) {
                    ClassErrorCode.CLASS_400_DEADLINE, ClassErrorCode.CLASS_400_DEADLINE_EXT -> {
                        _uiState.update {
                            reduce(
                                it,
                                ClassEditMutation.SettingInvalid(
                                    isClassNameInvalid = false,
                                    isDeadlineOutOfRange = true
                                )
                            )
                        }
                    }

                    else -> {
                        _uiState.update { reduce(it, ClassEditMutation.RequestFailed) }
                        sendEvent(ClassEditUiEvent.ShowActionFailedToast)
                    }
                }
            }
        }
    }
}