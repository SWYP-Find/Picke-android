package com.picke.presentation.ui.classroom.classedit.model

import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import java.time.LocalDate
import java.time.LocalDateTime

data class ClassEditUiState(
    val classId: Long = 0L,
    val originalClassName: String = "",
    val className: String = "",
    val isClassNameInvalid: Boolean = false,
    val originalDeadline: LocalDateTime? = null,
    val deadline: LocalDateTime? = null,
    val isDeadlineSheetVisible: Boolean = false,
    val isDeadlineOutOfRange: Boolean = false,
    val ticketEndDate: LocalDate? = null,
    val battle: ClassBattleUiModel? = null,
    val isRequesting: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = !isRequesting && deadline != null && isChanged

    private val isChanged: Boolean
        get() = className.trim() != originalClassName || deadline != originalDeadline
}