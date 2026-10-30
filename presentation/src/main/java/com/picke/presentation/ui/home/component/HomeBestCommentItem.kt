package com.picke.presentation.ui.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.home.model.HomeBestCommentUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private const val STANCE_BADGE_ID = "stance_badge"
private const val CONTENT_MAX_LINES = 2

@Composable
fun HomeBestCommentItem(
    item: HomeBestCommentUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val badgeStyle = PickeTheme.typography.bodyXxsMedium

    val textMeasurer = rememberTextMeasurer()
    val badgeTextSize = remember(item.optionTitle, badgeStyle) {
        textMeasurer.measure(text = item.optionTitle, style = badgeStyle).size
    }
    val inlineContent = remember(item.optionTitle, badgeTextSize, density) {
        val placeholderWidth = with(density) {
            (badgeTextSize.width.toDp() + SpacingTokens.s4 * 2 + SpacingTokens.s4).toSp()
        }
        val placeholderHeight = with(density) {
            (badgeTextSize.height.toDp() + SpacingTokens.s2 * 2).toSp()
        }
        mapOf(
            STANCE_BADGE_ID to InlineTextContent(
                placeholder = Placeholder(
                    width = placeholderWidth,
                    height = placeholderHeight,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                )
            ) {
                PickeBadge(
                    text = item.optionTitle,
                    horizontalPadding = SpacingTokens.s4,
                    textStyle = badgeStyle
                )
            }
        )
    }
    val content = remember(item.content) {
        buildAnnotatedString {
            appendInlineContent(STANCE_BADGE_ID)
            append(item.content)
        }
    }

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
                text = content,
                modifier = Modifier.padding(horizontal = SpacingTokens.s2),
                color = PickeTheme.colors.textSubtler,
                overflow = TextOverflow.Ellipsis,
                maxLines = CONTENT_MAX_LINES,
                inlineContent = inlineContent,
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