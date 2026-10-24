package com.picke.presentation.ui.classroom.classmember.model

sealed interface ClassMemberUiEvent {

    data object NavigateBack : ClassMemberUiEvent

    data object NavigateBackAfterMembersChanged : ClassMemberUiEvent

    data object ShowLoadFailedToast : ClassMemberUiEvent

    data object ShowNameDuplicatedToast : ClassMemberUiEvent

    data object ShowActionFailedToast : ClassMemberUiEvent
}