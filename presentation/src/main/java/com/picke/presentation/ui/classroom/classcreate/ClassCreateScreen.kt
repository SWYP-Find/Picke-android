package com.picke.presentation.ui.classroom.classcreate

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classcreate.component.ClassMethodGuideSheet
import com.picke.presentation.ui.classroom.classcreate.component.deadline.ClassDeadlineSheet
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMethod
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateStep
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiAction
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiEvent
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiState
import com.picke.presentation.ui.classroom.classcreate.step.ClassCreateConditionStep
import com.picke.presentation.ui.classroom.classcreate.step.ClassCreateMethodStep
import com.picke.presentation.ui.classroom.classcreate.step.ClassCreateSelectionStep
import com.picke.presentation.ui.classroom.classcreate.step.ClassCreateSettingStep
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassCreateScreenDimens {
    val bottomFadeHeight = 70.dp
}

@Composable
fun ClassCreateScreen(
    onBackClick: () -> Unit,
    onNavigateToShare: (classId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassCreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { viewModel.onAction(ClassCreateUiAction.BackClick) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassCreateUiEvent.NavigateBack -> onBackClick()
                is ClassCreateUiEvent.NavigateToShare -> onNavigateToShare(event.classId)
            }
        }
    }

    ClassCreateContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassCreateContent(
    uiState: ClassCreateUiState,
    onAction: (ClassCreateUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelectionStep = uiState.step == ClassCreateStep.SELECTION
    val showsBottomFade = isSelectionStep || uiState.step == ClassCreateStep.SETTING

    Scaffold(
        modifier = modifier
            .background(PickeTheme.colors.backgroundBeige)
            .systemBarsPadding(),
        topBar = {
            CustomTopAppBar(
                title = stringResource(uiState.step.titleRes()),
                onBackClick = { onAction(ClassCreateUiAction.BackClick) },
                showBackButton = true,
                backgroundColor = PickeTheme.colors.backgroundBeige,
                titleColor = PickeTheme.colors.textSubtler
            )
        },
        bottomBar = {
            ClassCreateBottomSection(
                text = stringResource(uiState.nextButtonRes()),
                isEnabled = uiState.isNextEnabled,
                onClick = { onAction(ClassCreateUiAction.NextClick) }
            )
        },
        containerColor = PickeTheme.colors.backgroundBeige,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(SpacingTokens.s16)
            ) {
                ClassCreateHeaderSection(
                    step = uiState.step,
                    method = uiState.method
                )

                Spacer(modifier = Modifier.height(if (isSelectionStep && uiState.method == ClassCreateMethod.CONTENT) SpacingTokens.s20 else SpacingTokens.s32))
                when (uiState.step) {
                    ClassCreateStep.METHOD -> ClassCreateMethodStep(
                        selectedMethod = uiState.method,
                        onMethodSelect = { method ->
                            onAction(ClassCreateUiAction.MethodSelect(method))
                        },
                        onGuideClick = { method ->
                            onAction(ClassCreateUiAction.GuideClick(method))
                        }
                    )

                    ClassCreateStep.CONDITION -> ClassCreateConditionStep(
                        uiState = uiState,
                        onTopicChange = { topic ->
                            onAction(ClassCreateUiAction.TopicChange(topic))
                        },
                        onLevelSelect = { level ->
                            onAction(ClassCreateUiAction.LevelSelect(level))
                        },
                        onCategorySelect = { category ->
                            onAction(ClassCreateUiAction.CategorySelect(category))
                        }
                    )

                    ClassCreateStep.SELECTION -> ClassCreateSelectionStep(
                        uiState = uiState,
                        onBattleSelect = { battleId ->
                            onAction(ClassCreateUiAction.BattleSelect(battleId))
                        },
                        onBattlePreviewClick = { battleId ->
                            onAction(ClassCreateUiAction.BattlePreviewClick(battleId))
                        },
                        onEditConditionClick = {
                            onAction(ClassCreateUiAction.EditConditionClick)
                        },
                        onRegenerateClick = {
                            onAction(ClassCreateUiAction.RegenerateClick)
                        }
                    )

                    ClassCreateStep.SETTING -> ClassCreateSettingStep(
                        uiState = uiState,
                        onClassNameChange = { name ->
                            onAction(ClassCreateUiAction.ClassNameChange(name))
                        },
                        onDeadlineToggle = { isEnabled ->
                            onAction(ClassCreateUiAction.DeadlineToggle(isEnabled))
                        },
                        onDeadlineClick = {
                            onAction(ClassCreateUiAction.DeadlineFieldClick)
                        },
                        onCommentRequiredToggle = { isRequired ->
                            onAction(ClassCreateUiAction.CommentRequiredToggle(isRequired))
                        },
                        onOperatorJoinToggle = { isJoined ->
                            onAction(ClassCreateUiAction.OperatorJoinToggle(isJoined))
                        }
                    )
                }
            }

            if (showsBottomFade) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(ClassCreateScreenDimens.bottomFadeHeight)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    PickeTheme.colors.backgroundBeige.copy(alpha = 0f),
                                    PickeTheme.colors.backgroundBeige
                                )
                            )
                        )
                )
            }
        }
    }

    if (uiState.isDeadlineSheetVisible && uiState.deadline != null) {
        ClassDeadlineSheet(
            deadline = uiState.deadline,
            onConfirm = { selected -> onAction(ClassCreateUiAction.DeadlineConfirm(selected)) },
            onDismiss = { onAction(ClassCreateUiAction.DeadlineSheetDismiss) }
        )
    }

    uiState.guideMethod?.let { method ->
        ClassMethodGuideSheet(
            method = method,
            onDismiss = { onAction(ClassCreateUiAction.GuideSheetDismiss) }
        )
    }
}

