package com.picke.presentation.ui.classroom.classshare.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonBlock
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassShareSkeletonDimens {
    val codeHeight = 180.dp
    val battleHeight = 200.dp
}

@Composable
fun ClassShareSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s32)
    ) {
        SkeletonBlock(height = ClassShareSkeletonDimens.codeHeight)
        SkeletonBlock(height = ClassShareSkeletonDimens.battleHeight)
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassShareSkeletonPreview() {
    PickeTheme {
        ClassShareSkeleton(modifier = Modifier.padding(SpacingTokens.s16))
    }
}