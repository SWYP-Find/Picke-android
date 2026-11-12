package com.picke.presentation.ui.my.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun CreditCard(
    credit: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(RadiusTokens.default))
            .background(PickeTheme.colors.primary800)
            .clickable { onClick() }
            .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s20),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(color = PickeTheme.colors.secondary300, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.my_point_symbol),
                    color = PickeTheme.colors.colorTextBasic,
                    style = PickeTheme.typography.captionMdBold
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
                Text(
                    text = stringResource(R.string.my_point),
                    color = PickeTheme.colors.textInverse,
                    style = PickeTheme.typography.bodyXxsSemiBold
                )
                Text(
                    text = credit.toString(),
                    color = PickeTheme.colors.textSecondary,
                    style = PickeTheme.typography.bodyXxsSemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditCardPreview() {
    PickeTheme {
        CreditCard(
            credit = 1,
            onClick = { }
        )
    }
}