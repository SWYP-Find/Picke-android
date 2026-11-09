package com.picke.presentation.ui.classroom.classdetail.model

import com.picke.presentation.ui.classroom.model.ClassBattleStep
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel

data class ClassDetailUiState(
    val classId: Long = 0L,
    val isLoading: Boolean = true,
    val classInfo: ClassDetailInfoUiModel? = null,
    val battle: ClassBattleUiModel? = null,
    val battleStep: ClassBattleStep = ClassBattleStep.NONE,
    val canParticipate: Boolean = false,
    val isManageSheetVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isLeaveDialogVisible: Boolean = false,
    val isClassChanged: Boolean = false,
    val isRequesting: Boolean = false
)