package com.picke.presentation.ui.classroom.classbattlepreview.model

sealed interface ClassBattlePreviewUiEvent {

    data object NavigateBack : ClassBattlePreviewUiEvent

    data object ShowLoadFailedToast : ClassBattlePreviewUiEvent
}