package com.picke.presentation.ui.classroom.classmember

import com.picke.presentation.ui.classroom.classmember.model.ClassMemberMutation
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiAction
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiState

internal fun reduce(state: ClassMemberUiState, action: ClassMemberUiAction): ClassMemberUiState =
    when (action) {
        is ClassMemberUiAction.SideEffectOnly -> state

        is ClassMemberUiAction.QueryChange -> state.copy(query = action.query)

        ClassMemberUiAction.EditNameClick -> state.copy(
            isNameSheetVisible = true,
            editingName = state.myName
        )

        is ClassMemberUiAction.EditingNameChange -> state.copy(editingName = action.name)

        ClassMemberUiAction.NameConfirm -> if (state.isNameConfirmEnabled) state.copy(isRequesting = true) else state

        ClassMemberUiAction.NameSheetDismiss -> state.copy(isNameSheetVisible = false)

        is ClassMemberUiAction.RemoveClick -> state.copy(removingMember = action.member)

        ClassMemberUiAction.RemoveConfirm -> if (state.isRemoveConfirmEnabled) {
            state.copy(removingMember = null, isRequesting = true)
        } else {
            state
        }

        ClassMemberUiAction.RemoveDialogDismiss -> state.copy(removingMember = null)
    }

internal fun reduce(state: ClassMemberUiState, mutation: ClassMemberMutation): ClassMemberUiState =
    when (mutation) {
        is ClassMemberMutation.MembersLoaded -> state.copy(
            isOperatorViewer = mutation.isOperatorViewer,
            members = mutation.members
        )

        is ClassMemberMutation.MyNameUpdated -> state.copy(
            members = state.members.map { if (it.isMe) it.copy(name = mutation.name) else it },
            isNameSheetVisible = false,
            isRequesting = false
        )

        is ClassMemberMutation.MemberRemoved -> state.copy(
            members = state.members.filterNot { it.memberId == mutation.memberId },
            isRequesting = false,
            isMembersChanged = true
        )

        ClassMemberMutation.RequestFailed -> state.copy(isRequesting = false)
    }