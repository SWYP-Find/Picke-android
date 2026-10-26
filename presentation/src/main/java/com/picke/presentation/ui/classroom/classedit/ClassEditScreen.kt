package com.picke.presentation.ui.classroom.classedit

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classedit.model.ClassEditUiAction
import com.picke.presentation.ui.classroom.classedit.model.ClassEditUiEvent
import com.picke.presentation.ui.classroom.classedit.model.ClassEditUiState
import com.picke.presentation.ui.classroom.component.ClassBattleCard
import com.picke.presentation.ui.classroom.component.ClassBottomButton
import com.picke.presentation.ui.classroom.component.ClassDeadlineField
import com.picke.presentation.ui.classroom.component.ClassSettingErrorText
import com.picke.presentation.ui.classroom.component.ClassTitledBlock
import com.picke.presentation.ui.classroom.component.deadline.ClassDeadlineSheet
import com.picke.presentation.ui.component.CustomTextField
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ClassEditScreen(
    onBackClick: () -> Unit,
    onClassUpdated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val loadFailedMessage = stringResource(R.string.toast_load_failed)
    val actionFailedMessage = stringResource(R.string.toast_action_failed)

    BackHandler { viewModel.onAction(ClassEditUiAction.BackClick) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassEditUiEvent.NavigateBack -> onBackClick()

                ClassEditUiEvent.NavigateBackAfterUpdated -> onClassUpdated()

                ClassEditUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()

                ClassEditUiEvent.ShowActionFailedToast ->
                    Toast.makeText(context, actionFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ClassEditContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassEditContent(
    uiState: ClassEditUiState,
    onAction: (ClassEditUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .background(PickeTheme.colors.backgroundBeige)
            .systemBarsPadding(),
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.class_edit_title),
                onBackClick = { onAction(ClassEditUiAction.BackClick) },
                backgroundColor = PickeTheme.colors.backgroundBeige,
                titleColor = PickeTheme.colors.textSubtler
            )
        },
        bottomBar = {
            ClassBottomButton(
                text = stringResource(R.string.class_edit_save),
                onClick = { onAction(ClassEditUiAction.SaveClick) },
                enabled = uiState.isSaveEnabled,
                topPadding = SpacingTokens.s0
            )
        },
        containerColor = PickeTheme.colors.backgroundBeige,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(SpacingTokens.s16)
        ) {
            Text(
                text = stringResource(R.string.class_edit_headline),
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.headingXl
            )

            Spacer(modifier = Modifier.height(SpacingTokens.s32))

            ClassEditFormSection(
                uiState = uiState,
                onClassNameChange = { name -> onAction(ClassEditUiAction.ClassNameChange(name)) },
                onDeadlineClick = { onAction(ClassEditUiAction.DeadlineFieldClick) }
            )
        }
    }

    val deadline = uiState.deadline
    if (uiState.isDeadlineSheetVisible && deadline != null) {
        ClassDeadlineSheet(
            deadline = deadline,
            onConfirm = { selected -> onAction(ClassEditUiAction.DeadlineConfirm(selected)) },
            onDismiss = { onAction(ClassEditUiAction.DeadlineSheetDismiss) }
        )
    }
}

@Composable
private fun ClassEditFormSection(
    uiState: ClassEditUiState,
    onClassNameChange: (String) -> Unit,
    onDeadlineClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s32)
    ) {
        ClassTitledBlock(
            title = stringResource(R.string.class_setting_name_label),
            isRequired = true,
            contentSpacing = SpacingTokens.s8
        ) {
            Column {
                CustomTextField(
                    value = uiState.className,
                    onValueChange = onClassNameChange,
                    minHeight = ComponentNumberTokens.inputTextfieldHeight,
                    textStyle = PickeTheme.typography.bodyXsMedium,
                    placeholder = stringResource(R.string.class_setting_name_placeholder),
                    isError = uiState.isClassNameInvalid
                )
                ClassSettingErrorText(
                    text = stringResource(R.string.class_setting_name_blank_error),
                    isVisible = uiState.isClassNameInvalid
                )
            }
        }

        ClassTitledBlock(
            title = stringResource(R.string.class_setting_deadline_label),
            contentSpacing = SpacingTokens.s8
        ) {
            Column {
                ClassDeadlineField(
                    deadline = uiState.deadline,
                    isEnabled = true,
                    onClick = onDeadlineClick,
                    isError = uiState.isDeadlineOutOfRange
                )
                ClassSettingErrorText(
                    text = stringResource(R.string.class_edit_deadline_range_error),
                    isVisible = uiState.isDeadlineOutOfRange
                )
            }
        }

        uiState.battle?.let { battle ->
            ClassTitledBlock(title = stringResource(R.string.class_setting_selected_battle)) {
                ClassBattleCard(battle = battle)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassEditContentPreview() {
    PickeTheme {
        ClassEditContent(
            uiState = ClassEditUiState(
                originalClassName = "3학년 2반 철학 토론",
                className = "3학년 2반 철학 토론",
                originalDeadline = LocalDateTime.of(2026, 11, 2, 14, 0),
                deadline = LocalDateTime.of(2026, 11, 2, 14, 0),
                ticketEndDate = LocalDate.of(2027, 4, 1),
                battle = DummyData.dummyClassBattles[1]
            ),
            onAction = {}
        )
    }
}

@Preview(name = "입력 오류", showBackground = true)
@Composable
private fun ClassEditContentErrorPreview() {
    PickeTheme {
        ClassEditContent(
            uiState = ClassEditUiState(
                className = " ",
                isClassNameInvalid = true,
                originalDeadline = LocalDateTime.of(2026, 11, 2, 14, 0),
                deadline = LocalDateTime.of(2026, 10, 30, 14, 0),
                isDeadlineOutOfRange = true,
                ticketEndDate = LocalDate.of(2027, 4, 1)
            ),
            onAction = {}
        )
    }
}