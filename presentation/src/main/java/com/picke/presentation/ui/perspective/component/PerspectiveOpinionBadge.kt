package com.picke.presentation.ui.perspective.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PerspectiveOpinionBadgeDimens {
    val minHeight = 20.dp
}

@Composable
fun PerspectiveOpinionBadge(
    opinionChanged: Boolean,
    modifier: Modifier = Modifier
) {
    // TODO: 토큰 동기화 - color/element/primary-lighter
    Row(
        modifier = modifier
            .heightIn(min = PerspectiveOpinionBadgeDimens.minHeight)
            .background(PickeTheme.colors.primary50, RoundedCornerShape(RadiusTokens.default))
            .padding(horizontal = SpacingTokens.s4),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_think),
            contentDescription = null,
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = PickeTheme.colors.textPrimary
        )
        Text(
            text = stringResource(if (opinionChanged) R.string.comment_opinion_changed else R.string.comment_opinion_same),
            color = PickeTheme.colors.textPrimary,
            style = PickeTheme.typography.bodyXxsSemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PerspectiveOpinionBadgePreview() {
    PickeTheme {
        PerspectiveOpinionBadge(opinionChanged = true)
    }
}