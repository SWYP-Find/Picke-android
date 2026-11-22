package com.picke.presentation.ui.classroom.classresult.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun ClassResultOpinionCard(
    bestComment: String,
    mostDebatedComment: String,
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
        ClassResultOpinionItem(
            label = stringResource(R.string.class_result_best_comment),
            comment = bestComment
        )
        HorizontalDivider(
            thickness = ComponentNumberTokens.borderWidthRegular,
            color = PickeTheme.colors.cardBaseBorderDefault
        )
        ClassResultOpinionItem(
            label = stringResource(R.string.class_result_most_debated_comment),
            comment = mostDebatedComment
        )
    }
}

@Composable
private fun ClassResultOpinionItem(
    label: String,
    comment: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpacingTokens.s16),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
    ) {
        PickeBadge(text = label)
        Text(
            text = stringResource(R.string.class_result_quote, comment),
            modifier = Modifier.padding(horizontal = SpacingTokens.s2),
            color = PickeTheme.colors.textBody,
            style = PickeTheme.typography.bodyXsRegular
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassResultOpinionCardPreview() {
    val insight = DummyData.dummyClassResultInsight

    PickeTheme {
        ClassResultOpinionCard(
            bestComment = insight.bestComment,
            mostDebatedComment = insight.mostDebatedComment
        )
    }
}