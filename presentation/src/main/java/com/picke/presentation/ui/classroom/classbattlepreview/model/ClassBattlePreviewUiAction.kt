package com.picke.presentation.ui.classroom.classbattlepreview.model

sealed interface ClassBattlePreviewUiAction {

    sealed interface SideEffectOnly : ClassBattlePreviewUiAction

    data object BackClick : SideEffectOnly

    data class ScriptClick(val startTimeMs: Long) : SideEffectOnly

    data class Seek(val ratio: Float) : SideEffectOnly

    data object SpeedClick : ClassBattlePreviewUiAction

    data object RewindClick : SideEffectOnly

    data object PlayPauseClick : SideEffectOnly

    data object ForwardClick : SideEffectOnly

    data object ReplayClick : SideEffectOnly

    data object ScreenStop : SideEffectOnly
}
