package com.picke.presentation.ui.classroom.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassFormField(
    title: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    contentSpacing: Dp = SpacingTokens.s12,
    content: @Composable () -> Unit
) {
    val requiredMark = stringResource(R.string.class_form_required_mark)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(contentSpacing)
    ) {
        Text(
            text = buildAnnotatedString {
                append(title)
                if (isRequired) {
                    withStyle(SpanStyle(color = PickeTheme.colors.textPrimary)) { append(requiredMark) }
                }
            },
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.bodyMdSemiBold
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassFormFieldPreview() {
    PickeTheme {
        ClassFormField(
            title = "카테고리",
            modifier = Modifier.padding(SpacingTokens.s16),
            isRequired = true
        ) {
            Text(text = "철학")
        }
    }
}