package com.picke.presentation.ui.classroom.classmyopinion.component

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

private object ClassMyOpinionSkeletonDimens {
    val cardHeight = 120.dp
}

@Composable
fun ClassMyOpinionSkeleton(modifier: Modifier = Modifier) {
    SkeletonBlock(
        height = ClassMyOpinionSkeletonDimens.cardHeight,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ClassMyOpinionSkeletonPreview() {
    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
        ) {
            ClassMyOpinionSkeleton()
            ClassMyOpinionSkeleton()
        }
    }
}