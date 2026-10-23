package com.picke.presentation.ui.classroom.classmember.model

internal sealed interface ClassMemberMutation {
    data class MembersLoaded(val isOperatorViewer: Boolean, val members: List<ClassMemberUiModel>) : ClassMemberMutation

    data class MyNameUpdated(val name: String) : ClassMemberMutation

    data class MemberRemoved(val memberId: Long) : ClassMemberMutation

    data object RequestFailed : ClassMemberMutation
}