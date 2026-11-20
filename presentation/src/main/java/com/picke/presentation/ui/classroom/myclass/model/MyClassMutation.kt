package com.picke.presentation.ui.classroom.myclass.model

internal sealed interface MyClassMutation {

    sealed interface RequestFinished : MyClassMutation

    data object RequestFailed : RequestFinished

    data class ClassesLoaded(val classes: List<MyClassUiModel>) : MyClassMutation

    data object ClassesLoadFailed : MyClassMutation

    data class ClassDeleted(val classId: Long) : RequestFinished

    data class ClassLeft(val classId: Long) : RequestFinished
}