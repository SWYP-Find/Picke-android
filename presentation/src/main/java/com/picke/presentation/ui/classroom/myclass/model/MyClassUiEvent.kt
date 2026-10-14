package com.picke.presentation.ui.classroom.myclass.model

sealed interface MyClassUiEvent {
    data object NavigateBack : MyClassUiEvent
    data class NavigateToDetail(val classId: Long) : MyClassUiEvent
    data class NavigateToShare(val classId: Long) : MyClassUiEvent
}