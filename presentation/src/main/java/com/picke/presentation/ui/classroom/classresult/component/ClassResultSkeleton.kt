package com.picke.presentation.ui.classroom.classresult.component

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

private object ClassResultSkeletonDimens {
    val tagWidth = 56.dp
    val lineHeight = 14.dp
    val titleWidth = 240.dp
    val titleHeight = 24.dp
    val deadlineWidth = 140.dp
    val tabHeight = 44.dp
    val participationHeight = 140.dp
    val stanceHeight = 220.dp
    val insightHeight = 160.dp
}

@Composable
fun ClassResultSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(SpacingTokens.s16),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s20)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
            SkeletonLine(width = ClassResultSkeletonDimens.tagWidth, height = ClassResultSkeletonDimens.lineHeight)
            SkeletonLine(width = ClassResultSkeletonDimens.titleWidth, height = ClassResultSkeletonDimens.titleHeight)
            SkeletonLine(width = ClassResultSkeletonDimens.deadlineWidth, height = ClassResultSkeletonDimens.lineHeight)
        }
        PickeSkeletonBlock(height = ClassResultSkeletonDimens.tabHeight)
        PickeSkeletonBlock(height = ClassResultSkeletonDimens.participationHeight)
        PickeSkeletonBlock(height = ClassResultSkeletonDimens.stanceHeight)
        PickeSkeletonBlock(height = ClassResultSkeletonDimens.insightHeight)
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassResultSkeletonPreview() {
    PickeTheme {
        ClassResultSkeleton()
    }
}