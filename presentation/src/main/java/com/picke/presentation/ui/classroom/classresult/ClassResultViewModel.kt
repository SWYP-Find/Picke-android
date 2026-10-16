package com.picke.presentation.ui.classroom.classresult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classresult.model.ClassResultMutation
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiAction
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiEvent
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiState
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
class ClassResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassResultUiState(classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L)
    )
    val uiState: StateFlow<ClassResultUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassResultUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassResultUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchClassResult()
    }

    fun onAction(action: ClassResultUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(state: ClassResultUiState, action: ClassResultUiAction) {
        when (action) {
            is ClassResultUiAction.StateOnly -> Unit

            ClassResultUiAction.BackClick -> sendEvent(ClassResultUiEvent.NavigateBack)

            ClassResultUiAction.MyOpinionClick ->
                sendEvent(ClassResultUiEvent.NavigateToMyOpinion(state.classId))
        }
    }

    private fun sendEvent(event: ClassResultUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchClassResult() {
        // TODO: API 연동 - 클래스 결과(요약·리포트) 조회로 교체
        val classItem =
            DummyData.dummyMyClasses.firstOrNull { it.id == _uiState.value.classId } ?: return
        val battle = DummyData.dummyClassBattleByClassId[classItem.id] ?: return
        val mutation = ClassResultMutation.ClassResultLoaded(
            className = classItem.title,
            isInProgress = classItem.isInProgress,
            tag = battle.tag,
            battleTitle = battle.title,
            deadline = classItem.deadline,
            summary = if (classItem.isOperator) DummyData.dummyClassResultSummary else null,
            report = DummyData.dummyClassResultReport
        )
        _uiState.update { reduce(it, mutation) }
    }
}