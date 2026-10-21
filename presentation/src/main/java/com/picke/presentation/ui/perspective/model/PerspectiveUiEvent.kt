package com.picke.presentation.ui.perspective.model

import androidx.annotation.StringRes

sealed interface PerspectiveUiEvent {
    data class ShowToast(@param:StringRes val messageResId: Int) : PerspectiveUiEvent
}