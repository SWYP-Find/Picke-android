package com.picke.presentation.ui.home.model

import com.picke.presentation.ui.attendance.AttendanceCheckUiState

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasNewNotice: Boolean = false,
    val isAlarmStatusLoading: Boolean = true,
    val editorPicks: List<HomeEditorPickUiModel>? = null,
    val trendingBattles: List<HomeTrendingBattleUiModel> = emptyList(),
    val bestBattles: List<HomeBestBattleUiModel> = emptyList(),
    val bestComments: List<HomeBestCommentUiModel> = emptyList(),
    val newBattles: List<HomeNewBattleUiModel> = emptyList(),
    val attendanceCheckUiState: AttendanceCheckUiState? = null
)