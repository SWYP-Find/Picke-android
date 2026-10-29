package com.picke.presentation.ui.classroom.classdetail.model

import com.picke.presentation.ui.classroom.model.ClassBattleStep
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDateTime

data class ClassDetailUiState(
    val classId: Long = 0L,
    val isOperator: Boolean = false,
    val isInProgress: Boolean = true,
    val title: String = "",
    val memberCount: Int = 0,
    val deadline: LocalDateTime? = null,
    val battle: ClassBattleUiModel? = null,
    val battleStep: ClassBattleStep = ClassBattleStep.NONE,
    val canParticipate: Boolean = false,
    val isManageSheetVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isLeaveDialogVisible: Boolean = false,
    val isClassChanged: Boolean = false,
    val isRequesting: Boolean = false
)