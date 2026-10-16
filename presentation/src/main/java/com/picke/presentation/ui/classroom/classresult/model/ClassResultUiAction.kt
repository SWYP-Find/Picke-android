package com.picke.presentation.ui.classroom.classresult.model

sealed interface ClassResultUiAction {

    sealed interface StateOnly : ClassResultUiAction

    sealed interface SideEffectOnly : ClassResultUiAction

    data object BackClick : SideEffectOnly

    data class TabSelect(val tab: ClassResultTab) : StateOnly

    data object MyOpinionClick : SideEffectOnly
}