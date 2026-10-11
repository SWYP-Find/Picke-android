package com.picke.presentation.ui.classroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassBottomButtonDimens {
    val topPadding = 10.dp
}

@Composable
fun ClassBottomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = PickeTheme.colors.backgroundBeige,
    topPadding: Dp = ClassBottomButtonDimens.topPadding
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .padding(
                start = SpacingTokens.s16,
                top = topPadding,
                end = SpacingTokens.s16,
                bottom = SpacingTokens.s16
            )
    ) {
        CustomButton(
            text = text,
            onClick = onClick,
            modifier = Modifier.height(ComponentNumberTokens.buttonPrimaryLargeHeight),
            backgroundColor = if (enabled) {
                PickeTheme.colors.buttonPrimaryBackgroundDefault
            } else {
                PickeTheme.colors.buttonPrimaryBackgroundDisabled
            },
            textColor = PickeTheme.colors.buttonPrimaryTextDefault,
            enabled = enabled
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassBottomButtonPreview() {
    PickeTheme {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)) {
            ClassBottomButton(
                text = "클래스로 이동하기",
                onClick = {}
            )
            ClassBottomButton(
                text = "클래스 참여하기",
                onClick = {},
                enabled = false
            )
        }
    }
}