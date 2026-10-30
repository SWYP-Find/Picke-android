package com.picke.presentation.ui.classroom.classcreate.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.picke.presentation.ui.component.CustomToggle
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassSettingToggle(
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    toggleWidth: Dp = ComponentNumberTokens.toggleWidth,
    toggleHeight: Dp = ComponentNumberTokens.toggleHeight
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s2)
        ) {
            Text(
                text = title,
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.bodyMdSemiBold
            )
            Text(
                text = description,
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.captionLgRegular
            )
        }

        CustomToggle(
            isChecked = isChecked,
            onCheckedChange = onCheckedChange,
            width = toggleWidth,
            height = toggleHeight
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassSettingTogglePreview() {
    PickeTheme {
        ClassSettingToggle(
            title = "댓글 필수",
            description = "댓글을 남겨야 결과를 볼 수 있어요",
            isChecked = true,
            onCheckedChange = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}