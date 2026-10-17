package com.picke.presentation.ui.my.ticket.ticketregister.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun TicketRegisterNoticeCard(
    message: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    // TODO: 토큰 동기화 - border/primary-light
    val borderColor = PickeTheme.colors.primary100

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(PickeTheme.colors.surfacePrimarySubtle)
            .border(ComponentNumberTokens.borderWidthRegular, borderColor, shape)
            .padding(SpacingTokens.s12),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_megaphone),
            contentDescription = null,
            modifier = Modifier.size(ComponentNumberTokens.iconLg),
            tint = PickeTheme.colors.textPrimary
        )
        Text(
            text = message,
            modifier = Modifier.weight(1f),
            color = PickeTheme.colors.textPrimary,
            style = PickeTheme.typography.bodyXxsRegular
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketRegisterNoticeCardPreview() {
    PickeTheme {
        TicketRegisterNoticeCard(message = "등록하는 순간 이용 기간이 시작돼요\n클래스를 만들기 전에도 기간은 흘러가요")
    }
}