package com.picke.presentation.ui.classroom.classmyopinion.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassMyOpinionSectionTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = PickeTheme.colors.textDefault,
        style = PickeTheme.typography.bodyXsSemiBold
    )
}

@Preview(showBackground = true)
@Composable
private fun ClassMyOpinionSectionTitlePreview() {
    PickeTheme {
        ClassMyOpinionSectionTitle(
            text = "남긴 댓글",
            modifier = Modifier.padding(SpacingTokens.s16)
        )
    }
}