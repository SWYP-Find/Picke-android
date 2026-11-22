package com.picke.presentation.ui.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.picke.presentation.R
import com.picke.presentation.ui.home.model.HomeBestCommentUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private const val CONTENT_MAX_LINES = 2

private object HomeBestCommentItemDimens {
    val contentLineHeight = 20.2.sp
}

@Composable
fun HomeBestCommentItem(
    item: HomeBestCommentUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(SpacingTokens.s16),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
    ) {
        Text(
            text = item.battleTitle,
            color = PickeTheme.colors.textMuted,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = PickeTheme.typography.bodyXsSemiBold
        )

        Row {
            Icon(
                painter = painterResource(R.drawable.ic_reply_corner),
                contentDescription = null,
                modifier = Modifier.size(ComponentNumberTokens.iconSm),
                tint = PickeTheme.colors.primary500
            )
            Text(
                text = item.content,
                modifier = Modifier.padding(horizontal = SpacingTokens.s2),
                color = PickeTheme.colors.textSubtler,
                lineHeight = HomeBestCommentItemDimens.contentLineHeight,
                overflow = TextOverflow.Ellipsis,
                maxLines = CONTENT_MAX_LINES,
                style = PickeTheme.typography.bodyXsRegular
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeBestCommentItemPreview() {
    PickeTheme {
        Column {
            DummyData.dummyBestComments.forEach { item ->
                HomeBestCommentItem(
                    item = item,
                    onClick = { }
                )
            }
        }
    }
}