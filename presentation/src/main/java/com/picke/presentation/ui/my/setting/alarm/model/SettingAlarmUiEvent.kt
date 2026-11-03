package com.picke.presentation.ui.my.setting.alarm.model

sealed interface SettingAlarmUiEvent {
    data object ShowActionFailedToast : SettingAlarmUiEvent
}