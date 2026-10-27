package com.picke.presentation.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object CustomTopAppBarDimens {
    val height = 48.dp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String? = null,
    centerTitle: Boolean = true,
    onBackClick: (() -> Unit)? = null,
    showLogo: Boolean = false,
    backgroundColor: Color,
    backIconColor: Color = PickeTheme.colors.textDefault,
    titleColor: Color = PickeTheme.colors.textDefault,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = backgroundColor,
        navigationIconContentColor = backIconColor,
        titleContentColor = titleColor,
        actionIconContentColor = LocalContentColor.current
    )

    if (centerTitle) {
        CenterAlignedTopAppBar(
            title = {
                if (title != null) {
                    CustomTopAppBarTitle(title = title, color = titleColor)
                }
            },
            navigationIcon = {
                if (onBackClick != null) {
                    CustomTopAppBarBackButton(onClick = onBackClick, tint = backIconColor)
                }
            },
            actions = actions,
            expandedHeight = CustomTopAppBarDimens.height,
            windowInsets = WindowInsets(0.dp),
            colors = colors
        )
    } else {
        TopAppBar(
            title = {
                if (showLogo) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo_picke),
                        contentDescription = stringResource(R.string.top_app_bar_logo),
                        tint = PickeTheme.colors.gray900
                    )
                }
                if (title != null) {
                    CustomTopAppBarTitle(title = title, color = titleColor)
                }
            },
            navigationIcon = {
                if (onBackClick != null) {
                    CustomTopAppBarBackButton(onClick = onBackClick, tint = backIconColor)
                }
            },
            actions = actions,
            expandedHeight = CustomTopAppBarDimens.height,
            windowInsets = WindowInsets(0.dp),
            colors = colors
        )
    }
}

@Composable
fun CustomTopAppBarAction(
    @DrawableRes iconRes: Int,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = ComponentNumberTokens.iconLg,
    tint: Color = PickeTheme.colors.textDefault
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = tint
        )
    }
}

@Composable
fun CustomTopAppBarTextAction(
    @DrawableRes iconRes: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = SpacingTokens.s12, vertical = SpacingTokens.s12),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = PickeTheme.colors.iconPrimaryDefault
        )
        Text(
            text = text,
            color = PickeTheme.colors.textPrimary,
            style = PickeTheme.typography.captionMdBold
        )
    }
}

@Composable
private fun CustomTopAppBarTitle(
    title: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier,
        color = color,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = PickeTheme.typography.headingSm
    )
}

@Composable
private fun CustomTopAppBarBackButton(
    onClick: () -> Unit,
    tint: Color,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = stringResource(R.string.top_app_bar_back),
            modifier = Modifier.size(ComponentNumberTokens.iconLg),
            tint = tint
        )
    }
}

@Preview(name = "가운데 제목", showBackground = true)
@Composable
private fun CustomTopAppBarPreview() {
    PickeTheme {
        CustomTopAppBar(
            title = "클래스",
            onBackClick = {},
            backgroundColor = PickeTheme.colors.backgroundBeige,
            actions = {
                CustomTopAppBarAction(
                    iconRes = R.drawable.ic_more,
                    contentDescription = null,
                    onClick = {}
                )
            }
        )
    }
}

@Preview(name = "글자 버튼", showBackground = true)
@Composable
private fun CustomTopAppBarTextActionPreview() {
    PickeTheme {
        CustomTopAppBar(
            title = "클래스",
            onBackClick = {},
            backgroundColor = PickeTheme.colors.backgroundBeige,
            actions = {
                CustomTopAppBarTextAction(
                    iconRes = R.drawable.ic_report,
                    text = "리포트",
                    onClick = {}
                )
            }
        )
    }
}

@Preview(name = "로고", showBackground = true)
@Composable
private fun CustomTopAppBarLogoPreview() {
    PickeTheme {
        CustomTopAppBar(
            centerTitle = false,
            showLogo = true,
            backgroundColor = PickeTheme.colors.backgroundBeige
        )
    }
}