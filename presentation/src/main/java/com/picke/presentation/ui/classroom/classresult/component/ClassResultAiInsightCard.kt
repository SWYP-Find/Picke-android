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
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun ClassResultAiInsightCard(
    insight: String,
    modifier: Modifier = Modifier
) {
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
            text = insight,
            color = PickeTheme.colors.textPrimary,
            style = PickeTheme.typography.captionLgRegular
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassResultAiInsightCardPreview() {
    PickeTheme {
        ClassResultAiInsightCard(insight = DummyData.dummyClassResultSummary.aiInsight)
    }
}