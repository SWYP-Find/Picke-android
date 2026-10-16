package com.picke.presentation.ui.classroom.classmyopinion.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyReplyUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

@Composable
fun ClassMyReplyCard(
    reply: ClassMyReplyUiModel,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.cardBaseBackgroundDefault, shape)
            .border(ComponentNumberTokens.borderWidthRegular, PickeTheme.colors.cardBaseBorderDefault, shape)
            .padding(SpacingTokens.s16),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PickeTheme.colors.surfaceBeigeSubtle, shape)
                .padding(SpacingTokens.s12),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
                Text(
                    text = reply.parentAuthorName,
                    color = PickeTheme.colors.textSubtler,
                    style = PickeTheme.typography.bodyXxsSemiBold
                )
                Text(
                    text = stringResource(R.string.class_my_opinion_parent_stance, reply.parentStance),
                    color = PickeTheme.colors.textMuted,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = PickeTheme.typography.bodyXxsSemiBold
                )
            }
            Text(
                text = reply.parentContent,
                color = PickeTheme.colors.textMuted,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = PickeTheme.typography.bodyXxsRegular
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_reply),
                    contentDescription = null,
                    modifier = Modifier.size(ComponentNumberTokens.iconXs),
                    tint = PickeTheme.colors.textPrimary
                )
                Text(
                    text = stringResource(R.string.class_my_opinion_reply_author, reply.authorName),
                    color = PickeTheme.colors.textPrimary,
                    style = PickeTheme.typography.captionLgSemiBold
                )
            }
            Text(
                text = reply.content,
                color = PickeTheme.colors.textBody,
                style = PickeTheme.typography.bodyXsRegular
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassMyReplyCardPreview() {
    PickeTheme {
        ClassMyReplyCard(reply = DummyData.dummyClassMyReplies.first())
    }
}