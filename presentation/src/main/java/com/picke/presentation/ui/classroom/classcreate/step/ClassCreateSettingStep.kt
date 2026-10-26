package com.picke.presentation.ui.classroom.classcreate.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiState
import com.picke.presentation.ui.classroom.component.ClassBattleCard
import com.picke.presentation.ui.classroom.component.ClassDeadlineField
import com.picke.presentation.ui.classroom.component.ClassSettingErrorText
import com.picke.presentation.ui.classroom.component.ClassTitledBlock
import com.picke.presentation.ui.component.CustomTextField
import com.picke.presentation.ui.component.CustomToggle
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import java.time.LocalDateTime

private object ClassCreateSettingStepDimens {
    val deadlineToggleWidth = 40.dp
    val deadlineToggleHeight = 22.dp
}

@Composable
internal fun ClassCreateSettingStep(
    uiState: ClassCreateUiState,
    onClassNameChange: (String) -> Unit,
    onDeadlineToggle: (Boolean) -> Unit,
    onDeadlineClick: () -> Unit,
    onCommentRequiredToggle: (Boolean) -> Unit,
    onOperatorJoinToggle: (Boolean) -> Unit,
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

        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)) {
            ClassSettingToggle(
                title = stringResource(R.string.class_setting_deadline_label),
                description = stringResource(R.string.class_setting_deadline_description),
                isChecked = uiState.isDeadlineEnabled,
                onCheckedChange = onDeadlineToggle,
                toggleWidth = ClassCreateSettingStepDimens.deadlineToggleWidth,
                toggleHeight = ClassCreateSettingStepDimens.deadlineToggleHeight
            )

            Column {
                ClassDeadlineField(
                    deadline = uiState.deadline,
                    isEnabled = uiState.isDeadlineEnabled,
                    onClick = onDeadlineClick,
                    isError = uiState.isDeadlineOutOfRange
                )
                ClassSettingErrorText(
                    text = stringResource(R.string.class_setting_deadline_range_error),
                    isVisible = uiState.isDeadlineOutOfRange
                )
            }
        }

        uiState.selectedBattle?.let { battle ->
            ClassTitledBlock(title = stringResource(R.string.class_setting_selected_battle)) {
                ClassBattleCard(battle = battle)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s20)) {
            ClassSettingToggle(
                title = stringResource(R.string.class_setting_comment_required),
                description = stringResource(R.string.class_setting_comment_required_description),
                isChecked = uiState.isCommentRequired,
                onCheckedChange = onCommentRequiredToggle
            )
            ClassSettingToggle(
                title = stringResource(R.string.class_setting_operator_join),
                description = stringResource(R.string.class_setting_operator_join_description),
                isChecked = uiState.isOperatorJoin,
                onCheckedChange = onOperatorJoinToggle
            )
        }
    }
}

@Composable
private fun ClassSettingToggle(
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    toggleWidth: Dp = ComponentNumberTokens.toggleWidth,
    toggleHeight: Dp = ComponentNumberTokens.toggleHeight
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s2)
        ) {
            Text(
                text = title,
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.bodyMdSemiBold
            )
            Text(
                text = description,
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.captionLgRegular
            )
        }

        CustomToggle(
            isChecked = isChecked,
            onCheckedChange = onCheckedChange,
            width = toggleWidth,
            height = toggleHeight
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassCreateSettingStepPreview() {
    PickeTheme {
        ClassCreateSettingStep(
            uiState = ClassCreateUiState(
                battles = listOf(DummyData.dummyClassBattles[1]),
                selectedBattleId = DummyData.dummyClassBattles[1].battleId,
                className = "2학년 3반 1학기 토론",
                deadline = LocalDateTime.of(2026, 9, 23, 18, 0)
            ),
            onClassNameChange = {},
            onDeadlineToggle = {},
            onDeadlineClick = {},
            onCommentRequiredToggle = {},
            onOperatorJoinToggle = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}

@Preview(name = "클래스명 공백", showBackground = true)
@Composable
private fun ClassCreateSettingStepBlankNamePreview() {
    PickeTheme {
        ClassCreateSettingStep(
            uiState = ClassCreateUiState(
                battles = listOf(DummyData.dummyClassBattles[1]),
                selectedBattleId = DummyData.dummyClassBattles[1].battleId,
                className = "   ",
                isClassNameInvalid = true,
                deadline = LocalDateTime.of(2026, 9, 23, 18, 0)
            ),
            onClassNameChange = {},
            onDeadlineToggle = {},
            onDeadlineClick = {},
            onCommentRequiredToggle = {},
            onOperatorJoinToggle = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}

@Preview(name = "마감일 범위 오류", showBackground = true)
@Composable
private fun ClassCreateSettingStepDeadlineErrorPreview() {
    PickeTheme {
        ClassCreateSettingStep(
            uiState = ClassCreateUiState(
                battles = listOf(DummyData.dummyClassBattles[1]),
                selectedBattleId = DummyData.dummyClassBattles[1].battleId,
                className = "2학년 3반 1학기 토론",
                deadline = LocalDateTime.of(2026, 9, 23, 18, 0),
                isDeadlineOutOfRange = true
            ),
            onClassNameChange = {},
            onDeadlineToggle = {},
            onDeadlineClick = {},
            onCommentRequiredToggle = {},
            onOperatorJoinToggle = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}