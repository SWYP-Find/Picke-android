package com.picke.presentation.ui.classroom.classbattlepreview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.presentation.AppRoute
import com.picke.presentation.audio.AudioPlayerManager
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewMutation
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiAction
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiEvent
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiState
import com.picke.presentation.ui.component.AudioPlayerDefaults.SKIP_INTERVAL_MS
import com.picke.presentation.ui.component.AudioPlayerDefaults.SYNC_INTERVAL_MS
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.splitScriptsBySentence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ClassBattlePreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val audioPlayerManager: AudioPlayerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ClassBattlePreviewUiState(
            battleId = savedStateHandle.get<Long>(AppRoute.ARG_BATTLE_ID) ?: 0L
        )
    )
    val uiState: StateFlow<ClassBattlePreviewUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ClassBattlePreviewUiEvent>(Channel.BUFFERED)
    val uiEvent: Flow<ClassBattlePreviewUiEvent> = _uiEvent.receiveAsFlow()

    private var syncJob: Job? = null

    // TODO: API 연동 - 오디오를 loadAudio로 불러오면 isDummyPlayback·dummyPositionMs와 더미 재생 분기 삭제
    private val isDummyPlayback = true
    private var dummyPositionMs = 0L

    init {
        viewModelScope.launch {
            audioPlayerManager.isPlaying.collect { isPlaying ->
                _uiState.update { reduce(it, ClassBattlePreviewMutation.PlayingChanged(isPlaying)) }
                if (isPlaying) startSync() else stopSync()
            }
        }
        fetchBattlePreview()
    }

    fun onAction(action: ClassBattlePreviewUiAction) {
        val previousState = _uiState.value
        _uiState.update { reduce(it, action) }
        handleSideEffect(previousState, action)
    }

    private fun handleSideEffect(
        state: ClassBattlePreviewUiState,
        action: ClassBattlePreviewUiAction
    ) {
        when (action) {
            ClassBattlePreviewUiAction.BackClick -> sendEvent(ClassBattlePreviewUiEvent.NavigateBack)

            is ClassBattlePreviewUiAction.ScriptClick -> seekAndPlay(action.startTimeMs)

            is ClassBattlePreviewUiAction.Seek -> seekAndPlay((state.totalDurationMs * action.ratio).toLong())

            ClassBattlePreviewUiAction.SpeedClick -> audioPlayerManager.setPlaybackSpeed(_uiState.value.playbackSpeed)

            ClassBattlePreviewUiAction.RewindClick -> seekAndPlay(currentPosition() - SKIP_INTERVAL_MS)

            ClassBattlePreviewUiAction.PlayPauseClick -> togglePlayPause(state)

            ClassBattlePreviewUiAction.ForwardClick -> seekAndPlay(currentPosition() + SKIP_INTERVAL_MS)

            ClassBattlePreviewUiAction.ReplayClick -> seekAndPlay(0L)

            ClassBattlePreviewUiAction.ScreenStop -> pause()
        }
    }

    private fun sendEvent(event: ClassBattlePreviewUiEvent) {
        viewModelScope.launch { _uiEvent.send(event) }
    }

    private fun fetchBattlePreview() {
        // TODO: API 연동 - 시나리오 조회(fetchBattleScenarioUseCase)로 교체하고 시작 노드 오디오를 audioPlayerManager.loadAudio로 불러오기
        val battleId = _uiState.value.battleId
        val battle = DummyData.dummyClassBattles.firstOrNull { it.battleId == battleId }
        val scenario = DummyData.dummyClassBattlePreviewScenarios[battleId] ?: return
        val startNode = scenario.nodes[scenario.startNodeId] ?: return
        val sortedScripts = startNode.scripts.sortedBy { it.startTimeMs }
        val startMs = sortedScripts.firstOrNull()?.startTimeMs ?: 0L
        val mutation = ClassBattlePreviewMutation.BattlePreviewLoaded(
            title = battle?.title ?: "",
            scripts = splitScriptsBySentence(
                sortedScripts,
                startMs + startNode.audioDuration * 1000L
            ).filter { it.startTimeMs < PREVIEW_DURATION_MS },
            totalDurationMs = PREVIEW_DURATION_MS
        )
        _uiState.update { reduce(it, mutation) }
        play()
    }

    private fun togglePlayPause(state: ClassBattlePreviewUiState) {
        when {
            state.isPreviewEnded -> Unit
            state.isPlaying -> pause()
            else -> play()
        }
    }

    private fun seekAndPlay(positionMs: Long) {
        val targetMs = positionMs.coerceIn(0L, PREVIEW_DURATION_MS)
        seekTo(targetMs)
        syncPosition(targetMs)
        if (!_uiState.value.isPreviewEnded) play()
    }

    private fun play() {
        if (isDummyPlayback) {
            _uiState.update { reduce(it, ClassBattlePreviewMutation.PlayingChanged(true)) }
            startSync()
        } else {
            audioPlayerManager.play()
        }
    }

    private fun pause() {
        if (isDummyPlayback) {
            _uiState.update { reduce(it, ClassBattlePreviewMutation.PlayingChanged(false)) }
            stopSync()
        } else {
            audioPlayerManager.pause()
        }
    }

    private fun seekTo(positionMs: Long) {
        if (isDummyPlayback) dummyPositionMs = positionMs else audioPlayerManager.seekTo(positionMs)
    }

    private fun currentPosition(): Long =
        if (isDummyPlayback) dummyPositionMs else audioPlayerManager.currentPosition

    private fun startSync() {
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            while (isActive) {
                syncPosition(currentPosition())
                delay(SYNC_INTERVAL_MS.milliseconds)
                if (isDummyPlayback) dummyPositionMs += (SYNC_INTERVAL_MS * _uiState.value.playbackSpeed).toLong()
            }
        }
    }

    private fun stopSync() {
        syncJob?.cancel()
        syncJob = null
    }

    private fun syncPosition(positionMs: Long) {
        val mutation =
            ClassBattlePreviewMutation.PositionSynced(minOf(positionMs, PREVIEW_DURATION_MS))
        _uiState.update { reduce(it, mutation) }
        if (_uiState.value.isPreviewEnded) pause()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.release()
    }

    companion object {
        private const val PREVIEW_DURATION_MS = 60_000L
    }
}