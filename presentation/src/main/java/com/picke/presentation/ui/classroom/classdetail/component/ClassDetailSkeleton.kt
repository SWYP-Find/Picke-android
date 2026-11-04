package com.picke.presentation.ui.classroom.classdetail.component

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
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassDetailSkeletonDimens {
    val badgeWidth = 44.dp
    val badgeHeight = 22.dp
    val titleWidth = 220.dp
    val titleHeight = 24.dp
    val infoHeight = 88.dp
    val battleHeight = 220.dp
}

@Composable
fun ClassDetailSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(SpacingTokens.s16),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s20)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
            SkeletonLine(width = ClassDetailSkeletonDimens.badgeWidth, height = ClassDetailSkeletonDimens.badgeHeight)
            SkeletonLine(width = ClassDetailSkeletonDimens.titleWidth, height = ClassDetailSkeletonDimens.titleHeight)
        }

        PickeSkeletonBlock(height = ClassDetailSkeletonDimens.infoHeight)
        PickeSkeletonBlock(height = ClassDetailSkeletonDimens.battleHeight)

        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
            PickeSkeletonBlock(height = ComponentNumberTokens.buttonPrimaryLargeHeight)
            PickeSkeletonBlock(height = ComponentNumberTokens.buttonPrimaryLargeHeight)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassDetailSkeletonPreview() {
    PickeTheme {
        ClassDetailSkeleton()
    }
}