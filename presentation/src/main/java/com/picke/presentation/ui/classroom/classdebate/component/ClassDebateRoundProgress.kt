package com.picke.presentation.ui.classroom.classdebate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassDebateRoundProgressDimens {
    val barWidth = 50.dp
    val barHeight = 4.dp
    val barSpacing = 5.dp
}

@Composable
fun ClassDebateRoundProgress(
    currentRound: Int,
    totalRounds: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.class_debate_round_format, currentRound),
            color = PickeTheme.colors.textMuted,
            style = PickeTheme.typography.captionLgBold
        )

        Row(horizontalArrangement = Arrangement.spacedBy(ClassDebateRoundProgressDimens.barSpacing)) {
            repeat(totalRounds) { index ->
                val barColor = if (index < currentRound) PickeTheme.colors.primary500 else PickeTheme.colors.beige600

                Box(
                    modifier = Modifier
                        .size(ClassDebateRoundProgressDimens.barWidth, ClassDebateRoundProgressDimens.barHeight)
                        .background(barColor, RoundedCornerShape(RadiusTokens.default))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassDebateRoundProgressPreview() {
    PickeTheme {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)) {
            ClassDebateRoundProgress(currentRound = 1, totalRounds = 3)
            ClassDebateRoundProgress(currentRound = 3, totalRounds = 3)
        }
    }
}