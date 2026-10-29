package com.picke.presentation.ui.classroom.classresult.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.formatRate

private object ClassResultParticipationCardDimens {
    val barHeight = 6.dp
}

@Composable
fun ClassResultParticipationCard(
    memberCount: Int,
    participatedCount: Int,
    commentedCount: Int,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.cardBaseBackgroundDefault, shape)
            .border(
                ComponentNumberTokens.borderWidthRegular,
                PickeTheme.colors.cardBaseBorderDefault,
                shape
            )
            .padding(SpacingTokens.s16),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
    ) {
        ClassResultParticipationRow(
            title = stringResource(R.string.class_result_battle_rate),
            caption = stringResource(R.string.class_result_battle_members),
            count = participatedCount,
            memberCount = memberCount,
            barColor = PickeTheme.colors.primary500
        )
        ClassResultParticipationRow(
            title = stringResource(R.string.class_result_comment_rate),
            caption = stringResource(R.string.class_result_comment_members),
            count = commentedCount,
            memberCount = memberCount,
            barColor = PickeTheme.colors.secondary500
        )
    }
}

@Composable
private fun ClassResultParticipationRow(
    title: String,
    caption: String,
    count: Int,
    memberCount: Int,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val ratio = if (memberCount > 0) (count.toFloat() / memberCount).coerceIn(0f, 1f) else 0f

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = PickeTheme.colors.textDefault,
                    style = PickeTheme.typography.captionLgSemiBold
                )
                Text(
                    text = if (memberCount > 0) {
                        stringResource(R.string.class_result_rate, formatRate(ratio * 100))
                    } else {
                        stringResource(R.string.class_result_no_participation)
                    },
                    color = PickeTheme.colors.textDefault,
                    style = PickeTheme.typography.bodySmSemiBold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = caption,
                    color = PickeTheme.colors.textMuted,
                    style = PickeTheme.typography.captionMdRegular
                )
                Text(
                    text = stringResource(R.string.class_result_member_ratio, count, memberCount),
                    color = PickeTheme.colors.textMuted,
                    style = PickeTheme.typography.captionMdRegular
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(ClassResultParticipationCardDimens.barHeight)
                .clip(RoundedCornerShape(RadiusTokens.max))
                .background(PickeTheme.colors.backgroundSubtle)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(ratio)
                    .fillMaxHeight()
                    .background(barColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassResultParticipationCardPreview() {
    val summary = DummyData.dummyClassResultSummary

    PickeTheme {
        ClassResultParticipationCard(
            memberCount = summary.memberCount,
            participatedCount = summary.participatedCount,
            commentedCount = summary.commentedCount
        )
    }
}

@Preview(name = "참여 대상 없음", showBackground = true)
@Composable
private fun ClassResultParticipationCardEmptyPreview() {
    PickeTheme {
        ClassResultParticipationCard(
            memberCount = 0,
            participatedCount = 0,
            commentedCount = 0
        )
    }
}