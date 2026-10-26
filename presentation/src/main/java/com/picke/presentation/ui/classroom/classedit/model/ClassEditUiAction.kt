package com.picke.presentation.ui.classroom.classedit.model

import java.time.LocalDateTime

sealed interface ClassEditUiAction {

    sealed interface StateOnly : ClassEditUiAction

    sealed interface SideEffectOnly : ClassEditUiAction

    data object BackClick : SideEffectOnly

    data class ClassNameChange(val name: String) : StateOnly

    data object DeadlineFieldClick : StateOnly

    data object SaveClick : ClassEditUiAction

    data class DeadlineConfirm(val deadline: LocalDateTime) : StateOnly

    data object DeadlineSheetDismiss : StateOnly
}