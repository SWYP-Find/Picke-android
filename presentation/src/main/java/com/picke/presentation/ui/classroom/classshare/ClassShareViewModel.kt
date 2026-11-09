package com.picke.presentation.ui.classroom.classshare

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.classroom.usecase.ClassroomUseCases
import com.picke.presentation.navigation.AppRoute
import com.picke.presentation.ui.classroom.classshare.model.ClassShareMutation
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiAction
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiEvent
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiState
import com.picke.presentation.ui.classroom.classshare.model.toUiModel
import com.picke.presentation.ui.classroom.model.ClassBattleType
import com.picke.presentation.ui.classroom.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
    savedStateHandle: SavedStateHandle,
    private val classroomUseCases: ClassroomUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassShareUiState(classId = savedStateHandle.get<Long>(AppRoute.ARG_CLASS_ID) ?: 0L)
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

            ClassShareUiAction.ShareCodeClick -> state.classInfo?.let { classInfo ->
                sendEvent(ClassShareUiEvent.ShareCode(classInfo.className, classInfo.code))
            }

            ClassShareUiAction.CopyCodeClick -> state.classInfo?.let { classInfo ->
                sendEvent(ClassShareUiEvent.CopyCode(classInfo.code))
            }

            ClassShareUiAction.GoToClassClick -> sendEvent(ClassShareUiEvent.NavigateToMyClass)
        }
    }

    private fun sendEvent(event: ClassShareUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchClassShare() {
        val classId = _uiState.value.classId
        viewModelScope.launch {
            val classDetailDeferred = async { classroomUseCases.getClassDetailUseCase(classId) }
            val classBattleDeferred = async { classroomUseCases.getClassBattleUseCase(classId) }
            val classDetail = classDetailDeferred.await().getOrNull()
            val classBattle = classBattleDeferred.await().getOrNull()

            if (classDetail == null || classBattle == null) {
                _uiState.update { reduce(it, ClassShareMutation.ClassShareLoadFailed) }
                sendEvent(ClassShareUiEvent.ShowLoadFailedToast)
                return@launch
            }

            val mutation = ClassShareMutation.ClassShareLoaded(
                classInfo = classDetail.toUiModel(),
                battle = classBattle.toUiModel(ClassBattleType.from(classDetail.battleType))
            )
            _uiState.update { reduce(it, mutation) }
        }
    }
}