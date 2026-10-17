package com.picke.presentation.ui.my.ticket.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.my.ticket.model.TicketUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.formatDate

@Composable
fun TicketCard(
    ticket: TicketUiModel,
    modifier: Modifier = Modifier
) {
    val period = if (ticket.startDate != null && ticket.endDate != null) {
        stringResource(
            R.string.ticket_info_period_range,
            formatDate(ticket.startDate),
            formatDate(ticket.endDate)
        )
    } else {
        stringResource(R.string.ticket_info_period_from_registration, ticket.durationMonths)
    }
    val shape = RoundedCornerShape(RadiusTokens.default)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .border(
                ComponentNumberTokens.borderWidthRegular,
                PickeTheme.colors.cardBaseBorderDefault,
                shape
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PickeTheme.colors.surfaceBeigeStrong)
                .padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_nav_classroom),
                    contentDescription = null,
                    modifier = Modifier.size(ComponentNumberTokens.iconLg),
                    tint = PickeTheme.colors.textDefault
                )
                Text(
                    text = ticket.name,
                    color = PickeTheme.colors.textDefault,
                    style = PickeTheme.typography.bodyLgBold
                )
            }

            Text(
                text = ticket.description,
                color = PickeTheme.colors.textPrimary,
                style = PickeTheme.typography.bodyXxsRegular
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PickeTheme.colors.cardBaseBackgroundDefault)
                .padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
        ) {
            TicketInfoRow(
                label = stringResource(R.string.ticket_info_member),
                value = stringResource(R.string.ticket_info_member_value, ticket.maxMemberCount)
            )
            TicketInfoRow(
                label = stringResource(R.string.ticket_info_period),
                value = period
            )
            TicketInfoRow(
                label = stringResource(R.string.ticket_info_class_creation),
                value = stringResource(R.string.ticket_info_class_creation_value)
            )
        }
    }
}

@Composable
private fun TicketInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = PickeTheme.colors.textMuted,
            style = PickeTheme.typography.bodyXxsRegular
        )
        Text(
            text = value,
            color = PickeTheme.colors.textBody,
            style = PickeTheme.typography.bodyXxsSemiBold
        )
    }
}

@Preview(name = "등록 전", showBackground = true)
@Composable
private fun TicketCardPreview() {
    PickeTheme {
        TicketCard(ticket = DummyData.dummyTicket)
    }
}

@Preview(name = "등록 후", showBackground = true)
@Composable
private fun TicketCardRegisteredPreview() {
    PickeTheme {
        TicketCard(ticket = DummyData.dummyRegisteredTicket)
    }
}