package com.picke.presentation.ui.classroom.classcreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.PickeSkeletonBlock
import com.picke.presentation.ui.component.PickeSkeletonLine
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassTopicSkeletonDimens {
    val badgeWidth = 44.dp
    val badgeHeight = 22.dp
    val titleWidth = 240.dp
    val titleHeight = 18.dp
    val descriptionWidth = 200.dp
    val descriptionHeight = 12.dp
    val optionHeight = 48.dp
}

@Composable
internal fun ClassTopicSkeleton(modifier: Modifier = Modifier) {
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
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)) {
            PickeSkeletonLine(
                width = ClassTopicSkeletonDimens.badgeWidth,
                height = ClassTopicSkeletonDimens.badgeHeight
            )

            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
                PickeSkeletonLine(
                    width = ClassTopicSkeletonDimens.titleWidth,
                    height = ClassTopicSkeletonDimens.titleHeight
                )
                PickeSkeletonLine(
                    width = ClassTopicSkeletonDimens.descriptionWidth,
                    height = ClassTopicSkeletonDimens.descriptionHeight
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
            PickeSkeletonBlock(
                height = ClassTopicSkeletonDimens.optionHeight,
                modifier = Modifier.weight(1f)
            )
            PickeSkeletonBlock(
                height = ClassTopicSkeletonDimens.optionHeight,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassTopicSkeletonPreview() {
    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
        ) {
            ClassTopicSkeleton()
            ClassTopicSkeleton()
        }
    }
}