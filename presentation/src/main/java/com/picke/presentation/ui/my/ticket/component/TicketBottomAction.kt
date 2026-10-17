package com.picke.presentation.ui.my.ticket.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun TicketBottomAction(
    caption: String,
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = caption,
            color = PickeTheme.colors.textMuted,
            style = PickeTheme.typography.bodyXxsRegular
        )
        CustomButton(
            text = buttonText,
            onClick = onClick,
            backgroundColor = PickeTheme.colors.buttonPrimaryBackgroundDefault,
            textColor = PickeTheme.colors.buttonPrimaryTextDefault,
            minHeight = ComponentNumberTokens.buttonPrimaryLargeHeight
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketBottomActionPreview() {
    PickeTheme {
        TicketBottomAction(
            caption = "등록 후 마이페이지에서 이용권을 확인할 수 있어요",
            buttonText = "이용권 등록하기",
            onClick = {}
        )
    }
}