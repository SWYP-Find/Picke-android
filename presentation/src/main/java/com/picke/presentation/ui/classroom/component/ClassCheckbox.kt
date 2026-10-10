package com.picke.presentation.ui.classroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassCheckbox(
    isChecked: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = ComponentNumberTokens.checkboxWidth
) {
    val backgroundColor =
        if (isChecked) PickeTheme.colors.checkboxBackgroundSelected else PickeTheme.colors.checkboxBackgroundDefault
    val borderColor =
        if (isChecked) PickeTheme.colors.checkboxBorderSelected else PickeTheme.colors.checkboxBorderDefault

    Box(
        modifier = modifier
            .size(size)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .border(
                width = ComponentNumberTokens.borderWidthRegular,
                color = borderColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isChecked) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                modifier = Modifier.size(size / 2),
                tint = PickeTheme.colors.textInverse
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassCheckboxPreview() {
    PickeTheme {
        Row(
            modifier = Modifier.padding(SpacingTokens.s16),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
        ) {
            ClassCheckbox(isChecked = true)
            ClassCheckbox(isChecked = false)
        }
    }
}