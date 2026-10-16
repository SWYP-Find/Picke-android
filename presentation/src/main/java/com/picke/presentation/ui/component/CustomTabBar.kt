package com.picke.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object CustomTabBarDimens {
    val dividerThickness = 2.dp
}

@Composable
fun CustomTabBar(
    tabs: List<String>,
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isScrollable: Boolean = false
) {
    val tabHorizontalPadding = if (isScrollable) SpacingTokens.s16 else SpacingTokens.s4
    val tabContent: @Composable () -> Unit = {
        tabs.forEach { tab ->
            val textColor =
                if (selectedTab == tab) PickeTheme.colors.primary500 else PickeTheme.colors.textMuted
            val fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal

            Box(
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints.copy(minWidth = 0))
                        layout(placeable.width, placeable.height) { placeable.placeRelative(0, 0) }
                    }
                    .selectable(
                        selected = selectedTab == tab,
                        onClick = { onTabSelected(tab) }
                    )
                    .padding(horizontal = tabHorizontalPadding, vertical = SpacingTokens.s16),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab,
                    color = textColor,
                    fontWeight = fontWeight,
                    maxLines = 1,
                    style = PickeTheme.typography.bodySmMedium
                )
            }
        }
    }

    if (isScrollable) {
        ScrollableTabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            modifier = modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            edgePadding = SpacingTokens.s0,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]),
                    color = PickeTheme.colors.primary500
                )
            },
            divider = {
                HorizontalDivider(
                    thickness = CustomTabBarDimens.dividerThickness,
                    color = PickeTheme.colors.surfaceBeigeStrong
                )
            }
        ) {
            tabContent()
        }
    } else {
        TabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            modifier = modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]),
                    color = PickeTheme.colors.primary500
                )
            },
            divider = {
                HorizontalDivider(
                    thickness = CustomTabBarDimens.dividerThickness,
                    color = PickeTheme.colors.borderBeigeDefault
                )
            }
        ) {
            tabContent()
        }
    }
}

@Preview(name = "고정 탭", showBackground = true)
@Composable
private fun CustomTabBarPreview() {
    PickeTheme {
        CustomTabBar(
            tabs = listOf("클래스 요약", "내 리포트"),
            selectedTab = "클래스 요약",
            onTabSelected = {}
        )
    }
}

@Preview(name = "스크롤 탭", showBackground = true)
@Composable
private fun CustomTabBarScrollablePreview() {
    PickeTheme {
        CustomTabBar(
            tabs = listOf("전체", "철학", "사회", "문학", "과학", "예술", "역사"),
            selectedTab = "사회",
            isScrollable = true,
            onTabSelected = {}
        )
    }
}