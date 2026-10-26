package com.picke.presentation.ui.classroom.classbattlepreview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.scenario.usecase.ScenarioUseCases
import com.picke.presentation.AppRoute
import com.picke.presentation.BuildConfig
import com.picke.presentation.audio.AudioPlayerManager
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewMutation
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiAction
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiEvent
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiState
import com.picke.presentation.ui.component.AudioPlayerDefaults.SKIP_INTERVAL_MS
import com.picke.presentation.ui.component.AudioPlayerDefaults.SYNC_INTERVAL_MS
import com.picke.presentation.ui.scenario.model.toUiModel
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
    private val scenarioUseCases: ScenarioUseCases,
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
        viewModelScope.launch {
            scenarioUseCases.fetchBattleScenarioUseCase(_uiState.value.battleId.toString())
                .onSuccess { board ->
                    val scenario = board.toUiModel()
                    val startNode = scenario.nodes[scenario.startNodeId]
                    val audioUrl = scenario.audios.values.firstOrNull()
                    if (startNode == null || audioUrl == null) {
                        sendEvent(ClassBattlePreviewUiEvent.ShowLoadFailedToast)
                        return@onSuccess
                    }
                    val sortedScripts = startNode.scripts.sortedBy { it.startTimeMs }
                    val startMs = sortedScripts.firstOrNull()?.startTimeMs ?: 0L
                    val mutation = ClassBattlePreviewMutation.BattlePreviewLoaded(
                        title = board.title,
                        scripts = splitScriptsBySentence(
                            sortedScripts,
                            startMs + startNode.audioDuration * 1000L
                        ).filter { it.startTimeMs < PREVIEW_DURATION_MS },
                        totalDurationMs = PREVIEW_DURATION_MS
                    )
                    _uiState.update { reduce(it, mutation) }
                    val fullAudioUrl =
                        if (audioUrl.startsWith("http")) audioUrl
                        else "${BuildConfig.BASE_URL}${audioUrl.removePrefix("/")}"
                    audioPlayerManager.loadAudio(fullAudioUrl)
                    play()
                }
                .onFailure { sendEvent(ClassBattlePreviewUiEvent.ShowLoadFailedToast) }
        }
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
        audioPlayerManager.play()
    }

    private fun pause() {
        audioPlayerManager.pause()
    }

    private fun seekTo(positionMs: Long) {
        audioPlayerManager.seekTo(positionMs)
    }

    private fun currentPosition(): Long = audioPlayerManager.currentPosition

    private fun startSync() {
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            while (isActive) {
                syncPosition(currentPosition())
                delay(SYNC_INTERVAL_MS.milliseconds)
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