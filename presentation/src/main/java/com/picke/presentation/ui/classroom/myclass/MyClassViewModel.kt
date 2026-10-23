package com.picke.presentation.ui.classroom.myclass

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.myclass.model.MyClassMutation
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiAction
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiEvent
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiState
import com.picke.presentation.ui.classroom.myclass.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyClassViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyClassUiState())
    val uiState: StateFlow<MyClassUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<MyClassUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<MyClassUiEvent> = _uiEvent.receiveAsFlow()

    init {
        observeClassChanged()
        fetchClasses()
    }

    fun onAction(action: MyClassUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: MyClassUiState, action: MyClassUiAction) {
        when (action) {
            MyClassUiAction.BackClick -> sendEvent(MyClassUiEvent.NavigateBack)
            is MyClassUiAction.ClassClick -> sendEvent(MyClassUiEvent.NavigateToDetail(action.classId))
            // TODO: API 연동 - 클래스 정보 수정 화면 연결
            MyClassUiAction.ManageEditClick -> Unit
            MyClassUiAction.ManageShareCodeClick -> state.managingClassId?.let { classId ->
                sendEvent(MyClassUiEvent.NavigateToShare(classId))
            }

            MyClassUiAction.DeleteConfirm -> if (!state.isRequesting) state.managingClassId?.let(::deleteClass)
            MyClassUiAction.LeaveConfirm -> if (!state.isRequesting) state.managingClassId?.let(::leaveClass)
            is MyClassUiAction.StateOnly -> Unit
        }
    }

    private fun sendEvent(event: MyClassUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun observeClassChanged() {
        savedStateHandle.getStateFlow(AppRoute.RESULT_CLASS_CHANGED, false)
            .filter { it }
            .onEach {
                savedStateHandle[AppRoute.RESULT_CLASS_CHANGED] = false
                fetchClasses()
            }
            .launchIn(viewModelScope)
    }

    private fun fetchClasses() {
        viewModelScope.launch {
            classroomUseCases.getMyClassesUseCase()
                .onSuccess { classes ->
                    val classUiModels = classes.map { it.toUiModel() }
                    _uiState.update { reduce(it, MyClassMutation.ClassesLoaded(classUiModels)) }
                }
                .onFailure {
                    _uiState.update { reduce(it, MyClassMutation.ClassesLoadFailed) }
                    sendEvent(MyClassUiEvent.ShowLoadFailedToast)
                }
        }
    }

    private fun deleteClass(classId: Long) {
        viewModelScope.launch {
            classroomUseCases.deleteClassUseCase(classId)
                .onSuccess { _uiState.update { reduce(it, MyClassMutation.ClassDeleted(classId)) } }
                .onFailure {
                    _uiState.update { reduce(it, MyClassMutation.RequestFailed) }
                    sendEvent(MyClassUiEvent.ShowActionFailedToast)
                }
        }
    }

    private fun leaveClass(classId: Long) {
        viewModelScope.launch {
            classroomUseCases.leaveClassUseCase(classId)
                .onSuccess { _uiState.update { reduce(it, MyClassMutation.ClassLeft(classId)) } }
                .onFailure {
                    _uiState.update { reduce(it, MyClassMutation.RequestFailed) }
                    sendEvent(MyClassUiEvent.ShowActionFailedToast)
                }
        }
    }
}