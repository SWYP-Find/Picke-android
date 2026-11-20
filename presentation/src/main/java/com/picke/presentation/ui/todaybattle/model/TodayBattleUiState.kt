package com.picke.presentation.ui.todaybattle.model

data class TodayBattleUiState(
    val isLoading: Boolean = true,
    val isEntering: Boolean = false,
    val battle: TodayBattleUiModel? = null,
    val errorMessage: String? = null
)