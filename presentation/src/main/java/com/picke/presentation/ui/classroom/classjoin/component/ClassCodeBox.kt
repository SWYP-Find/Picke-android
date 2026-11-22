package com.picke.presentation.ui.classroom.classjoin.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.classroom.classjoin.model.ClassCodeBoxState
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassCodeBoxDimens {
    val boxSize = 52.dp
    val borderWidth = 2.dp
}

@Composable
fun ClassCodeBox(
    char: String,
    state: ClassCodeBoxState,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    val backgroundColor = when (state) {
        ClassCodeBoxState.FILLED -> PickeTheme.colors.backgroundSubtle
        ClassCodeBoxState.ACTIVE, ClassCodeBoxState.ERROR, ClassCodeBoxState.EMPTY -> PickeTheme.colors.surfaceBeigeDefault
    }
    val borderColor = when (state) {
        ClassCodeBoxState.ACTIVE -> PickeTheme.colors.secondary300
        ClassCodeBoxState.ERROR -> PickeTheme.colors.borderErrorDefault
        ClassCodeBoxState.FILLED, ClassCodeBoxState.EMPTY -> PickeTheme.colors.borderGraySubtle
    }
    val textColor = when (state) {
        ClassCodeBoxState.FILLED -> PickeTheme.colors.textSubtler
        ClassCodeBoxState.ACTIVE -> PickeTheme.colors.textDefault
        ClassCodeBoxState.ERROR -> PickeTheme.colors.textError
        ClassCodeBoxState.EMPTY -> PickeTheme.colors.textMuted
    }

    Box(
        modifier = modifier
            .size(ClassCodeBoxDimens.boxSize)
            .background(backgroundColor, shape)
            .border(ClassCodeBoxDimens.borderWidth, borderColor, shape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            color = textColor,
            style = PickeTheme.typography.bodyLgRegular
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassCodeBoxPreview() {
    PickeTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
            ClassCodeBox(char = "A", state = ClassCodeBoxState.FILLED)
            ClassCodeBox(char = "A", state = ClassCodeBoxState.ACTIVE)
            ClassCodeBox(char = "A", state = ClassCodeBoxState.ERROR)
            ClassCodeBox(char = "-", state = ClassCodeBoxState.EMPTY)
        }
    }
}