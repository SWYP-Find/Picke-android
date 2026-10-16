package com.picke.presentation.ui.vote.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun VoteCommentCountBadge(
    count: Int,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Row(
        modifier = modifier
            .background(PickeTheme.colors.cardBaseBackgroundDefault, shape)
            .border(ComponentNumberTokens.borderWidthRegular, PickeTheme.colors.borderGrayDefault, shape)
            .padding(horizontal = SpacingTokens.s6, vertical = SpacingTokens.s4),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_message),
            contentDescription = stringResource(R.string.vote_comment_count),
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = PickeTheme.colors.cardBaseTextDecription
        )
        Text(
            text = count.toString(),
            color = PickeTheme.colors.cardBaseTextDecription,
            style = PickeTheme.typography.captionLgMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VoteCommentCountBadgePreview() {
    PickeTheme {
        VoteCommentCountBadge(count = 23)
    }
}