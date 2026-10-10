package com.picke.presentation.ui.classroom.classcreate.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassPillBadge(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconResId: Int? = null,
    textStyle: TextStyle = PickeTheme.typography.bodyXxsSemiBold,
    onClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(RadiusTokens.max)

    Row(
        modifier = modifier
            .clip(shape)
            .background(PickeTheme.colors.primary50)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(
                horizontal = SpacingTokens.s8,
                vertical = SpacingTokens.s2
            ),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = PickeTheme.colors.textPrimary,
            style = textStyle
        )
        if (iconResId != null) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(ComponentNumberTokens.iconSm),
                tint = PickeTheme.colors.textPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassPillBadgePreview() {
    PickeTheme {
        Row(
            modifier = Modifier.padding(SpacingTokens.s16),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
        ) {
            ClassPillBadge(
                text = "TTS 포함",
                iconResId = R.drawable.ic_info,
                textStyle = PickeTheme.typography.captionLgBold
            )
            ClassPillBadge(text = "중급")
        }
    }
}