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
import com.picke.presentation.ui.classroom.classresult.model.ClassFeedbackCoverage
import com.picke.presentation.ui.classroom.classresult.model.ClassReportState
import com.picke.presentation.ui.classroom.classresult.model.ClassResultReportUiModel
import com.picke.presentation.ui.classroom.component.rememberClassDeadlineText
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import java.time.LocalDateTime

@Composable
fun ClassResultFeedbackCard(
    report: ClassResultReportUiModel,
    deadline: LocalDateTime?,
    onMyOpinionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deadlineText = rememberClassDeadlineText(deadline, R.string.class_deadline_full_format)

    val notice = report.feedbackNotice(deadlineText)
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
        if (report.judgmentBasis.isNotBlank()) {
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
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
        ) {
            if (notice != null) {
                Text(
                    text = notice,
                    color = PickeTheme.colors.textMuted,
                    style = PickeTheme.typography.bodyXsRegular
                )
                HorizontalDivider(
                    thickness = ComponentNumberTokens.borderWidthRegular,
                    color = PickeTheme.colors.cardBaseBorderDefault
                )
            }

            report.feedbackItems.forEach { item ->
                ClassResultFeedbackItem(
                    title = stringResource(item.titleRes),
                    content = item.content
                )
                HorizontalDivider(
                    thickness = ComponentNumberTokens.borderWidthRegular,
                    color = PickeTheme.colors.cardBaseBorderDefault
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = onMyOpinionClick
                    )
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

@Composable
private fun ClassResultReportUiModel.feedbackNotice(deadlineText: String): String? = when (state) {
    ClassReportState.BEFORE_CLOSE -> stringResource(
        R.string.class_result_ai_before_close,
        deadlineText
    )

    ClassReportState.NOT_ELIGIBLE -> stringResource(R.string.class_result_report_not_eligible)

    ClassReportState.PENDING, ClassReportState.GENERATING -> stringResource(R.string.class_debate_generating)

    ClassReportState.SUCCEEDED -> if (coverage == ClassFeedbackCoverage.PARTIAL) {
        stringResource(R.string.class_result_report_partial)
    } else {
        null
    }

    ClassReportState.LIMITED -> stringResource(R.string.class_result_report_limited)

    ClassReportState.FAILED -> stringResource(R.string.class_result_insight_failed)
}

@Preview(name = "생성 완료", showBackground = true)
@Composable
private fun ClassResultFeedbackCardPreview() {
    ClassResultFeedbackCardStatePreview(report = DummyData.dummyClassResultReport)
}

@Preview(name = "일부 발언만 분석", showBackground = true)
@Composable
private fun ClassResultFeedbackCardPartialPreview() {
    ClassResultFeedbackCardStatePreview(
        report = DummyData.dummyClassResultReport.copy(coverage = ClassFeedbackCoverage.PARTIAL)
    )
}

@Preview(name = "자료 부족", showBackground = true)
@Composable
private fun ClassResultFeedbackCardLimitedPreview() {
    ClassResultFeedbackCardStatePreview(
        report = DummyData.dummyClassResultReport.copy(
            state = ClassReportState.LIMITED,
            judgmentBasis = "",
            feedbackItems = DummyData.dummyClassResultReport.feedbackItems.takeLast(1)
        )
    )
}

@Preview(name = "마감 전", showBackground = true)
@Composable
private fun ClassResultFeedbackCardBeforeClosePreview() {
    ClassResultFeedbackCardStatePreview(
        report = DummyData.dummyClassResultReport.copy(
            state = ClassReportState.BEFORE_CLOSE,
            judgmentBasis = "",
            feedbackItems = emptyList()
        )
    )
}

@Composable
private fun ClassResultFeedbackCardStatePreview(report: ClassResultReportUiModel) {
    val classItem = DummyData.dummyMyClasses.first()

    PickeTheme {
        ClassResultFeedbackCard(
            report = report,
            deadline = classItem.deadline,
            onMyOpinionClick = {}
        )
    }
}