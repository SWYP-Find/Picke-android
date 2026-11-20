package com.picke.presentation.ui.my.setting.model

sealed class SettingUiEvent {

    data object NavigateToLogin : SettingUiEvent()

    data object ShowLogoutFailedToast : SettingUiEvent()

    data object ShowWithdrawFailedToast : SettingUiEvent()
}