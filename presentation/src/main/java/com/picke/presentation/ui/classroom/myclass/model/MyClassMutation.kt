package com.picke.presentation.ui.classroom.myclass.model

internal sealed interface MyClassMutation {
    data class ClassesLoaded(val classes: List<MyClassUiModel>) : MyClassMutation
    data class ClassLeft(val classId: Long) : MyClassMutation
    data class ClassDeleted(val classId: Long) : MyClassMutation
}