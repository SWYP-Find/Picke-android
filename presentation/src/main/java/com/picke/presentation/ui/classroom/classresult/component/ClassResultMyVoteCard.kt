package com.picke.presentation.ui.classroom.classresult.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classresult.model.ClassResultReportUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun ClassResultMyVoteCard(
    report: ClassResultReportUiModel,
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
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = SpacingTokens.s16,
                    top = SpacingTokens.s16,
                    end = SpacingTokens.s16
                )
                .fillMaxWidth()
                .background(PickeTheme.colors.surfaceBeigeSubtle, shape)
                .padding(vertical = SpacingTokens.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClassResultVoteColumn(
                label = stringResource(R.string.class_result_pre_vote),
                stance = report.preVoteStance,
                modifier = Modifier.weight(1f)
            )
            if (report.isStanceChanged) {
                Text(
                    text = stringResource(R.string.class_result_stance_changed),
                    modifier = Modifier
                        .background(
                            PickeTheme.colors.primary500,
                            RoundedCornerShape(RadiusTokens.max)
                        )
                        .padding(horizontal = SpacingTokens.s6, vertical = SpacingTokens.s2),
                    color = PickeTheme.colors.textInverse,
                    style = PickeTheme.typography.captionSmSemiBold
                )
            }
            ClassResultVoteColumn(
                label = stringResource(R.string.class_result_post_vote),
                stance = report.postVoteStance,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(SpacingTokens.s16)
        ) {
            ClassResultStatColumn(
                count = report.perspectiveCount,
                label = stringResource(R.string.class_result_comment_count),
                modifier = Modifier.weight(1f)
            )
            VerticalDivider(
                thickness = ComponentNumberTokens.borderWidthRegular,
                color = PickeTheme.colors.cardBaseBorderDefault
            )
            ClassResultStatColumn(
                count = report.commentCount,
                label = stringResource(R.string.class_result_reply_count),
                modifier = Modifier.weight(1f)
            )
            if (report.receivedLikeCount != null) {
                VerticalDivider(
                    thickness = ComponentNumberTokens.borderWidthRegular,
                    color = PickeTheme.colors.cardBaseBorderDefault
                )
                ClassResultStatColumn(
                    count = report.receivedLikeCount,
                    label = stringResource(R.string.class_result_received_like_count),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ClassResultVoteColumn(
    label: String,
    stance: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = PickeTheme.colors.textMuted,
            style = PickeTheme.typography.captionMdMedium
        )
        Text(
            text = stance,
            color = PickeTheme.colors.textDefault,
            textAlign = TextAlign.Center,
            style = PickeTheme.typography.captionLgSemiBold
        )
    }
}

@Composable
private fun ClassResultStatColumn(
    count: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.bodyLgBold
        )
        Text(
            text = label,
            color = PickeTheme.colors.cardBaseTextDecription,
            style = PickeTheme.typography.captionSmMedium
        )
    }
}

@Preview(name = "입장 변경", showBackground = true)
@Composable
private fun ClassResultMyVoteCardPreview() {
    PickeTheme {
        ClassResultMyVoteCard(report = DummyData.dummyClassResultReport)
    }
}

@Preview(name = "입장 유지", showBackground = true)
@Composable
private fun ClassResultMyVoteCardKeptPreview() {
    val report = DummyData.dummyClassResultReport

    PickeTheme {
        ClassResultMyVoteCard(report = report.copy(postVoteStance = report.preVoteStance))
    }
}