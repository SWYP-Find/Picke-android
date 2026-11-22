package com.picke.presentation.ui.my.ticket.ticketregister.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object TicketCodeGuideItemDimens {
    val answerTopSpacing = 10.dp
}

private const val EXPANDED_ARROW_ROTATION = 180f

@Composable
fun TicketCodeGuideItem(
    question: String,
    answer: String,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false
) {
    var isExpanded by rememberSaveable { mutableStateOf(initiallyExpanded) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(TicketCodeGuideItemDimens.answerTopSpacing)
    ) {
        Row(
            modifier = Modifier.clickable(
                interactionSource = null,
                indication = null,
                onClick = { isExpanded = !isExpanded }
            ),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question,
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.bodyXxsRegular
            )
            Icon(
                painter = painterResource(R.drawable.ic_arrow_down),
                contentDescription = null,
                modifier = Modifier
                    .size(ComponentNumberTokens.iconXs)
                    .rotate(if (isExpanded) EXPANDED_ARROW_ROTATION else 0f),
                tint = PickeTheme.colors.textMuted
            )
        }

        if (isExpanded) {
            Text(
                text = answer,
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.bodyXxsRegular
            )
        }
    }
}

@Preview(name = "접힘", showBackground = true)
@Composable
private fun TicketCodeGuideItemPreview() {
    PickeTheme {
        TicketCodeGuideItem(
            question = "이용권 코드는 어디서 확인하나요?",
            answer = "펀딩 리워드 발송 안내에서 확인해 주세요."
        )
    }
}

@Preview(name = "펼침", showBackground = true)
@Composable
private fun TicketCodeGuideItemExpandedPreview() {
    PickeTheme {
        TicketCodeGuideItem(
            question = "이용권 코드는 어디서 확인하나요?",
            answer = "펀딩 리워드 발송 안내에서 확인해 주세요. 코드를 받지 못했다면 해당 펀딩의 문의 경로를 이용해 주세요.",
            initiallyExpanded = true
        )
    }
}