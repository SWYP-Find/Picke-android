package com.picke.presentation.ui.perspective.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.PickeSkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PerspectiveSkeletonDimens {
    val cardPadding = 14.dp
}

@Composable
fun PerspectiveHeaderSkeleton(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s4)
    ) {
        PerspectiveHeaderSideSkeleton()
        Spacer(modifier = Modifier.width(SpacingTokens.s8))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            PickeSkeletonLine(width = 96.dp, height = 20.dp)
            Spacer(modifier = Modifier.height(SpacingTokens.s24))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .shimmer()
            )
        }
        Spacer(modifier = Modifier.width(SpacingTokens.s8))
        PerspectiveHeaderSideSkeleton()
    }
}

@Composable
private fun PerspectiveHeaderSideSkeleton() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(64.dp)
    ) {
        Spacer(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmer()
        )
        Spacer(modifier = Modifier.height(SpacingTokens.s2))
        PickeSkeletonLine(width = 40.dp, height = 14.dp)
        PickeSkeletonLine(width = 28.dp, height = 16.dp)
    }
}

@Composable
fun PerspectiveTabBarSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s16),
            horizontalArrangement = Arrangement.Center
        ) {
            PickeSkeletonLine(width = 64.dp, height = 20.dp)
        }
        HorizontalDivider(color = PickeTheme.colors.borderBeigeDefault, thickness = 2.dp)
    }
}

@Composable
fun PerspectiveListSkeleton(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s12),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
    ) {
        items(4) {
            PerspectiveItemCardSkeleton()
        }
    }
}

@Composable
private fun PerspectiveItemCardSkeleton() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PickeTheme.colors.surfaceBeigeDefault),
        shape = RoundedCornerShape(RadiusTokens.default),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(width = ComponentNumberTokens.borderWidthRegular, color = PickeTheme.colors.borderBeigeDefault)
    ) {
        Column(modifier = Modifier.padding(PerspectiveSkeletonDimens.cardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .shimmer()
                )
                Spacer(modifier = Modifier.width(SpacingTokens.s8))
                Column(modifier = Modifier.weight(1f)) {
                    PickeSkeletonLine(width = 40.dp, height = 16.dp)
                    Spacer(modifier = Modifier.height(SpacingTokens.s4))
                    PickeSkeletonLine(width = 50.dp, height = 12.dp)
                }
            }

            Spacer(modifier = Modifier.height(SpacingTokens.s8))
            PickeSkeletonLine(width = 70.dp, height = 20.dp)
            Spacer(modifier = Modifier.height(SpacingTokens.s12))
            PickeSkeletonLine(width = 260.dp, height = 16.dp)
            Spacer(modifier = Modifier.height(SpacingTokens.s4))
            PickeSkeletonLine(width = 200.dp, height = 16.dp)

            Spacer(modifier = Modifier.height(SpacingTokens.s12))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                PickeSkeletonLine(width = 40.dp, height = 14.dp)
                Spacer(modifier = Modifier.weight(1f))
                PickeSkeletonLine(width = 24.dp, height = 14.dp)
                Spacer(modifier = Modifier.width(SpacingTokens.s12))
                PickeSkeletonLine(width = 24.dp, height = 14.dp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PerspectiveHeaderSkeletonPreview() {
    PickeTheme { PerspectiveHeaderSkeleton() }
}

@Preview(showBackground = true)
@Composable
private fun PerspectiveTabBarSkeletonPreview() {
    PickeTheme { PerspectiveTabBarSkeleton() }
}

@Preview(showBackground = true)
@Composable
private fun PerspectiveListSkeletonPreview() {
    PickeTheme { PerspectiveListSkeleton() }
}