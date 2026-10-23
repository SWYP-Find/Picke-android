package com.picke.presentation.ui.classroom.classmember.model

sealed interface ClassMemberUiEvent {

    data object NavigateBack : ClassMemberUiEvent

    data object ShowLoadFailedToast : ClassMemberUiEvent
}