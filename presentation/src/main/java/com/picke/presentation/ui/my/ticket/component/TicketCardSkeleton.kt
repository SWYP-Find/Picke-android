package com.picke.presentation.ui.my.ticket.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.PickeSkeletonBlock
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object TicketCardSkeletonDimens {
    val cardHeight = 132.dp
}

@Composable
fun TicketCardSkeleton(modifier: Modifier = Modifier) {
    PickeSkeletonBlock(
        height = TicketCardSkeletonDimens.cardHeight,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun TicketCardSkeletonPreview() {
    PickeTheme {
        TicketCardSkeleton(modifier = Modifier.padding(SpacingTokens.s16))
    }
}