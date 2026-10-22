package com.picke.presentation.ui.my.ticket.ticketregister

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.picke.presentation.ui.component.dialog.CustomConfirmDialog
import com.picke.presentation.ui.component.dialog.CustomSingleActionDialog
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterDialog
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterStep
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiAction
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiEvent
import com.picke.presentation.ui.my.ticket.ticketregister.model.TicketRegisterUiState
import com.picke.presentation.ui.my.ticket.ticketregister.step.TicketRegisterCodeStep
import com.picke.presentation.ui.my.ticket.ticketregister.step.TicketRegisterConfirmStep
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun TicketRegisterScreen(
    onBackClick: () -> Unit,
    onNavigateToRegisterComplete: (ticketId: Long) -> Unit,
    onNavigateToTicket: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicketRegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { viewModel.onAction(TicketRegisterUiAction.BackClick) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                TicketRegisterUiEvent.NavigateBack -> onBackClick()

                is TicketRegisterUiEvent.NavigateToRegisterComplete ->
                    onNavigateToRegisterComplete(event.ticketId)

                TicketRegisterUiEvent.NavigateToTicket -> onNavigateToTicket()
            }
        }
    }

    TicketRegisterContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun TicketRegisterContent(
    uiState: TicketRegisterUiState,
    onAction: (TicketRegisterUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundBeige)
    ) {
        CustomTopAppBar(
            title = stringResource(R.string.ticket_register_title),
            onBackClick = { onAction(TicketRegisterUiAction.BackClick) },
            backgroundColor = PickeTheme.colors.backgroundBeige,
            titleColor = PickeTheme.colors.textSubtler
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s20)
        ) {
            when (uiState.step) {
                TicketRegisterStep.CODE -> TicketRegisterCodeStep(
                    code = uiState.code,
                    isCodeCheckEnabled = uiState.isCodeCheckEnabled,
                    onCodeChange = { code -> onAction(TicketRegisterUiAction.CodeChange(code)) },
                    onCodeCheckClick = { onAction(TicketRegisterUiAction.CodeCheckClick) }
                )

                TicketRegisterStep.CONFIRM -> uiState.ticket?.let { ticket ->
                    TicketRegisterConfirmStep(
                        ticket = ticket,
                        onRegisterClick = { onAction(TicketRegisterUiAction.RegisterClick) }
                    )
                }
            }
        }
    }

    val dialog = uiState.dialog
    if (dialog != null) {
        when (dialog) {
            TicketRegisterDialog.ALREADY_REGISTERED -> CustomConfirmDialog(
                message = stringResource(dialog.messageRes()),
                onConfirm = { onAction(TicketRegisterUiAction.DialogBackClick) },
                onDismiss = { onAction(TicketRegisterUiAction.DialogDismiss) },
                confirmText = stringResource(R.string.dialog_dismiss),
                dismissText = stringResource(R.string.ticket_register_dialog_register)
            )

            TicketRegisterDialog.INVALID_CODE,
            TicketRegisterDialog.REGISTER_FAILED,
            TicketRegisterDialog.EXPIRED -> CustomSingleActionDialog(
                message = stringResource(dialog.messageRes()),
                buttonText = stringResource(R.string.ticket_register_dialog_confirm),
                onConfirm = { onAction(TicketRegisterUiAction.DialogDismiss) },
                onDismiss = { onAction(TicketRegisterUiAction.DialogDismiss) }
            )
        }
    }
}

@StringRes
private fun TicketRegisterDialog.messageRes(): Int = when (this) {
    TicketRegisterDialog.ALREADY_REGISTERED -> R.string.ticket_register_dialog_already_registered
    TicketRegisterDialog.INVALID_CODE -> R.string.ticket_register_dialog_invalid_code
    TicketRegisterDialog.REGISTER_FAILED -> R.string.ticket_register_dialog_register_failed
    TicketRegisterDialog.EXPIRED -> R.string.ticket_register_dialog_expired
}

@Preview(name = "코드 입력", showBackground = true)
@Composable
private fun TicketRegisterContentCodePreview() {
    PickeTheme {
        TicketRegisterContent(
            uiState = TicketRegisterUiState(code = "PICKE-30-DEMO"),
            onAction = {}
        )
    }
}

@Preview(name = "코드 확인", showBackground = true)
@Composable
private fun TicketRegisterContentConfirmPreview() {
    PickeTheme {
        TicketRegisterContent(
            uiState = TicketRegisterUiState(
                step = TicketRegisterStep.CONFIRM,
                code = "PICKE-30-DEMO",
                ticket = DummyData.dummyTicket
            ),
            onAction = {}
        )
    }
}

@Preview(name = "이미 등록된 이용권", showBackground = true)
@Composable
private fun TicketRegisterContentAlreadyRegisteredPreview() {
    PickeTheme {
        TicketRegisterContent(
            uiState = TicketRegisterUiState(
                code = "PICKE-30-DEMO",
                dialog = TicketRegisterDialog.ALREADY_REGISTERED
            ),
            onAction = {}
        )
    }
}

@Preview(name = "코드 틀림", showBackground = true)
@Composable
private fun TicketRegisterContentInvalidCodePreview() {
    PickeTheme {
        TicketRegisterContent(
            uiState = TicketRegisterUiState(
                code = "PICKE-30-DEMO",
                dialog = TicketRegisterDialog.INVALID_CODE
            ),
            onAction = {}
        )
    }
}