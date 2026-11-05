package com.picke.presentation.ui.vote.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun VoteTagChip(
    name: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(2.dp),
        color = Color.White.copy(alpha = 0.8f)
    ) {
        Text(
            text = "#$name",
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 2.dp
            ),
            color = PickeTheme.colors.primary500,
            style = PickeTheme.typography.captionLgMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VoteTagChipPreview() {
    PickeTheme {
        VoteTagChip(name = "철학")
    }
}