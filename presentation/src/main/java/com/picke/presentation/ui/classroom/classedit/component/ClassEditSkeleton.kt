package com.picke.presentation.ui.classroom.classedit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.PickeSkeletonBlock
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private const val CLASS_EDIT_FIELD_COUNT = 2

private object ClassEditSkeletonDimens {
    val labelWidth = 72.dp
    val labelHeight = 14.dp
    val fieldHeight = 48.dp
}

@Composable
fun ClassEditSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s24)
    ) {
        repeat(CLASS_EDIT_FIELD_COUNT) {
            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
                SkeletonLine(width = ClassEditSkeletonDimens.labelWidth, height = ClassEditSkeletonDimens.labelHeight)
                PickeSkeletonBlock(height = ClassEditSkeletonDimens.fieldHeight)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassEditSkeletonPreview() {
    PickeTheme {
        ClassEditSkeleton(modifier = Modifier.padding(SpacingTokens.s16))
    }
}