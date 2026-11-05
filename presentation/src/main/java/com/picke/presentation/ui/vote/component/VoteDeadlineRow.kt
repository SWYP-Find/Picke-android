package com.picke.presentation.ui.vote.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun VoteDeadlineRow(
    deadlineText: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = null,
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = PickeTheme.colors.cardBaseTextDecription
        )
        Text(
            text = deadlineText,
            color = PickeTheme.colors.cardBaseTextDecription,
            style = PickeTheme.typography.bodyXsRegular
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VoteDeadlineRowPreview() {
    PickeTheme {
        VoteDeadlineRow(deadlineText = "2026. 10. 11. 일요일 23:59까지")
    }
}