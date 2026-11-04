package com.picke.presentation.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

enum class ProfileImageKind(val imageRatio: Float) {
    CHARACTER(2f / 3f),
    PHILOSOPHER(28f / 40f),
    LOCKED(30f / 40f)
}

@Composable
fun ProfileImage(
    imageUrl: String?,
    size: Dp,
    modifier: Modifier = Modifier,
    kind: ProfileImageKind = ProfileImageKind.CHARACTER,
    @DrawableRes defaultImageRes: Int? = null,
    backgroundColor: Color = PickeTheme.colors.borderBeigeDefault
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl ?: defaultImageRes,
            contentDescription = null,
            modifier = Modifier.size(size * kind.imageRatio),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileImagePreview() {
    PickeTheme {
        Row(
            modifier = Modifier.padding(SpacingTokens.s16),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
        ) {
            ProfileImage(
                imageUrl = null,
                size = ComponentNumberTokens.avatarSm,
                defaultImageRes = R.drawable.illust_owl
            )
            ProfileImage(
                imageUrl = null,
                size = ComponentNumberTokens.avatarLg,
                defaultImageRes = R.drawable.illust_owl
            )
            ProfileImage(
                imageUrl = null,
                size = ComponentNumberTokens.avatarMd,
                kind = ProfileImageKind.PHILOSOPHER,
                defaultImageRes = R.drawable.illust_mengzi
            )
        }
    }
}