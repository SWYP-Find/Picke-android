package com.picke.presentation.ui.classroom.classbattlepreview

import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewMutation
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiAction
import com.picke.presentation.ui.classroom.classbattlepreview.model.ClassBattlePreviewUiState
import com.picke.presentation.ui.component.AudioPlayerDefaults.PLAYBACK_SPEEDS

internal fun reduce(
    state: ClassBattlePreviewUiState,
    action: ClassBattlePreviewUiAction
): ClassBattlePreviewUiState =
    when (action) {
        is ClassBattlePreviewUiAction.SideEffectOnly -> state

        ClassBattlePreviewUiAction.SpeedClick -> state.copy(
            playbackSpeed = PLAYBACK_SPEEDS[(PLAYBACK_SPEEDS.indexOf(state.playbackSpeed) + 1)
                .mod(PLAYBACK_SPEEDS.size)]
        )
    }

internal fun reduce(
    state: ClassBattlePreviewUiState,
    mutation: ClassBattlePreviewMutation
): ClassBattlePreviewUiState =
    when (mutation) {
        is ClassBattlePreviewMutation.BattlePreviewLoaded -> state.copy(
            title = mutation.title,
            scripts = mutation.scripts,
            totalDurationMs = mutation.totalDurationMs
        )

        is ClassBattlePreviewMutation.PlayingChanged -> state.copy(isPlaying = mutation.isPlaying)

        is ClassBattlePreviewMutation.PositionSynced -> state.copy(
            maxRevealedIndex = maxOf(
                state.maxRevealedIndex,
                state.scripts.indexOfLast { it.startTimeMs <= mutation.positionMs }
            ),
            currentPositionMs = mutation.positionMs
        )
    }
