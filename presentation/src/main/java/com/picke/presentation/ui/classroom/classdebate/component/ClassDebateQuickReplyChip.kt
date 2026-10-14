package com.picke.presentation.ui.classroom.classdebate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassDebateQuickReplyChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.max)

    Text(
        text = text,
        modifier = modifier
            .background(PickeTheme.colors.cardBaseBackgroundDefault, shape)
            .border(ComponentNumberTokens.borderWidthRegular, PickeTheme.colors.borderBeigeDefault, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = SpacingTokens.s12, vertical = SpacingTokens.s6),
        color = PickeTheme.colors.textBody,
        style = PickeTheme.typography.captionLgSemiBold
    )
}

@Preview(showBackground = true)
@Composable
private fun ClassDebateQuickReplyChipPreview() {
    PickeTheme {
        ClassDebateQuickReplyChip(
            text = "장례식에선 예의가 필요해요",
            onClick = {}
        )
    }
}