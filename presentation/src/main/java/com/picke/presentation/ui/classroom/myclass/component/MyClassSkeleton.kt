package com.picke.presentation.ui.classroom.myclass.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object MyClassSkeletonDimens {
    val headerHeight = 22.dp
    val badgeWidth = 44.dp
    val titleWidth = 180.dp
    val titleHeight = 18.dp
    val topicWidth = 240.dp
    val lineHeight = 12.dp
    val metaWidth = 140.dp
}

@Composable
fun MyClassSkeleton(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(PickeTheme.colors.cardBaseBackgroundDefault)
            .border(
                ComponentNumberTokens.borderWidthRegular,
                PickeTheme.colors.cardBaseBorderDefault,
                shape
            )
            .padding(SpacingTokens.s12),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
    ) {
        SkeletonLine(width = MyClassSkeletonDimens.badgeWidth, height = MyClassSkeletonDimens.headerHeight)

        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
            SkeletonLine(width = MyClassSkeletonDimens.titleWidth, height = MyClassSkeletonDimens.titleHeight)
            SkeletonLine(width = MyClassSkeletonDimens.topicWidth, height = MyClassSkeletonDimens.lineHeight)
        }

        HorizontalDivider(
            thickness = ComponentNumberTokens.borderWidthRegular,
            color = PickeTheme.colors.cardBaseBorderDefault
        )

        SkeletonLine(width = MyClassSkeletonDimens.metaWidth, height = MyClassSkeletonDimens.lineHeight)
    }
}

@Preview(showBackground = true)
@Composable
private fun MyClassSkeletonPreview() {
    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
        ) {
            MyClassSkeleton()
            MyClassSkeleton()
        }
    }
}