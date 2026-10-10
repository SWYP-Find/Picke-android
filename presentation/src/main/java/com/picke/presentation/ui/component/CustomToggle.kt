package com.picke.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun CustomToggle(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = ComponentNumberTokens.toggleWidth,
    height: Dp = ComponentNumberTokens.toggleHeight
) {
    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(RoundedCornerShape(RadiusTokens.max))
            .background(if (isChecked) PickeTheme.colors.toggleTrackOn else PickeTheme.colors.toggleTrackOff)
            .toggleable(value = isChecked, role = Role.Switch, onValueChange = onCheckedChange)
            .padding(SpacingTokens.s2),
        contentAlignment = if (isChecked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(ComponentNumberTokens.toggleThumbSize)
                .background(PickeTheme.colors.toggleThumbDefault, CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomTogglePreview() {
    PickeTheme {
        Row(
            modifier = Modifier.padding(SpacingTokens.s16),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
        ) {
            CustomToggle(isChecked = true, onCheckedChange = {})
            CustomToggle(isChecked = false, onCheckedChange = {})
        }
    }
}