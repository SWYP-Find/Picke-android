package com.picke.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import com.picke.presentation.ui.component.PickeTabScaffold

@Composable
fun TabScreen(
    backStackEntry: NavBackStackEntry,
    onTabClick: (BottomNavItem) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val selectedTab = remember(backStackEntry.id) { backStackEntry.bottomBarTab() }

    if (selectedTab != null) {
        PickeTabScaffold(
            selectedTab = selectedTab,
            onTabClick = onTabClick
        ) { contentModifier ->
            content(contentModifier)
        }
    } else {
        content(Modifier)
    }
}