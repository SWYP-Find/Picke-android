package com.picke.presentation.ui.my.notice.model

sealed interface NoticeEventUiEvent {
    data object ShowLoadFailedToast : NoticeEventUiEvent
}