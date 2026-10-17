package com.picke.presentation.ui.classroom.classjoin

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinDialog
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinStep
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiAction
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiEvent
import com.picke.presentation.ui.classroom.classjoin.model.ClassJoinUiState
import com.picke.presentation.ui.classroom.classjoin.step.ClassJoinCodeStep
import com.picke.presentation.ui.classroom.classjoin.step.ClassJoinNameStep
import com.picke.presentation.ui.classroom.component.ClassBottomButton
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.dialog.CustomSingleActionDialog
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun ClassJoinScreen(
    onBackClick: () -> Unit,
    onNavigateToClassDetail: (classId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassJoinViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { viewModel.onAction(ClassJoinUiAction.BackClick) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassJoinUiEvent.NavigateBack -> onBackClick()
                is ClassJoinUiEvent.NavigateToClassDetail -> onNavigateToClassDetail(event.classId)
            }
        }
    }

    ClassJoinContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassJoinContent(
    uiState: ClassJoinUiState,
    onAction: (ClassJoinUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .background(PickeTheme.colors.surfaceBeigeDefault)
            .systemBarsPadding()
            .imePadding(),
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.class_join_title),
                onBackClick = { onAction(ClassJoinUiAction.BackClick) },
                backgroundColor = PickeTheme.colors.surfaceBeigeDefault,
                titleColor = PickeTheme.colors.textSubtler
            )
        },
        bottomBar = {
            ClassBottomButton(
                text = stringResource(uiState.step.nextButtonRes()),
                onClick = { onAction(ClassJoinUiAction.NextClick) },
                enabled = uiState.isNextEnabled,
                backgroundColor = PickeTheme.colors.surfaceBeigeDefault
            )
        },
        containerColor = PickeTheme.colors.surfaceBeigeDefault,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s20)
        ) {
            ClassJoinHeaderSection(step = uiState.step)

            when (uiState.step) {
                ClassJoinStep.CODE -> ClassJoinCodeStep(
                    code = uiState.code,
                    isCodeInvalid = uiState.isCodeInvalid,
                    onCodeChange = { code -> onAction(ClassJoinUiAction.CodeChange(code)) }
                )

                ClassJoinStep.NAME -> ClassJoinNameStep(
                    name = uiState.name,
                    isNameDuplicated = uiState.isNameDuplicated,
                    onNameChange = { name -> onAction(ClassJoinUiAction.NameChange(name)) }
                )
            }
        }
    }

    val dialog = uiState.dialog
    if (dialog != null) {
        CustomSingleActionDialog(
            message = stringResource(dialog.messageRes()),
            buttonText = stringResource(R.string.dialog_ok),
            onConfirm = { onAction(ClassJoinUiAction.DialogDismiss) },
            onDismiss = { onAction(ClassJoinUiAction.DialogDismiss) }
        )
    }
}

@Composable
private fun ClassJoinHeaderSection(
    step: ClassJoinStep,
    modifier: Modifier = Modifier
) {
    val headline = when (step) {
        ClassJoinStep.CODE -> stringResource(R.string.class_join_headline)
        ClassJoinStep.NAME -> stringResource(R.string.class_name_sheet_title)
    }
    val description = when (step) {
        ClassJoinStep.CODE -> stringResource(
            R.string.class_join_description,
            ClassJoinUiState.CODE_LENGTH
        )

        ClassJoinStep.NAME -> stringResource(R.string.class_name_sheet_description)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
    ) {
        Text(
            text = headline,
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.headingXl
        )
        Text(
            text = description,
            color = PickeTheme.colors.textMuted,
            style = PickeTheme.typography.bodyMdMedium
        )
    }
}

@StringRes
private fun ClassJoinStep.nextButtonRes(): Int = when (this) {
    ClassJoinStep.CODE -> R.string.class_join_action
    ClassJoinStep.NAME -> R.string.class_name_sheet_enter
}

@StringRes
private fun ClassJoinDialog.messageRes(): Int = when (this) {
    ClassJoinDialog.INVALID_CODE -> R.string.class_join_dialog_invalid_code
    ClassJoinDialog.CLASS_FULL -> R.string.class_join_dialog_class_full
    ClassJoinDialog.CLASS_CLOSED -> R.string.class_join_dialog_class_closed
    ClassJoinDialog.CLASS_DELETED -> R.string.class_join_dialog_class_deleted
    ClassJoinDialog.ALREADY_JOINED -> R.string.class_join_dialog_already_joined
}

@Preview(name = "코드 입력", showBackground = true)
@Composable
private fun ClassJoinContentCodePreview() {
    PickeTheme {
        ClassJoinContent(
            uiState = ClassJoinUiState(code = DummyData.dummyClassCode),
            onAction = {}
        )
    }
}

@Preview(name = "이름 입력", showBackground = true)
@Composable
private fun ClassJoinContentNamePreview() {
    PickeTheme {
        ClassJoinContent(
            uiState = ClassJoinUiState(step = ClassJoinStep.NAME, code = DummyData.dummyClassCode),
            onAction = {}
        )
    }
}

@Preview(name = "없는 코드", showBackground = true)
@Composable
private fun ClassJoinContentInvalidCodePreview() {
    PickeTheme {
        ClassJoinContent(
            uiState = ClassJoinUiState(
                code = DummyData.dummyClassCode,
                isCodeInvalid = true,
                dialog = ClassJoinDialog.INVALID_CODE
            ),
            onAction = {}
        )
    }
}

@Preview(name = "인원 초과", showBackground = true)
@Composable
private fun ClassJoinContentClassFullPreview() {
    PickeTheme {
        ClassJoinContent(
            uiState = ClassJoinUiState(code = DummyData.dummyClassCode, dialog = ClassJoinDialog.CLASS_FULL),
            onAction = {}
        )
    }
}

@Preview(name = "마감된 클래스", showBackground = true)
@Composable
private fun ClassJoinContentClassClosedPreview() {
    PickeTheme {
        ClassJoinContent(
            uiState = ClassJoinUiState(code = DummyData.dummyClassCode, dialog = ClassJoinDialog.CLASS_CLOSED),
            onAction = {}
        )
    }
}

@Preview(name = "삭제된 클래스", showBackground = true)
@Composable
private fun ClassJoinContentClassDeletedPreview() {
    PickeTheme {
        ClassJoinContent(
            uiState = ClassJoinUiState(code = DummyData.dummyClassCode, dialog = ClassJoinDialog.CLASS_DELETED),
            onAction = {}
        )
    }
}

@Preview(name = "이미 참여 중", showBackground = true)
@Composable
private fun ClassJoinContentAlreadyJoinedPreview() {
    PickeTheme {
        ClassJoinContent(
            uiState = ClassJoinUiState(code = DummyData.dummyClassCode, dialog = ClassJoinDialog.ALREADY_JOINED),
            onAction = {}
        )
    }
}