package com.picke.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun PickeBadge(
    text: String,
    modifier: Modifier = Modifier,
    isMuted: Boolean = false,
    horizontalPadding: Dp = SpacingTokens.s6
) {
    val backgroundColor = if (isMuted) PickeTheme.colors.backgroundSubtler else PickeTheme.colors.badgeFilledBackground
    val textColor = if (isMuted) PickeTheme.colors.textMuted else PickeTheme.colors.badgeFilledText

    Text(
        text = text,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(RadiusTokens.default))
            .padding(horizontal = horizontalPadding, vertical = SpacingTokens.s2),
        color = textColor,
        style = PickeTheme.typography.bodyXxsSemiBold
    )
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
            PickeBadge(text = "종료", isMuted = true)
            PickeBadge(text = "운영자")
            PickeBadge(text = "나")
        }
    }
}