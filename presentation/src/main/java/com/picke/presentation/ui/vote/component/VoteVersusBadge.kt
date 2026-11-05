package com.picke.presentation.ui.vote.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun VoteVersusBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(36.dp),
        shape = CircleShape,
        color = Color(0xFFF2E3C6)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "VS",
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.bodySmMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VoteVersusBadgePreview() {
    PickeTheme {
        VoteVersusBadge()
    }
}