package com.picke.presentation.ui.classroom.classresult.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classresult.model.ClassResultStanceUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.formatRate
import kotlin.math.roundToInt

private object ClassResultStanceCardDimens {
    val chartSize = 100.dp
    val chartStrokeWidth = 12.dp
    val legendDotSize = 8.dp
}

private const val CHART_START_ANGLE = -90f
private const val FULL_ANGLE = 360f
private const val TOTAL_TENTHS = 1000
private const val TENTHS_PER_PERCENT = 10f

@Composable
fun ClassResultStanceCard(
    stances: List<ClassResultStanceUiModel>,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    val stanceColors = listOf(PickeTheme.colors.primary500, PickeTheme.colors.secondary500)
    val totalCount = stances.sumOf { it.count }
    val rates = stanceRates(stances.map { it.count })

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.cardBaseBackgroundDefault, shape)
            .border(
                ComponentNumberTokens.borderWidthRegular,
                PickeTheme.colors.cardBaseBorderDefault,
                shape
            )
            .padding(SpacingTokens.s16),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s24),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(ClassResultStanceCardDimens.chartSize),
            contentAlignment = Alignment.Center
        ) {
            ClassResultStanceChart(
                counts = stances.map { it.count },
                colors = stanceColors,
                trackColor = PickeTheme.colors.backgroundSubtle
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.class_result_people_count, totalCount),
                    color = PickeTheme.colors.textDefault,
                    style = PickeTheme.typography.bodyLgBold
                )
                Text(
                    text = stringResource(R.string.class_result_total_label),
                    color = PickeTheme.colors.textMuted,
                    style = PickeTheme.typography.captionSmMedium
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
        ) {
            stances.forEachIndexed { index, stance ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(ClassResultStanceCardDimens.legendDotSize)
                            .background(stanceColors[index % stanceColors.size], CircleShape)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stance.label,
                            color = PickeTheme.colors.textDefault,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = PickeTheme.typography.captionLgSemiBold
                        )
                        Text(
                            text = stringResource(R.string.class_result_people_count, stance.count),
                            color = PickeTheme.colors.textMuted,
                            style = PickeTheme.typography.captionMdMedium
                        )
                    }

                    Text(
                        text = if (rates != null) {
                            stringResource(R.string.class_result_rate, formatRate(rates[index]))
                        } else {
                            stringResource(R.string.class_result_no_participation)
                        },
                        color = PickeTheme.colors.textDefault,
                        style = PickeTheme.typography.captionLgBold
                    )
                }
            }
        }
    }
}

@Composable
private fun ClassResultStanceChart(
    counts: List<Int>,
    colors: List<Color>,
    trackColor: Color,
    modifier: Modifier = Modifier
) {
    val totalCount = counts.sum()

    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidth = ClassResultStanceCardDimens.chartStrokeWidth.toPx()
        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
        val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

        if (totalCount == 0) {
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = FULL_ANGLE,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(strokeWidth)
            )
            return@Canvas
        }

        var startAngle = CHART_START_ANGLE
        counts.forEachIndexed { index, count ->
            val sweepAngle = FULL_ANGLE * count / totalCount
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(strokeWidth)
            )
            startAngle += sweepAngle
        }
    }
}

private fun stanceRates(counts: List<Int>): List<Float>? {
    val totalCount = counts.sum()
    if (totalCount == 0) return null

    val leadingTenths = counts.dropLast(1).map { (it * TOTAL_TENTHS.toFloat() / totalCount).roundToInt() }
    val lastTenths = TOTAL_TENTHS - leadingTenths.sum()
    return (leadingTenths + lastTenths).map { it / TENTHS_PER_PERCENT }
}

@Preview(showBackground = true)
@Composable
private fun ClassResultStanceCardPreview() {
    PickeTheme {
        ClassResultStanceCard(stances = DummyData.dummyClassResultSummary.stances)
    }
}

@Preview(name = "사후투표 없음", showBackground = true)
@Composable
private fun ClassResultStanceCardEmptyPreview() {
    val stances = DummyData.dummyClassResultSummary.stances.map { it.copy(count = 0) }

    PickeTheme {
        ClassResultStanceCard(stances = stances)
    }
}