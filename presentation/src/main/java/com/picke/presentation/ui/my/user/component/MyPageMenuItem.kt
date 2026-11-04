package com.picke.presentation.ui.my.user.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun MyPageMenuItem(
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s20),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: 토큰 동기화 - color/text/basic
            Text(
                text = title,
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.bodySmSemiBold
            )
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right_a),
                contentDescription = null,
                modifier = Modifier.size(ComponentNumberTokens.iconSm),
                tint = PickeTheme.colors.textDefault
            )
        }
        // TODO: 토큰 동기화 - color/border/beige-dark
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = SpacingTokens.s16),
            thickness = ComponentNumberTokens.borderWidthRegular,
            color = PickeTheme.colors.beige700
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyPageMenuItemPreview() {
    PickeTheme {
        MyPageMenuItem(
            title = "test",
            onClick = {}
        )
    }
}