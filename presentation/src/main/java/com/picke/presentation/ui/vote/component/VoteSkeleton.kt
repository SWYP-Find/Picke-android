package com.picke.presentation.ui.vote.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.picke.presentation.ui.vote.model.VoteType

@Composable
fun VoteSkeleton(
    voteType: VoteType,
    modifier: Modifier = Modifier
) {
    val isPreVote = voteType == VoteType.PRE
    val bgColor = if (isPreVote) PickeTheme.colors.surfaceBeigeDefault else Color.Black
    val shimmerBase = if (isPreVote) null else PickeTheme.colors.gray600
    val shimmerHighlight = if (isPreVote) null else PickeTheme.colors.gray400

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer(
                        baseColor = shimmerBase,
                        highlightColor = shimmerHighlight
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(horizontal = SpacingTokens.s20)
                    .padding(bottom = SpacingTokens.s16),
                horizontalAlignment = Alignment.Start
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
                    PickeSkeletonLine(
                        width = 48.dp,
                        height = 20.dp,
                        baseColor = shimmerBase,
                        highlightColor = shimmerHighlight
                    )
                    PickeSkeletonLine(
                        width = 48.dp,
                        height = 20.dp,
                        baseColor = shimmerBase,
                        highlightColor = shimmerHighlight
                    )
                }

                Spacer(modifier = Modifier.height(SpacingTokens.s16))
                PickeSkeletonLine(
                    width = 220.dp,
                    height = 28.dp,
                    baseColor = shimmerBase,
                    highlightColor = shimmerHighlight
                )

                Spacer(modifier = Modifier.height(SpacingTokens.s8))
                PickeSkeletonLine(
                    width = 160.dp,
                    height = 28.dp,
                    baseColor = shimmerBase,
                    highlightColor = shimmerHighlight
                )

                Spacer(modifier = Modifier.height(SpacingTokens.s12))
                PickeSkeletonLine(
                    width = 240.dp,
                    height = 16.dp,
                    baseColor = shimmerBase,
                    highlightColor = shimmerHighlight
                )
            }
        }

        Spacer(modifier = Modifier.height(SpacingTokens.s24))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.s20),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
            ) {
                VoteOptionCardSkeleton(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                )
                VoteOptionCardSkeleton(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                )
            }

            Spacer(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .shimmer(shimmerBase, shimmerHighlight)
            )
        }

        Spacer(modifier = Modifier.height(SpacingTokens.s32))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(SpacingTokens.s20)
                .height(48.dp)
                .clip(RoundedCornerShape(RadiusTokens.default))
                .shimmer(shimmerBase, shimmerHighlight)
        )
    }
}

@Composable
private fun VoteOptionCardSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(RadiusTokens.default))
            .border(ComponentNumberTokens.borderWidthRegular, PickeTheme.colors.borderBeigeDisabled, RoundedCornerShape(RadiusTokens.default))
            .background(PickeTheme.colors.surfaceBeigeSubtle)
            .padding(vertical = SpacingTokens.s24, horizontal = SpacingTokens.s16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmer()
        )
        Spacer(modifier = Modifier.height(SpacingTokens.s16))
        PickeSkeletonLine(width = 60.dp, height = 20.dp)
        Spacer(modifier = Modifier.height(SpacingTokens.s4))
        PickeSkeletonLine(width = 40.dp, height = 14.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun VoteSkeletonPreview() {
    PickeTheme {
        VoteSkeleton(voteType = VoteType.PRE)
    }
}