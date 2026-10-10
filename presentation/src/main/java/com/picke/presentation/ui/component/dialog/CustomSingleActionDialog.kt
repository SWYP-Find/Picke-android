package com.picke.presentation.ui.component.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object CustomSingleActionDialogDimens {
    val messageLineHeight = 24.sp
}

@Composable
fun CustomSingleActionDialog(
    message: String,
    buttonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    subMessage: String? = null
) {
    val pointColor = PickeTheme.colors.primary500

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(RadiusTokens.default),
            color = PickeTheme.colors.borderBeigeDisabled,
            border = BorderStroke(ComponentNumberTokens.borderWidthRegular, pointColor)
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SpacingTokens.s24, vertical = SpacingTokens.s32),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = message,
                        color = pointColor,
                        textAlign = TextAlign.Center,
                        lineHeight = CustomSingleActionDialogDimens.messageLineHeight,
                        style = PickeTheme.typography.headingSm
                    )

                    if (subMessage != null) {
                        Spacer(modifier = Modifier.height(SpacingTokens.s8))
                        Text(
                            text = subMessage,
                            color = pointColor,
                            textAlign = TextAlign.Center,
                            style = PickeTheme.typography.bodyXsRegular
                        )
                    }
                }

                HorizontalDivider(thickness = ComponentNumberTokens.borderWidthRegular, color = pointColor)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(pointColor)
                        .clickable(onClick = onConfirm)
                        .padding(vertical = SpacingTokens.s16),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = buttonText,
                        color = PickeTheme.colors.textInverse,
                        style = PickeTheme.typography.bodySmSemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomSingleActionDialogPreview() {
    PickeTheme {
        CustomSingleActionDialog(
            message = "컨텐츠를 시청하기 위한\n포인트가 부족해요!",
            buttonText = "배틀 주제 구경하러 가기",
            onConfirm = {},
            onDismiss = {},
            subMessage = "매일 출석체크만 해도 5P를 받을 수 있어요!"
        )
    }
}