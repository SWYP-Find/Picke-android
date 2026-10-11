package com.picke.presentation.ui.classroom.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun ClassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    minHeight: Dp,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    horizontalPadding: Dp = SpacingTokens.s8,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .background(PickeTheme.colors.inputTextfieldBackgroundDefault, shape)
            .border(
                ComponentNumberTokens.borderWidthRegular,
                PickeTheme.colors.inputTextfieldBorderDefault,
                shape
            )
            .padding(horizontal = horizontalPadding, vertical = SpacingTokens.s8),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = textStyle.copy(color = PickeTheme.colors.textDefault),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            cursorBrush = SolidColor(PickeTheme.colors.textDefault)
        ) { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = PickeTheme.colors.inputTextfieldTextDefault,
                    style = textStyle
                )
            }
            innerTextField()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassTextFieldPreview() {
    PickeTheme {
        ClassTextField(
            value = "",
            onValueChange = {},
            minHeight = ComponentNumberTokens.inputTextfieldHeight,
            textStyle = PickeTheme.typography.bodySmMedium,
            placeholder = "ex) 일상에 있을 법한 논쟁"
        )
    }
}