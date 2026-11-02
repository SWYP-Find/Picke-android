package com.picke.presentation.ui.classroom.classmyopinion.model

sealed interface ClassMyOpinionUiAction {

    sealed interface SideEffectOnly : ClassMyOpinionUiAction

    data object BackClick : SideEffectOnly

    data object RepliesEndReach : ClassMyOpinionUiAction
}