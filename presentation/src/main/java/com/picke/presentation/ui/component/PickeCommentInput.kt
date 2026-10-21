package com.picke.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PickeCommentInputDimens {
    val shadowElevation = 12.dp
    val inputMinHeight = 52.dp
    val sendButtonSize = 40.dp
}

@Composable
fun PickeCommentInput(
    textFieldState: TextFieldState,
    hintText: String,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    editingId: Long? = null
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val inputShape = RoundedCornerShape(RadiusTokens.default)

    LaunchedEffect(editingId) {
        if (editingId != null) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    // TODO: 토큰 동기화 - color/element/beige
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding(),
        color = PickeTheme.colors.beige600,
        shadowElevation = PickeCommentInputDimens.shadowElevation
    ) {
        Column {
            // TODO: 토큰 동기화 - color/border/beige-dark
            HorizontalDivider(
                thickness = ComponentNumberTokens.borderWidthRegular,
                color = PickeTheme.colors.beige700
            )

            Row(
                modifier = Modifier.padding(SpacingTokens.s16),
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = PickeCommentInputDimens.inputMinHeight)
                        .background(PickeTheme.colors.inputTextfieldBackgroundDefault, inputShape)
                        .border(
                            width = ComponentNumberTokens.borderWidthRegular,
                            color = PickeTheme.colors.inputTextfieldBorderDefault,
                            shape = inputShape
                        )
                        .padding(horizontal = SpacingTokens.s12, vertical = SpacingTokens.s12),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (textFieldState.text.isEmpty()) {
                        Text(
                            text = hintText,
                            color = PickeTheme.colors.textMuted,
                            style = PickeTheme.typography.bodyXsMedium
                        )
                    }

                    BasicTextField(
                        state = textFieldState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = PickeTheme.typography.bodyXsMedium.copy(color = PickeTheme.colors.textDefault),
                        lineLimits = TextFieldLineLimits.MultiLine()
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(bottom = SpacingTokens.s6)
                        .size(PickeCommentInputDimens.sendButtonSize)
                        .clip(CircleShape)
                        .background(PickeTheme.colors.buttonIconBackgroundDefault)
                        .clickable { onSubmit() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_send),
                        contentDescription = stringResource(R.string.comment_submit),
                        modifier = Modifier.size(ComponentNumberTokens.iconLg),
                        tint = PickeTheme.colors.iconGrayInverse
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PickeCommentInputPreview() {
    PickeTheme {
        PickeCommentInput(
            textFieldState = rememberTextFieldState(),
            hintText = "내용을 입력하세요",
            onSubmit = {}
        )
    }
}