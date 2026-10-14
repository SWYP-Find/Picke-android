package com.picke.presentation.ui.classroom.classcreate.step

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classcreate.component.ClassMethodCard
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMethod
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
internal fun ClassCreateMethodStep(
    selectedMethod: ClassCreateMethod,
    onMethodSelect: (ClassCreateMethod) -> Unit,
    onGuideClick: (ClassCreateMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
    ) {
        ClassMethodCard(
            badge = stringResource(R.string.class_create_method_content_badge),
            title = stringResource(R.string.class_create_method_content_title),
            description = stringResource(R.string.class_create_method_content_description),
            isSelected = selectedMethod == ClassCreateMethod.CONTENT,
            onClick = { onMethodSelect(ClassCreateMethod.CONTENT) },
            onBadgeClick = { onGuideClick(ClassCreateMethod.CONTENT) }
        )
        ClassMethodCard(
            badge = stringResource(R.string.class_create_method_ai_badge),
            title = stringResource(R.string.class_create_method_ai_title),
            description = stringResource(R.string.class_create_method_ai_description),
            isSelected = selectedMethod == ClassCreateMethod.AI_TOPIC,
            onClick = { onMethodSelect(ClassCreateMethod.AI_TOPIC) },
            onBadgeClick = { onGuideClick(ClassCreateMethod.AI_TOPIC) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassCreateMethodStepPreview() {
    PickeTheme {
        ClassCreateMethodStep(
            selectedMethod = ClassCreateMethod.CONTENT,
            onMethodSelect = {},
            onGuideClick = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}