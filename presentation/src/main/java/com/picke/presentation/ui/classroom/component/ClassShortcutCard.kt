package com.picke.presentation.ui.classroom.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassShortcutCardDimens {
    val height = 118.dp
}

@Composable
fun ClassShortcutCard(
    title: String,
    description: String,
    @DrawableRes imageResId: Int,
    imageSize: Dp,
    imageEndOverflow: Dp,
    imageBottomOverflow: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Box(
        modifier = modifier
            .height(ClassShortcutCardDimens.height)
            .clip(shape)
            .background(PickeTheme.colors.cardGrayBackgroundDefault)
            .border(ComponentNumberTokens.borderWidthRegular, PickeTheme.colors.gray800, shape)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .wrapContentSize(align = Alignment.BottomEnd, unbounded = true)
                .offset(x = imageEndOverflow, y = imageBottomOverflow)
                .requiredSize(imageSize),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)
        ) {
            Text(
                text = title,
                color = PickeTheme.colors.textInverse,
                style = PickeTheme.typography.bodyLgSemiBold
            )
            Text(
                text = description,
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.bodyXsRegular
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassShortcutCardPreview() {
    PickeTheme {
        ClassShortcutCard(
            title = "내 클래스",
            description = "참여중인 클래스",
            imageResId = R.drawable.img_class_my_desk,
            imageSize = 130.dp,
            imageEndOverflow = 6.dp,
            imageBottomOverflow = 49.dp,
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}