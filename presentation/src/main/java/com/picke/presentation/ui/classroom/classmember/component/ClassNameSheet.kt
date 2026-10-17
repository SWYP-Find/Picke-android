package com.picke.presentation.ui.classroom.classmember.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.component.ClassBottomSheet
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomTextField
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassNameSheetDimens {
    val titleLetterSpacing = (-0.6).sp
    val textFieldMinHeight = 52.dp
}

@Composable
fun ClassNameSheet(
    name: String,
    onNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ClassBottomSheet(onDismiss = onDismiss, modifier = modifier) {
        ClassNameSheetContent(
            name = name,
            onNameChange = onNameChange,
            onConfirm = onConfirm
        )
    }
}

@Composable
private fun ClassNameSheetContent(
    name: String,
    onNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConfirmEnabled = name.isNotBlank()
    val backgroundColor =
        if (isConfirmEnabled) PickeTheme.colors.buttonPrimaryBackgroundDefault else PickeTheme.colors.buttonPrimaryBackgroundDisabled

    Column(
        modifier = modifier.padding(top = SpacingTokens.s20, bottom = SpacingTokens.s16),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s20)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)) {
            Text(
                text = stringResource(R.string.class_name_sheet_title),
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.headingXl.copy(letterSpacing = ClassNameSheetDimens.titleLetterSpacing)
            )
            Text(
                text = stringResource(R.string.class_name_sheet_edit_description),
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.bodySmRegular
            )
        }

        CustomTextField(
            value = name,
            onValueChange = onNameChange,
            minHeight = ClassNameSheetDimens.textFieldMinHeight,
            textStyle = PickeTheme.typography.bodyLgMedium,
            placeholder = stringResource(R.string.class_name_sheet_title),
            horizontalPadding = SpacingTokens.s12
        )

        CustomButton(
            text = stringResource(R.string.class_name_sheet_edit),
            onClick = onConfirm,
            backgroundColor = backgroundColor,
            textColor = PickeTheme.colors.buttonPrimaryTextDefault,
            enabled = isConfirmEnabled,
            minHeight = ComponentNumberTokens.buttonPrimaryLargeHeight
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassNameSheetContentPreview() {
    PickeTheme {
        Column(modifier = Modifier.background(PickeTheme.colors.surfaceBeigeDefault)) {
            ClassNameSheetContent(
                name = "권동현",
                onNameChange = {},
                onConfirm = {}
            )
        }
    }
}