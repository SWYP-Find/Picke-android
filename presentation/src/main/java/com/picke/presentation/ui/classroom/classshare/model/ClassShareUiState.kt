package com.picke.presentation.ui.classroom.classshare.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel

data class ClassShareUiState(
    val classId: Long = 0L,
    val isLoading: Boolean = true,
    val classInfo: ClassShareInfoUiModel? = null,
    val battle: ClassBattleUiModel? = null
)