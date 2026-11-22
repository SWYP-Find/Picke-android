package com.picke.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

enum class PickeBadgeColor {
    PRIMARY,
    SECONDARY,
    TERTIARY
}

@Composable
fun PickeBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: PickeBadgeColor = PickeBadgeColor.SECONDARY,
    horizontalPadding: Dp = SpacingTokens.s6,
    textStyle: TextStyle = PickeTheme.typography.bodyXxsSemiBold
) {
    val backgroundColor = color.backgroundColor()
    val textColor = color.textColor()

    Text(
        text = text,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(RadiusTokens.default))
            .padding(horizontal = horizontalPadding, vertical = SpacingTokens.s2),
        color = textColor,
        style = textStyle
    )
}

@Composable
private fun PickeBadgeColor.backgroundColor(): Color = when (this) {
    PickeBadgeColor.PRIMARY -> PickeTheme.colors.primary50
    PickeBadgeColor.SECONDARY -> PickeTheme.colors.badgeFilledBackground
    PickeBadgeColor.TERTIARY -> PickeTheme.colors.backgroundSubtler
}

@Composable
private fun PickeBadgeColor.textColor(): Color = when (this) {
    PickeBadgeColor.PRIMARY -> PickeTheme.colors.textPrimary
    PickeBadgeColor.SECONDARY -> PickeTheme.colors.badgeFilledText
    PickeBadgeColor.TERTIARY -> PickeTheme.colors.textMuted
}

@Preview(showBackground = true)
@Composable
private fun PickeBadgePreview() {
    PickeTheme {
        Row(
            modifier = Modifier.padding(SpacingTokens.s16),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
        ) {
            PickeBadge(text = "진행 중")
            PickeBadge(text = "종료", color = PickeBadgeColor.TERTIARY)
            PickeBadge(text = "중등", color = PickeBadgeColor.PRIMARY)
            PickeBadge(text = "운영자")
            PickeBadge(text = "나")
            PickeBadge(
                text = "변기는 변기다",
                horizontalPadding = SpacingTokens.s4,
                textStyle = PickeTheme.typography.bodyXxsMedium
            )
        }
    }
}