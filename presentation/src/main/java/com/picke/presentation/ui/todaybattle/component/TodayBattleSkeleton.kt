package com.picke.presentation.ui.todaybattle.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.PickeSkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object TodayBattleSkeletonDimens {
    val cardMinHeight = 140.dp
    val quoteTopSpacing = 10.dp
}

@Composable
fun TodayBattleSkeleton(modifier: Modifier = Modifier) {
    val darkShimmerBase = PickeTheme.colors.gray600
    val darkShimmerHighlight = PickeTheme.colors.gray400

    Column(modifier = modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shimmer(darkShimmerBase, darkShimmerHighlight)
        )

        Spacer(modifier = Modifier.height(SpacingTokens.s32))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.s16),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OpinionCardSkeleton(
                    darkShimmerBase = darkShimmerBase,
                    darkShimmerHighlight = darkShimmerHighlight
                )
                OpinionCardSkeleton(
                    darkShimmerBase = darkShimmerBase,
                    darkShimmerHighlight = darkShimmerHighlight
                )
            }

            Spacer(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmer(darkShimmerBase, darkShimmerHighlight)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = SpacingTokens.s16,
                    top = SpacingTokens.s16,
                    end = SpacingTokens.s16,
                    bottom = SpacingTokens.s20
                )
                .height(ComponentNumberTokens.buttonPrimaryLargeHeight)
                .clip(RoundedCornerShape(2.dp))
                .shimmer(darkShimmerBase, darkShimmerHighlight)
        )
    }
}

@Composable
private fun OpinionCardSkeleton(
    darkShimmerBase: Color,
    darkShimmerHighlight: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = TodayBattleSkeletonDimens.cardMinHeight)
            .clip(RoundedCornerShape(RadiusTokens.default))
            .background(PickeTheme.colors.gray700)
            .padding(vertical = SpacingTokens.s24),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PickeSkeletonLine(
            width = 40.dp,
            height = 12.dp,
            baseColor = darkShimmerBase,
            highlightColor = darkShimmerHighlight
        )

        Spacer(modifier = Modifier.height(SpacingTokens.s8))
        PickeSkeletonLine(
            width = 120.dp,
            height = 22.dp,
            baseColor = darkShimmerBase,
            highlightColor = darkShimmerHighlight
        )

        Spacer(modifier = Modifier.height(TodayBattleSkeletonDimens.quoteTopSpacing))
        PickeSkeletonLine(
            width = 160.dp,
            height = 12.dp,
            baseColor = darkShimmerBase,
            highlightColor = darkShimmerHighlight
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TodayBattleSkeletonPreview() {
    PickeTheme { TodayBattleSkeleton() }
}