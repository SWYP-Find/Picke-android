package com.picke.presentation.ui.todaybattle.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object OpinionCardDimens {
    val minHeight = 140.dp
    val quoteTopSpacing = 10.dp
}

@Composable
fun OpinionCard(
    name: String,
    opinion: String,
    quote: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) PickeTheme.colors.colorBorderSecondary else Color.Transparent
    val backgroundColor = if (isSelected) PickeTheme.colors.colorElementGrayDarker else PickeTheme.colors.colorElementGrayDark
    val shape = RoundedCornerShape(RadiusTokens.default)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = OpinionCardDimens.minHeight)
            .clip(shape)
            .border(
                width = ComponentNumberTokens.borderWidthRegular,
                color = borderColor,
                shape = shape
            )
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = SpacingTokens.s20, vertical = SpacingTokens.s24),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            modifier = Modifier.fillMaxWidth(),
            color = PickeTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            style = PickeTheme.typography.captionSmBold
        )

        Spacer(modifier = Modifier.height(SpacingTokens.s8))
        Text(
            text = opinion,
            modifier = Modifier.fillMaxWidth(),
            color = PickeTheme.colors.textInverse,
            textAlign = TextAlign.Center,
            style = PickeTheme.typography.headingMd
        )

        Spacer(modifier = Modifier.height(OpinionCardDimens.quoteTopSpacing))
        Text(
            text = "\"$quote\"",
            modifier = Modifier.fillMaxWidth(),
            color = PickeTheme.colors.textMuted,
            textAlign = TextAlign.Center,
            style = PickeTheme.typography.bodyXxsMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OpinionCardPreview() {
    PickeTheme {
        OpinionCard(
            name = "test name",
            opinion = "test opinion",
            quote = "test quote",
            isSelected = false,
            onClick = {}
        )
    }
}