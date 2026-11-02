package com.picke.presentation.ui.classroom.classmyopinion

import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionMutation
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiAction
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyOpinionUiState

internal fun reduce(
    state: ClassMyOpinionUiState,
    action: ClassMyOpinionUiAction
): ClassMyOpinionUiState =
    when (action) {
        is ClassMyOpinionUiAction.SideEffectOnly -> state

        ClassMyOpinionUiAction.RepliesEndReach -> if (state.canLoadMoreReplies) {
            state.copy(isRequesting = true)
        } else {
            state
        }
    }

internal fun reduce(
    state: ClassMyOpinionUiState,
    mutation: ClassMyOpinionMutation
): ClassMyOpinionUiState {
    val reduced = when (mutation) {
        ClassMyOpinionMutation.RequestFailed -> state.copy(hasNext = false)

        is ClassMyOpinionMutation.MyCommentsLoaded -> state.copy(comments = mutation.comments)

        is ClassMyOpinionMutation.RepliesLoaded -> state.copy(
            replies = mutation.replies,
            myDisplayName = mutation.myDisplayName,
            nextCursor = mutation.nextCursor,
            hasNext = mutation.hasNext
        )

        is ClassMyOpinionMutation.MoreRepliesLoaded -> state.copy(
            replies = state.replies + mutation.replies,
            nextCursor = mutation.nextCursor,
            hasNext = mutation.hasNext
        )
    }
    return if (mutation is ClassMyOpinionMutation.RequestFinished) reduced.copy(isRequesting = false) else reduced
}