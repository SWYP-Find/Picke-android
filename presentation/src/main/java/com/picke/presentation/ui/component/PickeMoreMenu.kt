package com.picke.presentation.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object PickeMoreMenuDimens {
    val menuCornerRadius = 8.dp
    val itemVerticalPadding = 10.dp
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
            modifier = Modifier
                .background(PickeTheme.colors.primary600)
                .clip(RoundedCornerShape(PickeMoreMenuDimens.menuCornerRadius))
        ) {
            items.forEach { item ->
                PickeMoreMenuRow(
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

@Composable
private fun PickeMoreMenuRow(
    item: PickeMoreMenuItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = SpacingTokens.s16, vertical = PickeMoreMenuDimens.itemVerticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(item.iconRes),
            contentDescription = null,
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = PickeTheme.colors.textInverse
        )
        Spacer(modifier = Modifier.width(SpacingTokens.s8))
        Text(
            text = stringResource(item.textRes),
            color = PickeTheme.colors.textInverse,
            style = PickeTheme.typography.bodySmMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PickeMoreMenuPreview() {
    PickeTheme {
        PickeMoreMenu(
            items = listOf(
                PickeMoreMenuItem(iconRes = R.drawable.ic_trash, textRes = R.string.comment_menu_delete, onClick = {}),
                PickeMoreMenuItem(iconRes = R.drawable.ic_edit, textRes = R.string.comment_menu_edit, onClick = {})
            )
        )
    }
}