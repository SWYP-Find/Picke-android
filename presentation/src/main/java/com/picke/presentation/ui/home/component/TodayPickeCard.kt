package com.picke.presentation.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.home.model.PollQuizOptionStatUiModel
import com.picke.presentation.ui.home.model.TodayPickUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData

@Composable
fun TodayPickeCard(
    item: TodayPickUiModel,
    onVoteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (item) {
        is TodayPickUiModel.VotePick -> PickeTheme.colors.surfaceBeigeDefault
        is TodayPickUiModel.QuizPick -> PickeTheme.colors.surfaceBeigeStrong
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(backgroundColor)
            .border(1.dp, PickeTheme.colors.borderBeigeSelected, RoundedCornerShape(1.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val typeName = if (item is TodayPickUiModel.VotePick) "투표" else "퀴즈"

            Surface(color = PickeTheme.colors.borderBeigeDefault, shape = RoundedCornerShape(2.dp)) {
                Text(
                    text = "#$typeName",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = PickeTheme.typography.captionLgMedium,
                    color = PickeTheme.colors.primary500
                )
            }
            Text(
                text = "${item.participantsCount}명 참여",
                style = PickeTheme.typography.captionLgMedium,
                color = PickeTheme.colors.gray400
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (item) {
            is TodayPickUiModel.VotePick -> VotePickeContent(item, onVoteClick)
            is TodayPickUiModel.QuizPick -> QuizPickeContent(item, onVoteClick)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VotePickeContent(item: TodayPickUiModel.VotePick, onVoteClick: (Long) -> Unit) {
    val isVoted = item.selectedOptionId != null
    val selectedOptionText = item.options.find { it.optionId == item.selectedOptionId }?.title ?: ""

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.titlePrefix,
                style = PickeTheme.typography.bodySmSemiBold,
                color = PickeTheme.colors.textDefault,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(min = 80.dp)
                    .height(28.dp)
                    .border(
                        width = 1.dp,
                        color = PickeTheme.colors.borderBeigeSelected,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .background(PickeTheme.colors.backgroundBeige)
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isVoted) selectedOptionText else "",
                    style = PickeTheme.typography.bodySmSemiBold,
                    color = PickeTheme.colors.primary500,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = item.titleSuffix,
                style = PickeTheme.typography.bodySmSemiBold,
                color = PickeTheme.colors.textDefault,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.summary,
            style = PickeTheme.typography.captionLgMedium,
            color = PickeTheme.colors.gray200,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val rows = item.options.chunked(2)
            rows.forEachIndexed { rowIndex, rowOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowOptions.forEachIndexed { colIndex, option ->
                        val indexNum = (rowIndex * 2) + colIndex + 1
                        val isSelected = item.selectedOptionId == option.optionId

                        PickeGridButton(
                            modifier = Modifier.weight(1f),
                            index = indexNum.toString(),
                            text = option.title,
                            isSelected = isSelected,
                            isVoted = isVoted,
                            onClick = { onVoteClick(option.optionId) },
                        )
                    }
                }
            }
        }

        if (isVoted) {
            Spacer(modifier = Modifier.height(24.dp))
            val statRows = item.options.chunked(2)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                statRows.forEach { rowOptions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        rowOptions.forEach { option ->
                            PollStatBar(modifier = Modifier.weight(1f), option = option)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizPickeContent(item: TodayPickUiModel.QuizPick, onVoteClick: (Long) -> Unit) {
    val isVoted = item.selectedOptionId != null

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = item.title,
            style = PickeTheme.typography.bodySmSemiBold,
            color = PickeTheme.colors.textDefault,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.summary,
            style = PickeTheme.typography.captionLgMedium,
            color = PickeTheme.colors.gray200,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item.options.forEach { option ->
                QuizOptionCard(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    option = option,
                    isVoted = isVoted,
                    selectedOptionId = item.selectedOptionId,
                    onClick = { onVoteClick(option.optionId) }
                )
            }
        }
    }
}

@Composable
private fun PickeGridButton(
    modifier: Modifier,
    index: String,
    text: String,
    isVoted: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor =
        if (isSelected) PickeTheme.colors.secondary500 else PickeTheme.colors.borderBeigeDefault

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surfaceBeigeSubtle)
            .border(1.dp, borderColor, RoundedCornerShape(1.dp))
            .clickable(enabled = !isVoted) { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$index. ",
            style = PickeTheme.typography.captionLgMedium,
            color = PickeTheme.colors.beige900
        )
        Text(
            text = text,
            style = PickeTheme.typography.bodyXsMedium,
            color = PickeTheme.colors.textDefault
        )
    }
}

@Composable
private fun PollStatBar(modifier: Modifier, option: PollQuizOptionStatUiModel) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = option.title,
            style = PickeTheme.typography.captionSmSemiBold,
            color = PickeTheme.colors.gray400,
            modifier = Modifier.width(44.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
                .padding(horizontal = 4.dp)
                .background(PickeTheme.colors.secondary100, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(option.ratio / 100f)
                    .height(4.dp)
                    .background(PickeTheme.colors.secondary500, CircleShape)
            )
        }

        Text(
            text = "${option.ratio.toInt()}%",
            style = PickeTheme.typography.captionMdSemiBold,
            color = PickeTheme.colors.textDefault,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.width(36.dp)
        )
    }
}

@Composable
private fun QuizOptionCard(
    modifier: Modifier,
    option: PollQuizOptionStatUiModel,
    selectedOptionId: Long?,
    isVoted: Boolean,
    onClick: () -> Unit
) {
    val isMySelection = isVoted && option.optionId == selectedOptionId
    val borderColor = when {
        !isVoted -> PickeTheme.colors.borderBeigeDisabled
        isMySelection -> if (option.isCorrect) PickeTheme.colors.secondary500 else PickeTheme.colors.primary500 // 정답이면 초록, 오답이면 빨강
        else -> PickeTheme.colors.borderBeigeDisabled
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
            .border(1.dp, borderColor, RoundedCornerShape(1.dp))
            .clickable(enabled = !isVoted) { onClick() }
            .padding(vertical = 16.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isVoted) {
            val resultColor =
                if (option.isCorrect) PickeTheme.colors.secondary500 else PickeTheme.colors.primary500
            val resultText = if (option.isCorrect) "O 정답" else "X 오답"

            Text(text = resultText, style = PickeTheme.typography.captionLgMedium, color = resultColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = option.title,
                style = PickeTheme.typography.bodyXsMedium,
                color = PickeTheme.colors.textDefault,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
        } else {
            Text(
                text = option.title,
                style = PickeTheme.typography.bodyXsMedium,
                color = PickeTheme.colors.textDefault,
                textAlign = TextAlign.Center
            )
            if (option.stance.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = option.stance,
                    style = PickeTheme.typography.captionSmSemiBold,
                    color = PickeTheme.colors.gray400,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodayPickeCardPreview() {
    PickeTheme {
        TodayPickeCard(
            item = DummyData.dummyVotePick,
            onVoteClick = {}
        )
    }
}