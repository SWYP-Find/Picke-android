package com.picke.presentation.ui.classroom.classdetail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassDetailInfoRow(
    title: String,
    description: String,
    actionText: String?,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SpacingTokens.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)
        ) {
            Text(
                text = title,
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.bodySmSemiBold
            )
            Text(
                text = description,
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.captionLgRegular
            )
        }

        if (actionText != null) {
            Text(
                text = actionText,
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    role = Role.Button,
                    onClick = onActionClick
                ),
                color = PickeTheme.colors.textPrimary,
                style = PickeTheme.typography.captionLgBold
            )
        }
    }
}

@Preview(name = "버튼 있음", showBackground = true)
@Composable
private fun ClassDetailInfoRowPreview() {
    PickeTheme {
        ClassDetailInfoRow(
            title = "클래스 멤버",
            description = "총 32명이 참여하고 있어요",
            actionText = "보기",
            onActionClick = {}
        )
    }
}

@Preview(name = "버튼 없음", showBackground = true)
@Composable
private fun ClassDetailInfoRowNoActionPreview() {
    PickeTheme {
        ClassDetailInfoRow(
            title = "참여 마감일",
            description = "2026. 09. 23. 수요일 18:00까지",
            actionText = null,
            onActionClick = {}
        )
    }
}