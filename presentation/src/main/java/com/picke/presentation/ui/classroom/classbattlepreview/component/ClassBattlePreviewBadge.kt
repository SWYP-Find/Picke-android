package com.picke.presentation.ui.classroom.classbattlepreview.component

import androidx.compose.foundation.background
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
fun ClassBattlePreviewBadge(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(
                PickeTheme.colors.surfacePrimarySubtle,
                RoundedCornerShape(RadiusTokens.max)
            )
            .padding(horizontal = SpacingTokens.s8, vertical = SpacingTokens.s2),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_play_badge),
            contentDescription = null,
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = PickeTheme.colors.textPrimary
        )
        Text(
            text = stringResource(R.string.class_battle_preview_badge),
            color = PickeTheme.colors.textPrimary,
            style = PickeTheme.typography.bodySmSemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassBattlePreviewBadgePreview() {
    PickeTheme {
        ClassBattlePreviewBadge(modifier = Modifier.padding(SpacingTokens.s16))
    }
}