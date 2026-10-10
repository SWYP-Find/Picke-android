package com.picke.presentation.ui.classroom.classcreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassOptionChipDimens {
    val minHeight = 44.dp
}

@Composable
fun ClassOptionChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    val backgroundColor =
        if (isSelected) PickeTheme.colors.surfacePrimarySubtle else PickeTheme.colors.surfaceBeigeDefault
    val borderColor =
        if (isSelected) PickeTheme.colors.borderPrimaryDefault else PickeTheme.colors.borderBeigeDefault
    val textColor =
        if (isSelected) PickeTheme.colors.textPrimary else PickeTheme.colors.textMuted
    val textStyle =
        if (isSelected) PickeTheme.typography.bodySmSemiBold else PickeTheme.typography.bodySmRegular

    Box(
        modifier = modifier
            .heightIn(min = ClassOptionChipDimens.minHeight)
            .clip(shape)
            .background(backgroundColor)
            .border(ComponentNumberTokens.borderWidthRegular, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(vertical = SpacingTokens.s8),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassOptionChipPreview() {
    PickeTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
            ClassOptionChip(
                text = "중등",
                isSelected = true,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            ClassOptionChip(
                text = "고등",
                isSelected = false,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}