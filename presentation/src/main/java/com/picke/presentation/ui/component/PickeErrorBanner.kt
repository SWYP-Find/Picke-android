package com.picke.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PickeErrorBannerDimens {
    val cornerRadius = 8.dp
    val shadowElevation = 4.dp
}

@Composable
fun PickeErrorBanner(
    message: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(PickeErrorBannerDimens.cornerRadius)

    Row(
        modifier = modifier
            .shadow(elevation = PickeErrorBannerDimens.shadowElevation, shape = shape)
            // TODO: 토큰 동기화 - color/danger/500
            .background(PickeTheme.colors.errorDefault, shape)
            .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s12),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_warning),
            contentDescription = null
        )

        Text(
            text = message,
            color = PickeTheme.colors.beige50,
            style = PickeTheme.typography.bodySmSemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PickeErrorBannerPreview() {
    PickeTheme {
        PickeErrorBanner(
            message = "오디오를 불러오는 중 문제가 발생했어요",
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.s16)
        )
    }
}