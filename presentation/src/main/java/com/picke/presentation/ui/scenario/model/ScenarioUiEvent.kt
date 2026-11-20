package com.picke.presentation.ui.scenario.model

sealed interface ScenarioUiEvent {

    data object ShowLoadFailedToast : ScenarioUiEvent

    data object ShowActionFailedToast : ScenarioUiEvent
}