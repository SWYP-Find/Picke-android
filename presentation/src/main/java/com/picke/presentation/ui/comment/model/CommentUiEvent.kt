package com.picke.presentation.ui.comment.model

import androidx.annotation.StringRes

sealed interface CommentUiEvent {
    data class ShowToast(@param:StringRes val messageResId: Int) : CommentUiEvent
}