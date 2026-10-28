package com.picke.presentation.ui.classroom.classresult.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classresult.model.ClassReportState
import com.picke.presentation.ui.classroom.classresult.model.ClassResultInsightUiModel
import com.picke.presentation.ui.classroom.component.rememberClassDeadlineText
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import java.time.LocalDateTime

@Composable
fun ClassResultAiInsightCard(
    insight: ClassResultInsightUiModel,
    deadline: LocalDateTime?,
    modifier: Modifier = Modifier
) {
    val deadlineText = rememberClassDeadlineText(deadline, R.string.class_deadline_full_format)

    val shape = RoundedCornerShape(RadiusTokens.default)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.surfacePrimarySubtle, shape)
            .border(ComponentNumberTokens.borderWidthRegular, PickeTheme.colors.primary100, shape)
            .padding(SpacingTokens.s12),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
    ) {
        ClassResultAiLabel(
            text = stringResource(R.string.class_result_ai_insight),
            style = PickeTheme.typography.captionMdBold
        )
        Text(
            text = insight.insightText(deadlineText),
            color = PickeTheme.colors.textPrimary,
            style = PickeTheme.typography.captionLgRegular
        )
    }
}

@Composable
private fun ClassResultInsightUiModel.insightText(deadlineText: String): String = when (state) {
    ClassReportState.BEFORE_CLOSE -> stringResource(
        R.string.class_result_ai_before_close,
        deadlineText
    )

    ClassReportState.PENDING, ClassReportState.GENERATING -> stringResource(R.string.class_debate_generating)

    ClassReportState.SUCCEEDED -> insightText

    ClassReportState.LIMITED -> stringResource(R.string.class_result_insight_limited)

    ClassReportState.FAILED -> stringResource(R.string.class_result_insight_failed)
}

@Preview(name = "생성 완료", showBackground = true)
@Composable
private fun ClassResultAiInsightCardPreview() {
    ClassResultAiInsightCardStatePreview(state = ClassReportState.SUCCEEDED)
}

@Preview(name = "마감 전", showBackground = true)
@Composable
private fun ClassResultAiInsightCardBeforeClosePreview() {
    ClassResultAiInsightCardStatePreview(state = ClassReportState.BEFORE_CLOSE)
}

@Preview(name = "생성 중", showBackground = true)
@Composable
private fun ClassResultAiInsightCardGeneratingPreview() {
    ClassResultAiInsightCardStatePreview(state = ClassReportState.GENERATING)
}

@Preview(name = "자료 부족", showBackground = true)
@Composable
private fun ClassResultAiInsightCardLimitedPreview() {
    ClassResultAiInsightCardStatePreview(state = ClassReportState.LIMITED)
}

@Preview(name = "실패", showBackground = true)
@Composable
private fun ClassResultAiInsightCardFailedPreview() {
    ClassResultAiInsightCardStatePreview(state = ClassReportState.FAILED)
}

@Composable
private fun ClassResultAiInsightCardStatePreview(state: ClassReportState) {
    val classItem = DummyData.dummyMyClasses.first()

    PickeTheme {
        ClassResultAiInsightCard(
            insight = DummyData.dummyClassResultInsight.copy(state = state),
            deadline = classItem.deadline
        )
    }
}