package com.picke.presentation.ui.classroom.classcreate.step

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classcreate.component.ClassFormField
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiState
import com.picke.presentation.ui.classroom.component.ClassBattleCard
import com.picke.presentation.ui.classroom.component.ClassTextField
import com.picke.presentation.ui.component.CustomToggle
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        ClassFormField(
            title = stringResource(R.string.class_setting_name_label),
            isRequired = true,
            contentSpacing = SpacingTokens.s8
        ) {
            ClassTextField(
                value = uiState.className,
                onValueChange = onClassNameChange,
                minHeight = ComponentNumberTokens.inputTextfieldHeight,
                textStyle = PickeTheme.typography.bodyXsMedium,
                placeholder = stringResource(R.string.class_setting_name_placeholder)
            )
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
            ClassDeadlineField(
                deadline = uiState.deadline,
                isEnabled = uiState.isDeadlineEnabled,
                onClick = onDeadlineClick
            )
        }

        uiState.selectedBattle?.let { battle ->
            ClassFormField(title = stringResource(R.string.class_setting_selected_battle)) {
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
private fun ClassDeadlineField(
    deadline: LocalDateTime?,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    val deadlineFormat = stringResource(R.string.class_setting_deadline_format)
    val deadlineText = remember(deadline, deadlineFormat) {
        deadline?.format(DateTimeFormatter.ofPattern(deadlineFormat))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ComponentNumberTokens.inputTextfieldHeight)
            .clip(shape)
            .background(
                if (isEnabled) {
                    PickeTheme.colors.inputTextfieldBackgroundDefault
                } else {
                    PickeTheme.colors.inputTextfieldBackgroundDisabled
                }
            )
            .border(
                ComponentNumberTokens.borderWidthRegular,
                PickeTheme.colors.inputTextfieldBorderDefault,
                shape
            )
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            )
            .padding(
                horizontal = SpacingTokens.s8,
                vertical = SpacingTokens.s8
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        if (isEnabled && deadlineText != null) {
            Text(
                text = deadlineText,
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.bodySmMedium
            )
        } else {
            Text(
                text = stringResource(R.string.class_setting_deadline_placeholder),
                color = PickeTheme.colors.inputTextfieldTextDefault,
                style = PickeTheme.typography.bodyXsMedium
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