package com.picke.presentation.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.formatCount

@Composable
fun PickeIconCount(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    count: Int,
    modifier: Modifier = Modifier,
    contentColor: Color = PickeTheme.colors.textMuted,
    onClick: (() -> Unit)? = null
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier.then(clickModifier),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = contentColor
        )
        Text(
            text = formatCount(count),
            color = contentColor,
            style = PickeTheme.typography.bodyXxsMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PickeIconCountPreview() {
    PickeTheme {
        Row(
            modifier = Modifier.padding(SpacingTokens.s16),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
        ) {
            PickeIconCount(
                iconRes = R.drawable.ic_heart_plus,
                contentDescription = "좋아요",
                count = 1340
            )
            PickeIconCount(
                iconRes = R.drawable.ic_heart_plus,
                contentDescription = "좋아요",
                count = 12,
                contentColor = PickeTheme.colors.primary500
            )
            PickeIconCount(
                iconRes = R.drawable.ic_message,
                contentDescription = "댓글",
                count = 23
            )
        }
    }
}