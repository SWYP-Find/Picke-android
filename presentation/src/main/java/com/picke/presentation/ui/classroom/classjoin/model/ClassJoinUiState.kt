package com.picke.presentation.ui.classroom.classjoin.model

data class ClassJoinUiState(
    val step: ClassJoinStep = ClassJoinStep.CODE,
    val code: String = "",
    val isCodeInvalid: Boolean = false,
    val name: String = "",
    val isNameDuplicated: Boolean = false
) {
    val isNextEnabled: Boolean
        get() = when (step) {
            ClassJoinStep.CODE -> code.length == CODE_LENGTH
            ClassJoinStep.NAME -> name.isNotBlank()
        }

    companion object {
        const val CODE_LENGTH = 6
        const val NAME_MAX_LENGTH = 12
    }
}