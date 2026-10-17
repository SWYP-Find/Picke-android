package com.picke.presentation.ui.my.ticket.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun TicketHeadline(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    isCentered: Boolean = false,
    spacing: Dp = SpacingTokens.s6
) {
    val textAlign = if (isCentered) TextAlign.Center else TextAlign.Start

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalAlignment = if (isCentered) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Text(
            text = title,
            color = PickeTheme.colors.textDefault,
            textAlign = textAlign,
            style = PickeTheme.typography.headingXl
        )
        Text(
            text = description,
            color = PickeTheme.colors.textMuted,
            textAlign = textAlign,
            style = PickeTheme.typography.bodyMdMedium
        )
    }
}

@Preview(name = "왼쪽 정렬", showBackground = true)
@Composable
private fun TicketHeadlinePreview() {
    PickeTheme {
        TicketHeadline(
            title = "클래스 이용권을 등록하세요",
            description = "이메일을 통해 리워드로 받은 코드를 입력해 주세요\n등록 전에 이용권 내용을 확인할 수 있어요"
        )
    }
}

@Preview(name = "가운데 정렬", showBackground = true)
@Composable
private fun TicketHeadlineCenteredPreview() {
    PickeTheme {
        TicketHeadline(
            title = "이용권이 등록됐어요",
            description = "이제 우리만의 클래스를 열고\n사람들을 초대해보세요",
            isCentered = true
        )
    }
}