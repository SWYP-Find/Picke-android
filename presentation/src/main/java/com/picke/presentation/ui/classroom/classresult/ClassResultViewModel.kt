package com.picke.presentation.ui.classroom.classresult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classresult.model.ClassResultMutation
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiAction
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiEvent
import com.picke.presentation.ui.classroom.classresult.model.ClassResultUiState
import com.picke.presentation.ui.classroom.classresult.model.toUiModel
import com.picke.presentation.ui.classroom.model.ClassBattleType
import com.picke.presentation.ui.classroom.model.toUiModel
import com.picke.presentation.util.DummyData
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
class ClassResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases
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
        val classId = _uiState.value.classId
        viewModelScope.launch {
            val classResult = classroomUseCases.getClassResultUseCase(classId).getOrElse {
                sendEvent(ClassResultUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val classDetail = classroomUseCases.getClassDetailUseCase(classId).getOrElse {
                sendEvent(ClassResultUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val classBattle = classroomUseCases.getClassBattleUseCase(classId).getOrElse {
                sendEvent(ClassResultUiEvent.ShowLoadFailedToast)
                return@launch
            }
            val battle = classBattle.toUiModel(ClassBattleType.from(classDetail.battleType))
            val hasSummaryTab = classResult.insightState != null
            val hasReportTab = classResult.myReportState != null
            val mutation = ClassResultMutation.ClassResultLoaded(
                className = classDetail.name,
                isInProgress = !classResult.closed,
                tag = battle.tag,
                battleTitle = battle.title,
                deadline = parseServerDateTime(classResult.closeAt),
                hasSummaryTab = hasSummaryTab,
                hasReportTab = hasReportTab,
                summary = if (hasSummaryTab) classResult.stats.toUiModel() else null
            )
            _uiState.update { reduce(it, mutation) }

            if (hasSummaryTab) fetchInsight()
            if (hasReportTab) fetchMyReport()
        }
    }

    private fun fetchInsight() {
        // TODO: API 연동 - 클래스 인사이트 조회로 교체
        val dummySummary = DummyData.dummyClassResultSummary
        val mutation = ClassResultMutation.InsightLoaded(
            aiInsight = dummySummary.aiInsight,
            bestComment = dummySummary.bestComment,
            mostDebatedComment = dummySummary.mostDebatedComment
        )
        _uiState.update { reduce(it, mutation) }
    }

    private fun fetchMyReport() {
        // TODO: API 연동 - 내 AI 피드백 조회로 교체
        val mutation = ClassResultMutation.MyReportLoaded(DummyData.dummyClassResultReport)
        _uiState.update { reduce(it, mutation) }
    }
}