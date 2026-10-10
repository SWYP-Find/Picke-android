package com.picke.presentation.ui.classroom.classshare

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.ui.classroom.classshare.model.ClassShareMutation
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiAction
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiEvent
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiState
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
class ClassShareViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassShareUiState(classId = savedStateHandle.get<Long>(CLASS_ID_KEY) ?: 0L)
    )
    val uiState: StateFlow<ClassShareUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassShareUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassShareUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchClassShare()
    }

    fun onAction(action: ClassShareUiAction) {
        val state = _uiState.value
        when (action) {
            ClassShareUiAction.BackClick -> sendEvent(ClassShareUiEvent.NavigateBack)
            ClassShareUiAction.ShareCodeClick -> sendEvent(ClassShareUiEvent.ShareCode(state.code))
            ClassShareUiAction.CopyCodeClick -> sendEvent(ClassShareUiEvent.CopyCode(state.code))
            ClassShareUiAction.GoToClassClick -> sendEvent(ClassShareUiEvent.NavigateToClass(state.classId))
        }
    }

    private fun sendEvent(event: ClassShareUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchClassShare() {
        // TODO: API 연동 - 클래스 정보·참여 코드 조회로 교체
        val classItem =
            DummyData.dummyMyClasses.firstOrNull { it.id == _uiState.value.classId } ?: return
        val mutation = ClassShareMutation.ClassShareLoaded(
            className = classItem.title,
            deadline = DummyData.dummyClassDeadline,
            code = DummyData.dummyClassCode,
            battle = DummyData.dummyClassBattles.first()
        )
        _uiState.update { reduce(it, mutation) }
    }

    companion object {
        private const val CLASS_ID_KEY = "classId"
    }
}