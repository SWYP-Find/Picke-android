package com.picke.presentation.ui.classroom

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.component.ClassShortcutCard
import com.picke.presentation.ui.classroom.model.ClassTicketDialog
import com.picke.presentation.ui.classroom.model.ClassTicketStatus
import com.picke.presentation.ui.classroom.model.ClassUiAction
import com.picke.presentation.ui.classroom.model.ClassUiEvent
import com.picke.presentation.ui.classroom.model.ClassUiState
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.dialog.CustomReverseConfirmDialog
import com.picke.presentation.ui.component.dialog.CustomSingleActionDialog
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private object ClassScreenDimens {
    val introTitleLetterSpacing = (-0.6).sp
    val introDescriptionLetterSpacing = (-0.4).sp
    val joinCardImageSize = 169.dp
    val joinCardImageEndOverflow = 69.dp
    val joinCardImageBottomOverflow = 54.dp
    val myClassCardImageSize = 130.dp
    val myClassCardImageEndOverflow = 6.dp
    val myClassCardImageBottomOverflow = 49.dp
    val ticketVerticalPadding = 10.dp
}

@Composable
fun ClassScreen(
    onBackClick: () -> Unit,
    onNavigateToJoin: () -> Unit,
    onNavigateToMyClass: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToTicket: () -> Unit,
    onNavigateToTicketRegister: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassUiEvent.NavigateBack -> onBackClick()

                ClassUiEvent.NavigateToJoin -> onNavigateToJoin()

                ClassUiEvent.NavigateToMyClass -> onNavigateToMyClass()

                ClassUiEvent.NavigateToCreate -> onNavigateToCreate()

                ClassUiEvent.NavigateToTicket -> onNavigateToTicket()

                ClassUiEvent.NavigateToTicketRegister -> onNavigateToTicketRegister()
            }
        }
    }

    ClassContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassContent(
    uiState: ClassUiState,
    onAction: (ClassUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundBeige)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_class_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            CustomTopAppBar(
                title = stringResource(R.string.class_title),
                onBackClick = { onAction(ClassUiAction.BackClick) },
                backgroundColor = Color.Transparent,
                backIconColor = PickeTheme.colors.textInverse,
                titleColor = PickeTheme.colors.textInverse
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(
                        start = SpacingTokens.s16,
                        top = SpacingTokens.s24,
                        end = SpacingTokens.s16,
                        bottom = SpacingTokens.s16
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ClassIntroSection()

                ClassMenuSection(
                    onJoinClick = { onAction(ClassUiAction.JoinClick) },
                    onMyClassClick = { onAction(ClassUiAction.MyClassClick) },
                    onCreateClick = { onAction(ClassUiAction.CreateClick) },
                    onTicketClick = { onAction(ClassUiAction.TicketClick) }
                )
            }
        }
    }

    val ticketDialog = uiState.ticketDialog
    if (ticketDialog != null) {
        when (ticketDialog) {
            ClassTicketDialog.NO_PERMISSION -> CustomSingleActionDialog(
                message = stringResource(ticketDialog.messageRes()),
                buttonText = stringResource(R.string.dialog_dismiss),
                onConfirm = { onAction(ClassUiAction.TicketDialogDismiss) },
                onDismiss = { onAction(ClassUiAction.TicketDialogDismiss) }
            )

            ClassTicketDialog.UNREGISTERED,
            ClassTicketDialog.EXPIRED -> CustomReverseConfirmDialog(
                message = stringResource(ticketDialog.messageRes()),
                onConfirm = { onAction(ClassUiAction.TicketDialogRegisterClick) },
                onDismiss = { onAction(ClassUiAction.TicketDialogDismiss) },
                confirmText = stringResource(R.string.ticket_register_dialog_register)
            )
        }
    }
}

