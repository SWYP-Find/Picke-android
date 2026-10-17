package com.picke.presentation.ui.classroom.classcreate.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
internal fun ClassSettingErrorText(
    text: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(SpacingTokens.s0)
    ) {
        if (isVisible) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(top = SpacingTokens.s8)
                    .wrapContentHeight(align = Alignment.Top, unbounded = true),
                color = PickeTheme.colors.textError,
                style = PickeTheme.typography.captionLgMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassSettingErrorTextPreview() {
    PickeTheme {
        ClassSettingErrorText(
            text = "공백만으로는 클래스를 만들 수 없어요",
            isVisible = true,
            modifier = Modifier.padding(bottom = SpacingTokens.s32)
        )
    }
}