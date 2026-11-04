package com.picke.presentation.ui.my.point.model

sealed interface PointUiEvent {
    data object ShowLoadFailedToast : PointUiEvent
}