@Composable
private fun ClassIntroSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.class_intro_title),
            color = PickeTheme.colors.textInverse,
            textAlign = TextAlign.Center,
            style = PickeTheme.typography.headingXl.copy(letterSpacing = ClassScreenDimens.introTitleLetterSpacing)
        )
        Text(
            text = stringResource(R.string.class_intro_description),
            color = PickeTheme.colors.textMuted,
            textAlign = TextAlign.Center,
            style = PickeTheme.typography.headingSm.copy(letterSpacing = ClassScreenDimens.introDescriptionLetterSpacing)
        )
    }
}

@Composable
private fun ClassMenuSection(
    onJoinClick: () -> Unit,
    onMyClassClick: () -> Unit,
    onCreateClick: () -> Unit,
    onTicketClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6)) {
            ClassShortcutCard(
                title = stringResource(R.string.class_join_action),
                description = stringResource(R.string.class_join_code_description),
                imageResId = R.drawable.img_class_join_code,
                imageSize = ClassScreenDimens.joinCardImageSize,
                imageEndOverflow = ClassScreenDimens.joinCardImageEndOverflow,
                imageBottomOverflow = ClassScreenDimens.joinCardImageBottomOverflow,
                onClick = onJoinClick,
                modifier = Modifier.weight(1f)
            )
            ClassShortcutCard(
                title = stringResource(R.string.my_class_title),
                description = stringResource(R.string.class_my_class_description),
                imageResId = R.drawable.img_class_my_desk,
                imageSize = ClassScreenDimens.myClassCardImageSize,
                imageEndOverflow = ClassScreenDimens.myClassCardImageEndOverflow,
                imageBottomOverflow = ClassScreenDimens.myClassCardImageBottomOverflow,
                onClick = onMyClassClick,
                modifier = Modifier.weight(1f)
            )
        }

        Column {
            CustomButton(
                text = stringResource(R.string.class_create_title),
                onClick = onCreateClick,
                backgroundColor = PickeTheme.colors.buttonPrimaryBackgroundDefault,
                textColor = PickeTheme.colors.buttonPrimaryTextDefault,
                minHeight = ComponentNumberTokens.buttonPrimaryLargeHeight
            )
            Text(
                text = stringResource(R.string.class_ticket_register),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onTicketClick)
                    .padding(vertical = ClassScreenDimens.ticketVerticalPadding),
                color = PickeTheme.colors.textMuted,
                textAlign = TextAlign.Center,
                style = PickeTheme.typography.bodyXxsMedium
            )
        }
    }
}

@StringRes
private fun ClassTicketDialog.messageRes(): Int = when (this) {
    ClassTicketDialog.NO_PERMISSION -> R.string.class_create_dialog_no_permission
    ClassTicketDialog.UNREGISTERED -> R.string.class_create_dialog_unregistered
    ClassTicketDialog.EXPIRED -> R.string.class_create_dialog_expired
}

@Preview(showBackground = true)
@Composable
private fun ClassContentPreview() {
    PickeTheme {
        ClassContent(
            uiState = ClassUiState(ticketStatus = DummyData.dummyClassTicketStatus),
            onAction = {}
        )
    }
}

@Preview(name = "생성 권한 없음", showBackground = true)
@Composable
private fun ClassContentNoPermissionPreview() {
    PickeTheme {
        ClassContent(
            uiState = ClassUiState(
                ticketStatus = ClassTicketStatus.NONE,
                ticketDialog = ClassTicketDialog.NO_PERMISSION
            ),
            onAction = {}
        )
    }
}

@Preview(name = "이용권 미등록", showBackground = true)
@Composable
private fun ClassContentUnregisteredPreview() {
    PickeTheme {
        ClassContent(
            uiState = ClassUiState(
                ticketStatus = ClassTicketStatus.UNREGISTERED,
                ticketDialog = ClassTicketDialog.UNREGISTERED
            ),
            onAction = {}
        )
    }
}

@Preview(name = "이용권 종료", showBackground = true)
@Composable
private fun ClassContentExpiredPreview() {
    PickeTheme {
        ClassContent(
            uiState = ClassUiState(
                ticketStatus = ClassTicketStatus.EXPIRED,
                ticketDialog = ClassTicketDialog.EXPIRED
            ),
            onAction = {}
        )
    }
}