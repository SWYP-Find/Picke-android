package com.picke.presentation.ui.classroom.classcreate.model

import java.time.LocalDateTime

sealed interface ClassCreateUiAction {
    sealed interface StateOnly : ClassCreateUiAction

    data object BackClick : ClassCreateUiAction
    data object NextClick : ClassCreateUiAction
    data object EditConditionClick : StateOnly

    data class MethodSelect(val method: ClassCreateMethod) : StateOnly
    data class GuideClick(val method: ClassCreateMethod) : StateOnly
    data object GuideSheetDismiss : StateOnly
    data class TopicChange(val topic: String) : StateOnly
    data class LevelSelect(val level: ClassLevel) : StateOnly
    data class CategorySelect(val category: ClassCategory) : StateOnly

    data class BattleSelect(val battleId: Long) : StateOnly
    data class BattlePreviewClick(val battleId: Long) : StateOnly
    data object RegenerateClick : ClassCreateUiAction

    data class ClassNameChange(val name: String) : StateOnly
    data class DeadlineToggle(val isEnabled: Boolean) : ClassCreateUiAction
    data object DeadlineFieldClick : StateOnly
    data class DeadlineConfirm(val deadline: LocalDateTime) : StateOnly
    data object DeadlineSheetDismiss : StateOnly
    data class CommentRequiredToggle(val isRequired: Boolean) : StateOnly
    data class OperatorJoinToggle(val isJoined: Boolean) : StateOnly
}