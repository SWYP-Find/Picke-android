package com.picke.presentation.ui.classroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import java.time.LocalDateTime

@Composable
fun ClassDeadlineField(
    deadline: LocalDateTime?,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    val deadlineText = rememberClassDeadlineText(deadline)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ComponentNumberTokens.inputTextfieldHeight)
            .clip(shape)
            .background(
                if (isEnabled) {
                    PickeTheme.colors.inputTextfieldBackgroundDefault
                } else {
                    PickeTheme.colors.inputTextfieldBackgroundDisabled
                }
            )
            .border(
                width = ComponentNumberTokens.borderWidthRegular,
                color = if (isError) PickeTheme.colors.borderErrorDefault else PickeTheme.colors.inputTextfieldBorderDefault,
                shape = shape
            )
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            )
            .padding(
                horizontal = SpacingTokens.s8,
                vertical = SpacingTokens.s8
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        if (isEnabled && deadline != null) {
            Text(
                text = deadlineText,
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.bodySmMedium
            )
        } else {
            Text(
                text = stringResource(R.string.class_setting_deadline_placeholder),
                color = PickeTheme.colors.inputTextfieldTextDefault,
                style = PickeTheme.typography.bodyXsMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassDeadlineFieldPreview() {
    PickeTheme {
        ClassDeadlineField(
            deadline = LocalDateTime.of(2026, 11, 2, 14, 0),
            isEnabled = true,
            onClick = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}

@Preview(name = "오류", showBackground = true)
@Composable
private fun ClassDeadlineFieldErrorPreview() {
    PickeTheme {
        ClassDeadlineField(
            deadline = LocalDateTime.of(2026, 11, 2, 14, 0),
            isEnabled = true,
            onClick = {},
            modifier = Modifier.padding(SpacingTokens.s16),
            isError = true
        )
    }
}