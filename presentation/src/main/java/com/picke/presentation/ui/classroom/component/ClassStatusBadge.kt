package com.picke.presentation.ui.classroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassStatusBadge(
    isInProgress: Boolean,
    modifier: Modifier = Modifier
) {
    val textRes =
        if (isInProgress) R.string.my_class_status_in_progress else R.string.my_class_status_ended
    val backgroundColor =
        if (isInProgress) PickeTheme.colors.badgeFilledBackground else PickeTheme.colors.backgroundSubtler
    val textColor =
        if (isInProgress) PickeTheme.colors.badgeFilledText else PickeTheme.colors.textMuted

    Text(
        text = stringResource(textRes),
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(RadiusTokens.default))
            .padding(horizontal = SpacingTokens.s6, vertical = SpacingTokens.s2),
        color = textColor,
        style = PickeTheme.typography.bodyXxsSemiBold
    )
}

@Preview(showBackground = true)
@Composable
private fun ClassStatusBadgePreview() {
    PickeTheme {
        Row(
            modifier = Modifier.padding(SpacingTokens.s16),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
        ) {
            ClassStatusBadge(isInProgress = true)
            ClassStatusBadge(isInProgress = false)
        }
    }
}