@Composable
private fun ClassCreateHeaderSection(
    step: ClassCreateStep,
    method: ClassCreateMethod,
    modifier: Modifier = Modifier
) {
    val (headlineRes, descriptionRes) = when (step) {
        ClassCreateStep.METHOD -> R.string.class_create_method_headline to R.string.class_create_method_description
        ClassCreateStep.CONDITION -> R.string.class_create_headline to R.string.class_create_description
        ClassCreateStep.SELECTION -> if (method == ClassCreateMethod.CONTENT) {
            R.string.class_create_battle_headline to R.string.class_create_battle_description
        } else {
            R.string.class_create_question_headline to R.string.class_create_question_description
        }

        ClassCreateStep.SETTING -> R.string.class_setting_headline to null
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
    ) {
        Text(
            text = stringResource(headlineRes),
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.headingXl
        )
        if (descriptionRes != null) {
            Text(
                text = stringResource(descriptionRes),
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.bodyMdMedium
            )
        }
    }
}

@Composable
private fun ClassCreateBottomSection(
    text: String,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isEnabled) {
        PickeTheme.colors.buttonPrimaryBackgroundDefault
    } else {
        PickeTheme.colors.buttonPrimaryBackgroundDisabled
    }

    Box(
        modifier = modifier
            .background(PickeTheme.colors.backgroundBeige)
            .padding(
                start = SpacingTokens.s16,
                end = SpacingTokens.s16,
                bottom = SpacingTokens.s16
            )
    ) {
        CustomButton(
            text = text,
            onClick = onClick,
            modifier = Modifier.height(ComponentNumberTokens.buttonPrimaryLargeHeight),
            backgroundColor = backgroundColor,
            textColor = PickeTheme.colors.buttonPrimaryTextDefault,
            enabled = isEnabled
        )
    }
}

@StringRes
private fun ClassCreateStep.titleRes(): Int = when (this) {
    ClassCreateStep.METHOD, ClassCreateStep.CONDITION -> R.string.class_create_title
    ClassCreateStep.SELECTION -> R.string.class_create_selection_title
    ClassCreateStep.SETTING -> R.string.class_setting_title
}

@StringRes
private fun ClassCreateUiState.nextButtonRes(): Int = when (step) {
    ClassCreateStep.METHOD -> R.string.class_create_set_topic
    ClassCreateStep.CONDITION -> R.string.class_create_recommend
    ClassCreateStep.SELECTION -> if (method == ClassCreateMethod.CONTENT) {
        R.string.class_create_select_battle
    } else {
        R.string.class_create_select_question
    }

    ClassCreateStep.SETTING -> R.string.class_setting_create
}

@Preview(showBackground = true)
@Composable
private fun ClassCreateScreenPreview() {
    PickeTheme {
        ClassCreateContent(
            uiState = ClassCreateUiState(),
            onAction = {}
        )
    }
}