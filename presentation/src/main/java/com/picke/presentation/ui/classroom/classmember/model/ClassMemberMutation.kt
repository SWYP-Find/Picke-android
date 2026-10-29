package com.picke.presentation.ui.classroom.classmember.model

internal sealed interface ClassMemberMutation {

    sealed interface RequestFinished : ClassMemberMutation

    data object RequestFailed : RequestFinished

    data class MembersLoaded(val isOperatorViewer: Boolean, val members: List<ClassMemberUiModel>) : ClassMemberMutation

    data class MyNameUpdated(val name: String) : RequestFinished

    data class MemberRemoved(val memberId: Long) : RequestFinished
}