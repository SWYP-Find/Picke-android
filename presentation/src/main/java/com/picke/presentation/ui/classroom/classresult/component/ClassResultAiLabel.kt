package com.picke.presentation.ui.classroom.classresult.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassResultAiLabelDimens {
    val iconBoxRadius = 6.dp
}

@Composable
fun ClassResultAiLabel(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = PickeTheme.typography.captionLgSemiBold
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_ai_spark),
            contentDescription = null,
            modifier = Modifier
                .size(ComponentNumberTokens.iconMd)
                .background(
                    PickeTheme.colors.surfacePrimaryDefault,
                    RoundedCornerShape(ClassResultAiLabelDimens.iconBoxRadius)
                ),
            tint = PickeTheme.colors.textInverse
        )
        Text(
            text = text,
            color = PickeTheme.colors.textPrimary,
            style = style
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassResultAiLabelPreview() {
    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
        ) {
            ClassResultAiLabel(text = "내 판단의 기준")
            ClassResultAiLabel(text = "AI 인사이트", style = PickeTheme.typography.captionMdBold)
        }
    }
}