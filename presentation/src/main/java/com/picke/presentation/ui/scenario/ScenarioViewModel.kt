package com.picke.presentation.ui.scenario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.scenario.usecase.ScenarioUseCases
import com.picke.presentation.BuildConfig
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.analytics.BattleStepName
import com.picke.presentation.ui.scenario.model.PastChoice
import com.picke.presentation.ui.scenario.model.ScenarioUiModel
import com.picke.presentation.ui.scenario.model.ScenarioUiState
import com.picke.presentation.ui.scenario.model.toUiModel
import com.picke.presentation.util.ScenarioAudioKey
import com.picke.presentation.util.splitScriptsBySentence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ScenarioViewModel @Inject constructor(
    private val scenarioUseCases: ScenarioUseCases,
    private val audioPlayerManager: AudioPlayerManager,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScenarioUiState())
    val uiState: StateFlow<ScenarioUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var fullScenario: ScenarioUiModel? = null
    private var currentAudioKey: String? = null
    private var currentBattleId: String = ""
    private var isAudioEndTracked = false

    init {
        audioPlayerManager.onPlaybackEnded = { handleNodeEnd() }
        viewModelScope.launch {
            audioPlayerManager.isPlaying.collect { isPlaying ->
                _uiState.update { it.copy(isPlaying = isPlaying) }
                if (isPlaying) startSync() else stopSync()
            }
        }
    }

    fun loadScenario(battleId: String) {
        currentBattleId = battleId
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    pastScripts = emptyList(),
                    pastChoices = emptyList(),
                    maxListenedPositionMs = 0L
                )
            }
            scenarioUseCases.fetchBattleScenarioUseCase(battleId)
                .onSuccess { board ->
                    fullScenario = board.toUiModel()
                    _uiState.update { it.copy(title = board.title) }
                    val firstNodeId = fullScenario?.startNodeId ?: return@onSuccess
                    loadNode(firstNodeId)
                }
        }
    }

    private fun loadNode(nodeId: String) {
        val node = fullScenario?.nodes?.get(nodeId) ?: return

        val targetKey = when {
            node.nodeName.contains(ScenarioAudioKey.NODE_SUFFIX_A) -> ScenarioAudioKey.PATH_A
            node.nodeName.contains(ScenarioAudioKey.NODE_SUFFIX_B) -> ScenarioAudioKey.PATH_B
            else -> currentAudioKey ?: fullScenario?.audios?.keys?.firstOrNull()
        }

        if (targetKey != null && targetKey != currentAudioKey) {
            val audioUrl = fullScenario?.audios?.get(targetKey)
            if (audioUrl != null) {
                currentAudioKey = targetKey
                val fullAudioUrl =
                    if (audioUrl.startsWith("http")) audioUrl
                    else "${BuildConfig.BASE_URL}${audioUrl.removePrefix("/")}"
                audioPlayerManager.loadAudio(fullAudioUrl, _uiState.value.currentPositionMs)
            }
        }

        val sortedScripts = node.scripts.sortedBy { it.startTimeMs }
        val startMs = sortedScripts.firstOrNull()?.startTimeMs ?: _uiState.value.currentPositionMs
        val endMs = startMs + (node.audioDuration * 1000L)

        val splitScripts = splitScriptsBySentence(sortedScripts, endMs)

        val currentScripts = _uiState.value.scripts
        val newPastScripts =
            if (currentScripts.isNotEmpty() && _uiState.value.currentNodeId != nodeId) {
                _uiState.value.pastScripts + currentScripts
            } else {
                _uiState.value.pastScripts
            }

        _uiState.update {
            it.copy(
                currentNodeId = nodeId,
                pastScripts = newPastScripts,
                scripts = splitScripts,
                nodeEndTimeMs = endMs,
                activeIndex = -1,
                activePastIndex = -1,
                maxRevealedIndex = -1,
                showOptions = false,
                interactiveOptions = emptyList()
            )
        }

        playAudio()
    }

    private fun updateSync(positionMs: Long) {
        val scripts = _uiState.value.scripts
        val newActiveIndex = scripts.indexOfLast { it.startTimeMs <= positionMs }
        val newActivePastIndex = if (newActiveIndex < 0) {
            _uiState.value.pastScripts.indexOfLast { it.startTimeMs <= positionMs }
        } else {
            -1
        }
        val newMaxRevealed = maxOf(_uiState.value.maxRevealedIndex, newActiveIndex)
        val newMaxListened = maxOf(_uiState.value.maxListenedPositionMs, positionMs)
        val isNodeEnded = positionMs >= _uiState.value.nodeEndTimeMs

        _uiState.update {
            it.copy(
                currentPositionMs = positionMs,
                maxListenedPositionMs = newMaxListened,
                totalDurationMs = audioPlayerManager.duration,
                activeIndex = newActiveIndex,
                activePastIndex = newActivePastIndex,
                maxRevealedIndex = newMaxRevealed,
                showOptions = isNodeEnded
            )
        }

        if (isNodeEnded && audioPlayerManager.isPlaying.value) {
            handleNodeEnd()
        }
    }

    private fun handleNodeEnd() {
        pauseAudio()
        val currentNode = fullScenario?.nodes?.get(_uiState.value.currentNodeId) ?: return

        if (_uiState.value.isReviewing) return

        if (currentNode.autoNextNodeId != null) {
            loadNode(currentNode.autoNextNodeId)
        } else if (currentNode.interactiveOptions.isNotEmpty()) {
            _uiState.update { it.copy(interactiveOptions = currentNode.interactiveOptions) }
        } else {
            if (!isAudioEndTracked && currentBattleId.isNotEmpty()) {
                isAudioEndTracked = true
                analyticsTracker.trackBattleStep(BattleStepName.AUDIO_END, currentBattleId)
            }
            _uiState.update { it.copy(showFinalVoteDialog = true) }
        }
    }

    fun dismissFinalDialog() {
        _uiState.update { it.copy(showFinalVoteDialog = false) }
    }

    fun restartCurrentAudio() {
        val allScripts = _uiState.value.pastScripts + _uiState.value.scripts

        _uiState.update {
            it.copy(
                showFinalVoteDialog = false,
                isReviewing = true,
                pastScripts = emptyList(),
                scripts = allScripts,
                maxRevealedIndex = allScripts.size - 1
            )
        }

        seekAndPlay(0)
    }

    fun togglePlayPause() {
        if (audioPlayerManager.isPlaying.value) pauseAudio() else playAudio()
    }

    fun onScreenStopped() {
        pauseAudio()
    }

    private fun playAudio() {
        audioPlayerManager.play()
    }

    private fun pauseAudio() {
        audioPlayerManager.pause()
    }

    private fun startSync() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                updateSync(audioPlayerManager.currentPosition)
                delay(SYNC_INTERVAL_MS.milliseconds)
            }
        }
    }

    private fun stopSync() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun seekAndPlay(positionMs: Long) {
        audioPlayerManager.seekTo(positionMs)
        updateSync(positionMs)
        playAudio()
    }

    fun seekRewind() {
        seekAndPlay(maxOf(0, audioPlayerManager.currentPosition - SKIP_INTERVAL_MS))
    }

    fun seekToPosition(ratio: Float) {
        val newPos = (_uiState.value.totalDurationMs * ratio).toLong()
        val safePos =
            minOf(newPos, _uiState.value.nodeEndTimeMs, _uiState.value.maxListenedPositionMs)
        seekAndPlay(safePos)
    }

    fun seekForward() {
        if (_uiState.value.showOptions || _uiState.value.showFinalVoteDialog) return
        val safePos = minOf(
            audioPlayerManager.currentPosition + SKIP_INTERVAL_MS,
            _uiState.value.nodeEndTimeMs
        )
        seekAndPlay(safePos)
    }

    fun cyclePlaybackSpeed() {
        val nextIndex =
            (PLAYBACK_SPEEDS.indexOf(_uiState.value.playbackSpeed) + 1).mod(PLAYBACK_SPEEDS.size)
        val nextSpeed = PLAYBACK_SPEEDS[nextIndex]
        _uiState.update { it.copy(playbackSpeed = nextSpeed) }
        audioPlayerManager.setPlaybackSpeed(nextSpeed)
    }

    fun replayFromStart() {
        seekAndPlay(0)
    }

    fun selectOption(nextNodeId: String) {
        val currentOptions = _uiState.value.interactiveOptions
        val totalScriptsSize = _uiState.value.pastScripts.size + _uiState.value.scripts.size

        val newChoice = PastChoice(
            scriptIndex = totalScriptsSize - 1,
            options = currentOptions,
            selectedNextNodeId = nextNodeId
        )

        _uiState.update { it.copy(pastChoices = it.pastChoices + newChoice) }
        loadNode(nextNodeId)
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.release()
    }

    companion object {
        private val PLAYBACK_SPEEDS = listOf(1.0f, 1.5f, 2.0f, 3.0f)
        private const val SYNC_INTERVAL_MS = 100L
        private const val SKIP_INTERVAL_MS = 15_000L
    }
}