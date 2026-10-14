package com.picke.presentation.ui.classroom.classcreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassLevelTabDimens {
    val itemMinHeight = 36.dp
}

@Composable
fun ClassLevelTab(
    labels: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.backgroundSubtler, shape)
            .padding(SpacingTokens.s4)
    ) {
        labels.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            val backgroundColor =
                if (isSelected) PickeTheme.colors.surfaceBeigeDefault else Color.Transparent
            val textColor =
                if (isSelected) PickeTheme.colors.textPrimary else PickeTheme.colors.textMuted
            val textStyle =
                if (isSelected) PickeTheme.typography.bodySmSemiBold else PickeTheme.typography.bodySmRegular

            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = ClassLevelTabDimens.itemMinHeight)
                    .clip(shape)
                    .background(backgroundColor)
                    .clickable { onSelect(index) }
                    .padding(vertical = SpacingTokens.s8),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = textColor,
                    style = textStyle
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassLevelTabPreview() {
    PickeTheme {
        ClassLevelTab(
            labels = listOf("초급", "중급", "고급"),
            selectedIndex = 0,
            onSelect = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}