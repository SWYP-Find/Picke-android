package com.picke.presentation.ui.recommend.model

sealed interface RecommendUiEvent {
    data object ShowLoadFailedToast : RecommendUiEvent
}