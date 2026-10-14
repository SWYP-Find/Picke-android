package com.picke.presentation.ui.classroom.myclass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.ui.classroom.myclass.model.MyClassMutation
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiAction
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiEvent
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiState
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
class MyClassViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MyClassUiState())
    val uiState: StateFlow<MyClassUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<MyClassUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<MyClassUiEvent> = _uiEvent.receiveAsFlow()

    init {
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

            MyClassUiAction.DeleteConfirm -> state.managingClassId?.let(::deleteClass)
            MyClassUiAction.LeaveConfirm -> state.managingClassId?.let(::leaveClass)
            is MyClassUiAction.StateOnly -> Unit
        }
    }

    private fun sendEvent(event: MyClassUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchClasses() {
        // TODO: API 연동 - 내 클래스 목록 조회로 교체
        _uiState.update { reduce(it, MyClassMutation.ClassesLoaded(DummyData.dummyMyClasses)) }
    }

    private fun deleteClass(classId: Long) {
        // TODO: API 연동 - 클래스 삭제 요청 성공 시 반영
        _uiState.update { reduce(it, MyClassMutation.ClassDeleted(classId)) }
    }

    private fun leaveClass(classId: Long) {
        // TODO: API 연동 - 클래스 나가기 요청 성공 시 반영
        _uiState.update { reduce(it, MyClassMutation.ClassLeft(classId)) }
    }
}