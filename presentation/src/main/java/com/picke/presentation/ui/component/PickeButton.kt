package com.picke.presentation.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme

private object PickeButtonDimens {
    val minHeight = 48.dp
}

@Composable
fun PickeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = PickeTheme.colors.buttonPrimaryBackgroundDefault,
    disabledBackgroundColor: Color = PickeTheme.colors.buttonPrimaryBackgroundDisabled,
    textColor: Color = PickeTheme.colors.buttonPrimaryTextDefault,
    @DrawableRes iconResId: Int? = null,
    minHeight: Dp = PickeButtonDimens.minHeight
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight),
        enabled = enabled,
        shape = RoundedCornerShape(2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = disabledBackgroundColor
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconResId != null) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(2.dp))
            }
            Text(
                text = text,
                color = textColor,
                style = PickeTheme.typography.bodySmSemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PickeButtonPreview() {
    PickeTheme {
        PickeButton(
            text = "새 클래스 만들기",
            onClick = {}
        )
    }
}