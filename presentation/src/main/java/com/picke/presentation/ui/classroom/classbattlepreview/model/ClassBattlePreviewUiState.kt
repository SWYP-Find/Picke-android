package com.picke.presentation.ui.classroom.classbattlepreview.model

import com.picke.presentation.ui.scenario.model.ScenarioScriptUiModel

data class ClassBattlePreviewUiState(
    val battleId: Long = 0L,
    val title: String = "",
    val scripts: List<ScenarioScriptUiModel> = emptyList(),
    val isPlaying: Boolean = false,
    val maxRevealedIndex: Int = -1,
    val currentPositionMs: Long = 0L,
    val totalDurationMs: Long = 0L,
    val playbackSpeed: Float = 1.0f
) {
    val visibleScripts: List<ScenarioScriptUiModel>
        get() = scripts.take(maxRevealedIndex + 1)

    val activeIndex: Int
        get() = scripts.indexOfLast { it.startTimeMs <= currentPositionMs }

    internal val isPreviewEnded: Boolean
        get() = currentPositionMs >= totalDurationMs
}