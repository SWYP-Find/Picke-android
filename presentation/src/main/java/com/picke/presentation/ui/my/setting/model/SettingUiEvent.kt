package com.picke.presentation.ui.my.setting.model

import androidx.annotation.StringRes

sealed class SettingUiEvent {
    data object NavigateToLogin : SettingUiEvent()
    data class ShowToast(@param:StringRes val messageResId: Int) : SettingUiEvent()
}