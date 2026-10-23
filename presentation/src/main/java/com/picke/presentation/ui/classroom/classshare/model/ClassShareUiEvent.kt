package com.picke.presentation.ui.classroom.classshare.model

sealed interface ClassShareUiEvent {

    data object NavigateBack : ClassShareUiEvent

    data class ShareCode(val code: String) : ClassShareUiEvent

    data class CopyCode(val code: String) : ClassShareUiEvent

    data object NavigateToMyClass : ClassShareUiEvent

    data object ShowLoadFailedToast : ClassShareUiEvent
}