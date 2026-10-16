package com.picke.presentation.ui.classroom.classmyopinion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionMutation
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiAction
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiEvent
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiState
import com.picke.presentation.util.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ClassMyOpinionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ClassMyOpinionUiState(classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L)
    )
    val uiState: StateFlow<ClassMyOpinionUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassMyOpinionUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassMyOpinionUiEvent> = _uiEvent.receiveAsFlow()

    init {
        fetchMyOpinions()
    }

    fun onAction(action: ClassMyOpinionUiAction) {
        when (action) {
            ClassMyOpinionUiAction.BackClick -> sendEvent(ClassMyOpinionUiEvent.NavigateBack)
        }
    }

    private fun sendEvent(event: ClassMyOpinionUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchMyOpinions() {
        // TODO: API 연동 - 클래스에서 내가 남긴 댓글·대댓글 조회로 교체
        val mutation = ClassMyOpinionMutation.ClassMyOpinionLoaded(
            comments = DummyData.dummyClassMyComments,
            replies = DummyData.dummyClassMyReplies
        )
        _uiState.update { reduce(it, mutation) }
    }
}