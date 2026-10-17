package com.picke.presentation.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object CustomTextFieldDimens {
    val errorBorderWidth = 2.dp
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    minHeight: Dp,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    @DrawableRes leadingIconRes: Int? = null,
    horizontalPadding: Dp = SpacingTokens.s8,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    val width =
        if (isError) CustomTextFieldDimens.errorBorderWidth else ComponentNumberTokens.borderWidthRegular
    val color =
        if (isError) PickeTheme.colors.borderErrorDefault else PickeTheme.colors.inputTextfieldBorderDefault

    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .background(PickeTheme.colors.inputTextfieldBackgroundDefault, shape)
            .border(width, color, shape)
            .padding(horizontal = horizontalPadding, vertical = SpacingTokens.s8),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIconRes != null) {
                Icon(
                    painter = painterResource(id = leadingIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(ComponentNumberTokens.iconMd),
                    tint = PickeTheme.colors.textDefault
                )
            }
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
}

@Preview(name = "기본", showBackground = true)
@Composable
private fun CustomTextFieldPreview() {
    PickeTheme {
        CustomTextField(
            value = "",
            onValueChange = {},
            minHeight = ComponentNumberTokens.inputTextfieldHeight,
            textStyle = PickeTheme.typography.bodySmMedium,
            placeholder = "ex) 일상에 있을 법한 논쟁"
        )
    }
}

@Preview(name = "오류", showBackground = true)
@Composable
private fun CustomTextFieldErrorPreview() {
    PickeTheme {
        CustomTextField(
            value = "권동현",
            onValueChange = {},
            minHeight = ComponentNumberTokens.inputTextfieldHeight,
            textStyle = PickeTheme.typography.bodySmMedium,
            isError = true
        )
    }
}