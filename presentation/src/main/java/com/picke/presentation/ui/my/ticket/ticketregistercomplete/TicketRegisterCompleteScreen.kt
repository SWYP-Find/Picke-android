package com.picke.presentation.ui.my.ticket.ticketregistercomplete

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.my.ticket.component.TicketBottomAction
import com.picke.presentation.ui.my.ticket.component.TicketCard
import com.picke.presentation.ui.my.ticket.component.TicketHeadline
import com.picke.presentation.ui.my.ticket.ticketregistercomplete.model.TicketRegisterCompleteUiAction
import com.picke.presentation.ui.my.ticket.ticketregistercomplete.model.TicketRegisterCompleteUiEvent
import com.picke.presentation.ui.my.ticket.ticketregistercomplete.model.TicketRegisterCompleteUiState
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun TicketRegisterCompleteScreen(
    onBackClick: () -> Unit,
    onNavigateToTicket: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicketRegisterCompleteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val loadFailedMessage = stringResource(R.string.toast_load_failed)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                TicketRegisterCompleteUiEvent.NavigateBack -> onBackClick()

                TicketRegisterCompleteUiEvent.NavigateToTicket -> onNavigateToTicket()

                TicketRegisterCompleteUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    TicketRegisterCompleteContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun TicketRegisterCompleteContent(
    uiState: TicketRegisterCompleteUiState,
    onAction: (TicketRegisterCompleteUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundBeige)
    ) {
        CustomTopAppBar(
            title = stringResource(R.string.ticket_register_title),
            onBackClick = { onAction(TicketRegisterCompleteUiAction.BackClick) },
            backgroundColor = PickeTheme.colors.backgroundBeige,
            titleColor = PickeTheme.colors.textSubtler
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s20),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s40)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s32)) {
                TicketHeadline(
                    title = stringResource(R.string.ticket_register_complete_headline),
                    description = stringResource(R.string.ticket_register_complete_description),
                    isCentered = true
                )

                uiState.ticket?.let { ticket -> TicketCard(ticket = ticket) }
            }

            TicketBottomAction(
                caption = stringResource(R.string.ticket_check_in_my_page),
                buttonText = stringResource(R.string.ticket_register_complete_check),
                onClick = { onAction(TicketRegisterCompleteUiAction.TicketCheckClick) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketRegisterCompleteContentPreview() {
    PickeTheme {
        TicketRegisterCompleteContent(
            uiState = TicketRegisterCompleteUiState(
                ticketId = 1L,
                ticket = DummyData.dummyRegisteredTicket
            ),
            onAction = {}
        )
    }
}