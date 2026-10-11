package com.picke.presentation.ui.classroom.classcreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.component.ClassCheckbox
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassMethodCard(
    badge: String,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onBadgeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    val borderColor = if (isSelected) PickeTheme.colors.borderPrimaryDefault else PickeTheme.colors.borderBeigeDefault

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(PickeTheme.colors.cardBaseBackgroundDefault)
            .border(ComponentNumberTokens.borderWidthMedium, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s20),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
        ) {
            ClassPillBadge(
                text = badge,
                iconResId = R.drawable.ic_info,
                textStyle = PickeTheme.typography.captionLgBold,
                onClick = onBadgeClick
            )

            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)) {
                Text(
                    text = title,
                    color = PickeTheme.colors.textDefault,
                    style = PickeTheme.typography.bodyLgBold
                )
                Text(
                    text = description,
                    color = PickeTheme.colors.textSubtler,
                    style = PickeTheme.typography.bodyXsRegular
                )
            }
        }

        ClassCheckbox(isChecked = isSelected)
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassMethodCardPreview() {
    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
        ) {
            ClassMethodCard(
                badge = "TTS 포함",
                title = "Pické 배틀로 클래스 만들기",
                description = "기존 배틀 콘텐츠를 골라 클래스를 만들어요",
                isSelected = true,
                onClick = {},
                onBadgeClick = {}
            )
            ClassMethodCard(
                badge = "AI 채팅",
                title = "커스텀 배틀로 클래스 만들기",
                description = "원하는 주제로 직접 배틀을 만들어보세요",
                isSelected = false,
                onClick = {},
                onBadgeClick = {}
            )
        }
    }
}