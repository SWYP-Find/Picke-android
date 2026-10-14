package com.picke.presentation.ui.classroom.classcreate.step

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classcreate.component.ClassPillBadge
import com.picke.presentation.ui.classroom.classcreate.model.ClassCategory
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMethod
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiState
import com.picke.presentation.ui.classroom.classcreate.model.ClassLevel
import com.picke.presentation.ui.classroom.component.ClassBattleCard
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private object ClassCreateSelectionStepDimens {
    val conditionFadeWidth = 32.dp
    val conditionFadeHeight = 32.dp
    val editConditionButtonSize = 36.dp
}

@Composable
internal fun ClassCreateSelectionStep(
    uiState: ClassCreateUiState,
    onBattleSelect: (Long) -> Unit,
    onBattlePreviewClick: (Long) -> Unit,
    onEditConditionClick: () -> Unit,
    onRegenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAiQuestion = uiState.method == ClassCreateMethod.AI_TOPIC

    Column(modifier = modifier) {
        if (!isAiQuestion) {
            ClassCreateSelectedConditionSection(
                topic = uiState.topic,
                level = uiState.selectedLevel,
                categories = uiState.selectedCategories,
                onEditConditionClick = onEditConditionClick
            )
            Spacer(modifier = Modifier.height(SpacingTokens.s16))
        }

        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)) {
            ClassCreateSelectionHeader(
                resultCount = uiState.battles.size,
                showsRegenerate = isAiQuestion,
                onRegenerateClick = onRegenerateClick
            )

            uiState.battles.forEach { battle ->
                ClassBattleCard(
                    battle = battle,
                    isSelected = battle.battleId == uiState.selectedBattleId,
                    onClick = { onBattleSelect(battle.battleId) },
                    onPreviewClick = if (isAiQuestion) null else ({ onBattlePreviewClick(battle.battleId) })
                )
            }
        }
    }
}

@Composable
private fun ClassCreateSelectedConditionSection(
    topic: String,
    level: ClassLevel,
    categories: List<ClassCategory>,
    onEditConditionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.class_create_selected_condition),
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.bodyXsMedium
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
                ) {
                    if (topic.isNotBlank()) {
                        ClassPillBadge(text = topic)
                    }
                    ClassPillBadge(text = stringResource(level.labelRes))
                    categories.forEach { category ->
                        ClassPillBadge(text = stringResource(category.labelRes))
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .width(ClassCreateSelectionStepDimens.conditionFadeWidth)
                        .height(ClassCreateSelectionStepDimens.conditionFadeHeight)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    PickeTheme.colors.backgroundBeige.copy(alpha = 0f),
                                    PickeTheme.colors.backgroundBeige
                                )
                            )
                        )
                )
            }
        }

        Box(
            modifier = Modifier
                .size(ClassCreateSelectionStepDimens.editConditionButtonSize)
                .clip(CircleShape)
                .background(PickeTheme.colors.cardBaseBackgroundDefault)
                .border(
                    ComponentNumberTokens.borderWidthRegular,
                    PickeTheme.colors.primary100,
                    CircleShape
                )
                .clickable(onClick = onEditConditionClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_reset),
                contentDescription = stringResource(R.string.class_create_edit_condition),
                modifier = Modifier.size(ComponentNumberTokens.iconMd),
                tint = PickeTheme.colors.textPrimary
            )
        }
    }
}

@Composable
private fun ClassCreateSelectionHeader(
    resultCount: Int,
    showsRegenerate: Boolean,
    onRegenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.class_create_result_count, resultCount),
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.bodySmSemiBold
        )

        if (showsRegenerate) {
            Row(
                modifier = Modifier.clickable(onClick = onRegenerateClick),
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_replay),
                    contentDescription = null,
                    modifier = Modifier.size(ComponentNumberTokens.iconSm),
                    tint = PickeTheme.colors.textMuted
                )
                Text(
                    text = stringResource(R.string.class_create_regenerate),
                    color = PickeTheme.colors.textMuted,
                    style = PickeTheme.typography.bodyXsSemiBold
                )
            }
        }
    }
}

@Preview(name = "기존 콘텐츠", showBackground = true)
@Composable
private fun ClassCreateSelectionStepPreview() {
    PickeTheme {
        ClassCreateSelectionStep(
            uiState = ClassCreateUiState(
                topic = "AI와 인간 사이의 윤리와 사상에 대해서",
                selectedCategories = listOf(ClassCategory.PHILOSOPHY, ClassCategory.SOCIETY),
                battles = DummyData.dummyClassBattles,
                selectedBattleId = DummyData.dummyClassBattles.first().battleId
            ),
            onBattleSelect = {},
            onBattlePreviewClick = {},
            onEditConditionClick = {},
            onRegenerateClick = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}

@Preview(name = "AI 질문", showBackground = true)
@Composable
private fun ClassCreateAiSelectionStepPreview() {
    PickeTheme {
        ClassCreateSelectionStep(
            uiState = ClassCreateUiState(
                method = ClassCreateMethod.AI_TOPIC,
                battles = DummyData.dummyClassAiQuestions,
                selectedBattleId = DummyData.dummyClassAiQuestions.first().battleId
            ),
            onBattleSelect = {},
            onBattlePreviewClick = {},
            onEditConditionClick = {},
            onRegenerateClick = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}