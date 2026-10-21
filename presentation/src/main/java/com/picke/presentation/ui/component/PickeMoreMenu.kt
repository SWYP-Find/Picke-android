package com.picke.presentation.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PickeMoreMenuDimens {
    val itemMinWidth = 70.dp
    val itemMinHeight = 34.dp
    val itemCornerRadius = 24.dp
}

data class PickeMoreMenuItem(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val textRes: Int,
    val onClick: () -> Unit
)

@Composable
fun PickeMoreMenu(
    items: List<PickeMoreMenuItem>,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { isExpanded = true },
            modifier = Modifier.size(ComponentNumberTokens.iconLg)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_more),
                contentDescription = stringResource(R.string.more),
                tint = PickeTheme.colors.textMuted
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
                items.forEach { item ->
                    PickeMoreMenuButton(
                        item = item,
                        onClick = {
                            isExpanded = false
                            item.onClick()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PickeMoreMenuButton(
    item: PickeMoreMenuItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(PickeMoreMenuDimens.itemCornerRadius)

    Row(
        modifier = modifier
            .widthIn(min = PickeMoreMenuDimens.itemMinWidth)
            .heightIn(min = PickeMoreMenuDimens.itemMinHeight)
            .clip(shape)
            .background(PickeTheme.colors.buttonIconBackgroundDefault)
            .clickable { onClick() }
            .padding(horizontal = SpacingTokens.s6),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(item.iconRes),
            contentDescription = null,
            tint = PickeTheme.colors.iconGrayInverse
        )
        Text(
            text = stringResource(item.textRes),
            color = PickeTheme.colors.iconGrayInverse,
            style = PickeTheme.typography.bodyXsMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PickeMoreMenuButtonPreview() {
    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)
        ) {
            PickeMoreMenuButton(
                item = PickeMoreMenuItem(iconRes = R.drawable.ic_trash, textRes = R.string.comment_menu_delete, onClick = {}),
                onClick = {}
            )
            PickeMoreMenuButton(
                item = PickeMoreMenuItem(iconRes = R.drawable.ic_edit, textRes = R.string.comment_menu_edit, onClick = {}),
                onClick = {}
            )
            PickeMoreMenuButton(
                item = PickeMoreMenuItem(iconRes = R.drawable.ic_bell, textRes = R.string.comment_menu_report, onClick = {}),
                onClick = {}
            )
        }
    }
}