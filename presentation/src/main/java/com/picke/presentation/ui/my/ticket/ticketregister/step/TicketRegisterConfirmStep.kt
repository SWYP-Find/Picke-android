package com.picke.presentation.ui.my.ticket.ticketregister.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.my.ticket.component.TicketBottomAction
import com.picke.presentation.ui.my.ticket.component.TicketCard
import com.picke.presentation.ui.my.ticket.component.TicketHeadline
import com.picke.presentation.ui.my.ticket.model.TicketUiModel
import com.picke.presentation.ui.my.ticket.ticketregister.component.TicketRegisterNoticeCard
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
internal fun TicketRegisterConfirmStep(
    ticket: TicketUiModel,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s40)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s32)) {
            TicketHeadline(
                title = stringResource(R.string.ticket_register_confirm_headline),
                description = stringResource(R.string.ticket_register_confirm_description),
                spacing = SpacingTokens.s8
            )

            TicketCard(ticket = ticket)

            TicketRegisterNoticeCard(message = stringResource(R.string.ticket_register_notice))
        }

        TicketBottomAction(
            caption = stringResource(R.string.ticket_check_in_my_page),
            buttonText = stringResource(R.string.ticket_register_action),
            onClick = onRegisterClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketRegisterConfirmStepPreview() {
    PickeTheme {
        TicketRegisterConfirmStep(
            ticket = DummyData.dummyTicket,
            onRegisterClick = {}
        )
    }
}