package com.picke.presentation.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun HomeSectionEmpty(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(width = 220.dp, height = 274.dp)
            .border(1.dp, PickeTheme.colors.borderBeigeDefault, RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surfaceBeigeSubtle),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = PickeTheme.typography.bodySmRegular,
            color = PickeTheme.colors.textMuted,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeSectionEmptyPreview() {
    PickeTheme {
        HomeSectionEmpty(message = "지금 뜨는 배틀이 아직 없어요")
    }
}
