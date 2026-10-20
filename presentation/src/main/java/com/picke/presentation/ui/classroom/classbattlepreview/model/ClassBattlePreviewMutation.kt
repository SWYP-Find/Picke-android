package com.picke.presentation.ui.classroom.classbattlepreview.model

import com.picke.presentation.ui.scenario.model.ScenarioScriptUiModel

internal sealed interface ClassBattlePreviewMutation {

    data class BattlePreviewLoaded(
        val title: String,
        val scripts: List<ScenarioScriptUiModel>,
        val totalDurationMs: Long
    ) : ClassBattlePreviewMutation

    data class PlayingChanged(val isPlaying: Boolean) : ClassBattlePreviewMutation

    data class PositionSynced(val positionMs: Long) : ClassBattlePreviewMutation
}