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
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classcreate.component.ClassLevelTab
import com.picke.presentation.ui.classroom.classcreate.component.ClassOptionChip
import com.picke.presentation.ui.classroom.classcreate.model.ClassCategory
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMethod
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateUiState
import com.picke.presentation.ui.classroom.classcreate.model.ClassLevel
import com.picke.presentation.ui.classroom.component.ClassTitledBlock
import com.picke.presentation.ui.component.PickeTextField
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private const val CATEGORY_COLUMNS = 3

@Composable
internal fun ClassCreateConditionStep(
    uiState: ClassCreateUiState,
    onTopicChange: (String) -> Unit,
    onLevelSelect: (ClassLevel) -> Unit,
    onCategorySelect: (ClassCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s20)
    ) {
        ClassTitledBlock(
            title = stringResource(R.string.class_create_topic_label),
            isRequired = uiState.isTopicRequired,
            contentSpacing = SpacingTokens.s8,
            titleStyle = PickeTheme.typography.bodySmSemiBold
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
                PickeTextField(
                    value = uiState.topic,
                    onValueChange = onTopicChange,
                    minHeight = ComponentNumberTokens.inputTextfieldHeight,
                    textStyle = PickeTheme.typography.bodyXsMedium,
                    placeholder = stringResource(R.string.class_create_topic_placeholder),
                    horizontalPadding = SpacingTokens.s12
                )

                if (uiState.isTopicRequired) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.class_create_topic_helper),
                            modifier = Modifier.weight(1f),
                            color = PickeTheme.colors.textMuted,
                            style = PickeTheme.typography.captionLgMedium
                        )
                        Text(
                            text = stringResource(
                                R.string.class_create_topic_count,
                                uiState.topic.length,
                                ClassCreateUiState.TOPIC_MAX_LENGTH
                            ),
                            color = PickeTheme.colors.textMuted,
                            style = PickeTheme.typography.captionLgMedium
                        )
                    }
                }
            }
        }

        ClassTitledBlock(
            title = stringResource(R.string.class_create_level_label),
            isRequired = true,
            titleStyle = PickeTheme.typography.bodySmSemiBold
        ) {
            ClassLevelTab(
                labels = ClassLevel.entries.map { stringResource(it.labelRes) },
                selectedIndex = uiState.selectedLevel.ordinal,
                onSelect = { index -> onLevelSelect(ClassLevel.entries[index]) }
            )
        }

        ClassTitledBlock(
            title = stringResource(R.string.class_create_category_label),
            isRequired = true,
            titleStyle = PickeTheme.typography.bodySmSemiBold
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
                ClassCategory.entries.chunked(CATEGORY_COLUMNS).forEach { rowCategories ->
                    Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
                        rowCategories.forEach { category ->
                            ClassOptionChip(
                                text = stringResource(category.labelRes),
                                isSelected = category in uiState.selectedCategories,
                                onClick = { onCategorySelect(category) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassCreateConditionStepPreview() {
    PickeTheme {
        ClassCreateConditionStep(
            uiState = ClassCreateUiState(),
            onTopicChange = {},
            onLevelSelect = {},
            onCategorySelect = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}

@Preview(name = "AI 주제", showBackground = true)
@Composable
private fun ClassCreateConditionStepTopicErrorPreview() {
    PickeTheme {
        ClassCreateConditionStep(
            uiState = ClassCreateUiState(
                method = ClassCreateMethod.AI_TOPIC,
                topic = "촉"
            ),
            onTopicChange = {},
            onLevelSelect = {},
            onCategorySelect = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}