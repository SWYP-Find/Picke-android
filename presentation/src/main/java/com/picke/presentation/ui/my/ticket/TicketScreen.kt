package com.picke.presentation.ui.my.ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.my.ticket.component.TicketBottomAction
import com.picke.presentation.ui.my.ticket.component.TicketCard
import com.picke.presentation.ui.my.ticket.model.TicketUiAction
import com.picke.presentation.ui.my.ticket.model.TicketUiEvent
import com.picke.presentation.ui.my.ticket.model.TicketUiModel
import com.picke.presentation.ui.my.ticket.model.TicketUiState
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun TicketScreen(
    onBackClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onReplaceWithRegister: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicketViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                TicketUiEvent.NavigateBack -> onBackClick()

                TicketUiEvent.NavigateToRegister -> onNavigateToRegister()

                TicketUiEvent.ReplaceWithRegister -> onReplaceWithRegister()
            }
        }
    }

    TicketContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun TicketContent(
    uiState: TicketUiState,
    onAction: (TicketUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundBeige)
    ) {
        CustomTopAppBar(
            title = stringResource(R.string.ticket_title),
            onBackClick = { onAction(TicketUiAction.BackClick) },
            backgroundColor = PickeTheme.colors.backgroundBeige,
            titleColor = PickeTheme.colors.textSubtler
        )

        if (!uiState.isLoading) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s20),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.s40)
            ) {
                TicketInUseSection(tickets = uiState.tickets)

                TicketBottomAction(
                    caption = stringResource(R.string.ticket_check_in_my_page),
                    buttonText = stringResource(R.string.ticket_add_register),
                    onClick = { onAction(TicketUiAction.AddRegisterClick) }
                )
            }
        }
    }
}

@Composable
private fun TicketInUseSection(
    tickets: List<TicketUiModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
    ) {
        Text(
            text = stringResource(R.string.ticket_in_use),
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.bodyMdSemiBold
        )

        tickets.forEach { ticket ->
            TicketCard(ticket = ticket)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TicketContentPreview() {
    PickeTheme {
        TicketContent(
            uiState = TicketUiState(
                isLoading = false,
                tickets = DummyData.dummyRegisteredTickets
            ),
            onAction = {}
        )
    }
}