package com.picke.presentation.ui.perspective.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PerspectiveRatioBarDimens {
    val areaHeight = 35.dp
    val barHeight = 6.dp
}

private const val MAX_RATIO = 100f

@Composable
fun PerspectiveRatioBar(
    ratio: Float,
    modifier: Modifier = Modifier
) {
    val barShape = RoundedCornerShape(RadiusTokens.default)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(PerspectiveRatioBarDimens.areaHeight)
            .padding(horizontal = SpacingTokens.s8),
        contentAlignment = Alignment.Center
    ) {
        // TODO: 토큰 동기화 - 비율 바 배경 (Figma 변수 없이 #F0EEEB)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(PerspectiveRatioBarDimens.barHeight)
                .clip(barShape)
                .background(Color(0xFFF0EEEB))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(ratio / MAX_RATIO)
                    .fillMaxHeight()
                    .clip(barShape)
                    .background(PickeTheme.colors.primary500)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PerspectiveRatioBarPreview() {
    PickeTheme {
        PerspectiveRatioBar(ratio = 59.5f)
    }
}