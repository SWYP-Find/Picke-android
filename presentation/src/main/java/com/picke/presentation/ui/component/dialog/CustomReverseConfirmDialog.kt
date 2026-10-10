package com.picke.presentation.ui.component.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object CustomReverseConfirmDialogDimens {
    val messageLineHeight = 24.sp
}

@Composable
fun CustomReverseConfirmDialog(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    confirmText: String = stringResource(R.string.dialog_confirm),
    dismissText: String = stringResource(R.string.dialog_dismiss)
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
                        .padding(SpacingTokens.s24),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (title != null) {
                        Text(
                            text = title,
                            color = pointColor,
                            textAlign = TextAlign.Center,
                            style = PickeTheme.typography.bodyLgBold
                        )
                        Spacer(modifier = Modifier.height(SpacingTokens.s12))
                    }
                    Text(
                        text = message,
                        color = pointColor,
                        textAlign = TextAlign.Center,
                        lineHeight = CustomReverseConfirmDialogDimens.messageLineHeight,
                        style = if (title != null) PickeTheme.typography.bodySmRegular else PickeTheme.typography.bodySmSemiBold
                    )
                }

                HorizontalDivider(thickness = ComponentNumberTokens.borderWidthRegular, color = pointColor)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onDismiss)
                            .padding(vertical = SpacingTokens.s16),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dismissText,
                            color = pointColor,
                            style = PickeTheme.typography.bodySmSemiBold
                        )
                    }

                    VerticalDivider(thickness = ComponentNumberTokens.borderWidthRegular, color = pointColor)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(pointColor)
                            .clickable(onClick = onConfirm)
                            .padding(vertical = SpacingTokens.s16),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = confirmText,
                            color = PickeTheme.colors.textInverse,
                            style = PickeTheme.typography.bodySmSemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomReverseConfirmDialogPreview() {
    PickeTheme {
        CustomReverseConfirmDialog(
            message = "-30P를 사용해 원하는 주제를 제안하고,\n채택되면 +100P를 돌려받아요.",
            onConfirm = {},
            onDismiss = {},
            title = "나만의 배틀을 제안해주세요",
            confirmText = "제안하기"
        )
    }
}