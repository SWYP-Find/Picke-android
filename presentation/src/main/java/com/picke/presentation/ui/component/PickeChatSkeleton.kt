package com.picke.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PickeChatSkeletonDimens {
    val opponentBubbleWidth = 260.dp
    val opponentBubbleHeight = 96.dp
    val myBubbleWidth = 220.dp
    val myBubbleHeight = 64.dp
}

@Composable
fun PickeChatSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(SpacingTokens.s16),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
    ) {
        SkeletonLine(
            width = PickeChatSkeletonDimens.opponentBubbleWidth,
            height = PickeChatSkeletonDimens.opponentBubbleHeight
        )
        SkeletonLine(
            width = PickeChatSkeletonDimens.myBubbleWidth,
            height = PickeChatSkeletonDimens.myBubbleHeight,
            modifier = Modifier.align(Alignment.End)
        )
        SkeletonLine(
            width = PickeChatSkeletonDimens.opponentBubbleWidth,
            height = PickeChatSkeletonDimens.opponentBubbleHeight
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PickeChatSkeletonPreview() {
    PickeTheme {
        PickeChatSkeleton()
    }
}