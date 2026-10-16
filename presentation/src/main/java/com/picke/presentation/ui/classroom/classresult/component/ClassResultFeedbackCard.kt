package com.picke.presentation.ui.classroom.classresult.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classresult.model.ClassResultReportUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun ClassResultFeedbackCard(
    report: ClassResultReportUiModel,
    onMyOpinionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PickeTheme.colors.surfacePrimarySubtle)
                .padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
        ) {
            ClassResultAiLabel(text = stringResource(R.string.class_result_judgment_basis))
            Text(
                text = report.judgmentBasis,
                color = PickeTheme.colors.textPrimary,
                style = PickeTheme.typography.bodyXxsRegular
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
        ) {
            ClassResultFeedbackItem(
                title = stringResource(R.string.class_result_strength),
                content = report.strength
            )
            HorizontalDivider(
                thickness = ComponentNumberTokens.borderWidthRegular,
                color = PickeTheme.colors.cardBaseBorderDefault
            )

            ClassResultFeedbackItem(
                title = stringResource(R.string.class_result_improvement),
                content = report.improvement
            )
            HorizontalDivider(
                thickness = ComponentNumberTokens.borderWidthRegular,
                color = PickeTheme.colors.cardBaseBorderDefault
            )

            ClassResultFeedbackItem(
                title = stringResource(R.string.class_result_next_thought),
                content = report.nextThought
            )
            HorizontalDivider(
                thickness = ComponentNumberTokens.borderWidthRegular,
                color = PickeTheme.colors.cardBaseBorderDefault
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onMyOpinionClick)
                    .padding(vertical = SpacingTokens.s6),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.class_result_my_opinion_link,
                        report.myOpinionCount
                    ),
                    color = PickeTheme.colors.textPrimary,
                    style = PickeTheme.typography.captionLgBold
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right_a),
                    contentDescription = null,
                    modifier = Modifier.size(ComponentNumberTokens.iconXs),
                    tint = PickeTheme.colors.textPrimary
                )
            }
        }
    }
}

@Composable
private fun ClassResultFeedbackItem(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)
    ) {
        Text(
            text = title,
            color = PickeTheme.colors.textPrimary,
            style = PickeTheme.typography.captionLgSemiBold
        )
        Text(
            text = content,
            color = PickeTheme.colors.textSubtle,
            style = PickeTheme.typography.bodyXsRegular
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassResultFeedbackCardPreview() {
    PickeTheme {
        ClassResultFeedbackCard(
            report = DummyData.dummyClassResultReport,
            onMyOpinionClick = {}
        )
    }
}