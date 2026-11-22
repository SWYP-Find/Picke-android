package com.picke.presentation.ui.classroom.classjoin.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.ui.classroom.classjoin.component.ClassCodeBox
import com.picke.presentation.ui.classroom.classjoin.model.ClassCodeBoxState
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiState
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData

private const val EMPTY_CODE_CHAR = "-"

@Composable
internal fun ClassJoinCodeStep(
    code: String,
    isCodeInvalid: Boolean,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeIndex = (code.length - 1).coerceAtLeast(0)

    BasicTextField(
        value = code,
        onValueChange = onCodeChange,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(ClassJoinUiState.CODE_LENGTH) { index ->
                val boxState = when {
                    index < code.length && isCodeInvalid -> ClassCodeBoxState.ERROR
                    index == activeIndex -> ClassCodeBoxState.ACTIVE
                    index < code.length -> ClassCodeBoxState.FILLED
                    else -> ClassCodeBoxState.EMPTY
                }

                ClassCodeBox(
                    char = code.getOrNull(index)?.toString() ?: EMPTY_CODE_CHAR,
                    state = boxState
                )
            }
        }
    }
}

@Preview(name = "입력 전", showBackground = true)
@Composable
private fun ClassJoinCodeStepEmptyPreview() {
    PickeTheme {
        ClassJoinCodeStep(
            code = "",
            isCodeInvalid = false,
            onCodeChange = {}
        )
    }
}

@Preview(name = "입력 중", showBackground = true)
@Composable
private fun ClassJoinCodeStepPreview() {
    PickeTheme {
        ClassJoinCodeStep(
            code = DummyData.dummyClassCode,
            isCodeInvalid = false,
            onCodeChange = {}
        )
    }
}

@Preview(name = "잘못된 코드", showBackground = true)
@Composable
private fun ClassJoinCodeStepInvalidPreview() {
    PickeTheme {
        ClassJoinCodeStep(
            code = DummyData.dummyClassCode,
            isCodeInvalid = true,
            onCodeChange = {}
        )
    }
}