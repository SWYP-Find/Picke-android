package com.picke.presentation.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PickeLoadFailedSectionDimens {
    val borderWidth = 1.dp
    val iconSize = 16.dp
}

@Composable
fun PickeLoadFailedSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .border(
                width = PickeLoadFailedSectionDimens.borderWidth,
                color = PickeTheme.colors.borderBeigeDefault,
                shape = RoundedCornerShape(RadiusTokens.default)
            )
            .padding(SpacingTokens.s16),
        horizontalArrangement = Arrangement.spacedBy(
            SpacingTokens.s8,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_warning),
            contentDescription = null,
            modifier = Modifier.size(PickeLoadFailedSectionDimens.iconSize),
            tint = PickeTheme.colors.textMuted
        )
        Text(
            text = stringResource(R.string.toast_load_failed),
            color = PickeTheme.colors.textMuted,
            style = PickeTheme.typography.bodySmRegular
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PickeLoadFailedSectionPreview() {
    PickeTheme {
        PickeLoadFailedSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.s16)
        )
    }
}