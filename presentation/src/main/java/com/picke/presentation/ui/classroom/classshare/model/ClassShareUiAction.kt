package com.picke.presentation.ui.classroom.classshare.model

sealed interface ClassShareUiAction {
    data object BackClick : ClassShareUiAction
    data object ShareCodeClick : ClassShareUiAction
    data object CopyCodeClick : ClassShareUiAction
    data object GoToClassClick : ClassShareUiAction
}