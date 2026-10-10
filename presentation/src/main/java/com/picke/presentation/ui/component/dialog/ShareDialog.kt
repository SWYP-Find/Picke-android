package com.picke.presentation.ui.component.dialog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ShareDialogDimens {
    val cornerRadius = 4.dp
    val itemIconSize = 42.dp
}

@Composable
fun ShareDialog(
    onKakaoClick: () -> Unit,
    onInstaClick: () -> Unit,
    onCopyLinkClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(ShareDialogDimens.cornerRadius)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(PickeTheme.colors.borderBeigeDefault, shape)
                .border(ComponentNumberTokens.borderWidthRegular, PickeTheme.colors.beige900, shape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpacingTokens.s20, vertical = SpacingTokens.s16),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.share_dialog_title),
                    modifier = Modifier.align(Alignment.CenterStart),
                    color = PickeTheme.colors.textDefault,
                    style = PickeTheme.typography.headingSm
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_x),
                    contentDescription = stringResource(R.string.share_dialog_close),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(ComponentNumberTokens.iconXs)
                        .clickable(onClick = onDismiss),
                    tint = PickeTheme.colors.textDefault
                )
            }

            HorizontalDivider(thickness = ComponentNumberTokens.borderWidthRegular, color = PickeTheme.colors.textDefault)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpacingTokens.s20, vertical = SpacingTokens.s24),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShareDialogItem(
                    iconRes = R.drawable.logo_share_kakao,
                    title = stringResource(R.string.share_dialog_kakao),
                    onClick = onKakaoClick
                )
                ShareDialogItem(
                    iconRes = R.drawable.logo_share_instagram,
                    title = stringResource(R.string.share_dialog_instagram),
                    onClick = onInstaClick
                )
                ShareDialogItem(
                    iconRes = R.drawable.ic_link,
                    title = stringResource(R.string.share_dialog_copy_link),
                    onClick = onCopyLinkClick
                )
            }
        }
    }
}

@Composable
private fun ShareDialogItem(
    @DrawableRes iconRes: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier
                .size(ShareDialogDimens.itemIconSize)
                .clip(CircleShape)
                .clickable(onClick = onClick)
        )
        Spacer(modifier = Modifier.height(SpacingTokens.s8))
        Text(
            text = title,
            color = PickeTheme.colors.primary500,
            style = PickeTheme.typography.bodyXxsMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShareDialogPreview() {
    PickeTheme {
        ShareDialog(
            onKakaoClick = {},
            onInstaClick = {},
            onCopyLinkClick = {},
            onDismiss = {}
        )
    }
}