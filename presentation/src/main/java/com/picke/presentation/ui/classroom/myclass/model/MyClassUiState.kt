package com.picke.presentation.ui.classroom.myclass.model

data class MyClassUiState(
    val selectedFilter: MyClassFilter = MyClassFilter.ALL,
    val classes: List<MyClassUiModel> = emptyList(),
    val managingClassId: Long? = null,
    val isDeleteDialogVisible: Boolean = false,
    val isLeaveDialogVisible: Boolean = false
) {
    val managingClass: MyClassUiModel?
        get() = classes.firstOrNull { it.id == managingClassId }

    val filteredClasses: List<MyClassUiModel>
        get() = when (selectedFilter) {
            MyClassFilter.ALL -> classes
            MyClassFilter.IN_PROGRESS -> classes.filter { it.isInProgress }
            MyClassFilter.ENDED -> classes.filterNot { it.isInProgress }
        }
}