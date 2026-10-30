package com.picke.presentation.ui.classroom.classcreate.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassCreateSelectionHeader(
    resultCount: Int,
    showsRegenerate: Boolean,
    isRegenerateEnabled: Boolean,
    onRegenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.class_create_result_count, resultCount),
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.bodySmSemiBold
        )

        if (showsRegenerate) {
            Row(
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    enabled = isRegenerateEnabled,
                    onClick = onRegenerateClick
                ),
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_replay),
                    contentDescription = null,
                    modifier = Modifier.size(ComponentNumberTokens.iconSm),
                    tint = PickeTheme.colors.textMuted
                )
                Text(
                    text = stringResource(R.string.class_create_regenerate),
                    color = PickeTheme.colors.textMuted,
                    style = PickeTheme.typography.bodyXsSemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassCreateSelectionHeaderPreview() {
    PickeTheme {
        ClassCreateSelectionHeader(
            resultCount = 3,
            showsRegenerate = true,
            isRegenerateEnabled = true,
            onRegenerateClick = {},
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}