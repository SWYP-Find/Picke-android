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

        ClassMyOpinionUiAction.CommentsEndReach -> if (state.canLoadMoreComments) {
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

        is ClassMyOpinionMutation.MyPerspectivesLoaded -> state.copy(perspectives = mutation.perspectives)

        is ClassMyOpinionMutation.MyCommentsLoaded -> state.copy(
            comments = mutation.comments,
            myDisplayName = mutation.myDisplayName,
            nextCursor = mutation.nextCursor,
            hasNext = mutation.hasNext
        )

        is ClassMyOpinionMutation.MoreMyCommentsLoaded -> state.copy(
            comments = state.comments + mutation.comments,
            nextCursor = mutation.nextCursor,
            hasNext = mutation.hasNext
        )
    }
    return if (mutation is ClassMyOpinionMutation.RequestFinished) reduced.copy(isRequesting = false) else reduced
}