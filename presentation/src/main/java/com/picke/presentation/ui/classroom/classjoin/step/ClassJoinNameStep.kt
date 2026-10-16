package com.picke.presentation.ui.classroom.classjoin.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiState
import com.picke.presentation.ui.classroom.component.ClassTextField
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassJoinNameStepDimens {
    val nameFieldMinHeight = 52.dp
}

@Composable
internal fun ClassJoinNameStep(
    name: String,
    isNameDuplicated: Boolean,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
    ) {
        ClassTextField(
            value = name,
            onValueChange = onNameChange,
            minHeight = ClassJoinNameStepDimens.nameFieldMinHeight,
            textStyle = PickeTheme.typography.bodyXsMedium,
            placeholder = stringResource(R.string.class_name_sheet_title),
            isError = isNameDuplicated
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isNameDuplicated) stringResource(R.string.class_join_name_duplicated) else "",
                modifier = Modifier.weight(1f),
                color = PickeTheme.colors.textError,
                style = PickeTheme.typography.captionLgMedium
            )
            Text(
                text = stringResource(
                    R.string.class_join_name_count,
                    name.length,
                    ClassJoinUiState.NAME_MAX_LENGTH
                ),
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.captionLgMedium
            )
        }
    }
}

@Preview(name = "입력 중", showBackground = true)
@Composable
private fun ClassJoinNameStepPreview() {
    PickeTheme {
        ClassJoinNameStep(
            name = "권동현",
            isNameDuplicated = false,
            onNameChange = {}
        )
    }
}

@Preview(name = "이름 중복", showBackground = true)
@Composable
private fun ClassJoinNameStepDuplicatedPreview() {
    PickeTheme {
        ClassJoinNameStep(
            name = "권동현",
            isNameDuplicated = true,
            onNameChange = {}
        )
    }
}