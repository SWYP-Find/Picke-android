package com.picke.presentation.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.component.PickeTabScaffold
import com.picke.presentation.ui.theme.PickeTheme

private val alwaysBottomBarRoutes = setOf(
    BottomNavItem.Home.route,
    BottomNavItem.Explore.route,
    BottomNavItem.My.route,
    AppRoute.Ticket.route,
    AppRoute.TicketRegister.route,
    AppRoute.TicketRegisterComplete.route,
    AppRoute.DiscussionHistory.route,
    AppRoute.ContentActivity.route,
    AppRoute.PhilosopherType.route,
    AppRoute.Setting.route,
    AppRoute.MyClass.route,
    AppRoute.ClassDetail.route,
    AppRoute.ClassMember.route,
    AppRoute.ClassResult.route,
    AppRoute.ClassMyOpinion.route
)

private val entryPathBottomBarRoutes = setOf(
    AppRoute.Point.route,
    AppRoute.MakeBattle.route,
    AppRoute.NoticeEvent.route
)

private val mainTabs = setOf(BottomNavItem.Home, BottomNavItem.Explore, BottomNavItem.My)

fun NavController.navigateToTab(item: BottomNavItem) {
    when (item) {
        BottomNavItem.Home, BottomNavItem.Explore, BottomNavItem.My -> {
            if (!popUpToMainTabScreen()) {
                navigate(BottomNavItem.Home.route) { popUpTo(0) }
            }
            val currentEntry = currentBackStackEntry ?: return
            if (selectedTab(currentEntry) != item) navigateToMainTab(item)
        }

        BottomNavItem.TodayBattle -> navigate(item.route) { launchSingleTop = true }

        BottomNavItem.Class -> {
            if (!popBackStack(item.route, inclusive = false)) {
                navigate(item.route) { launchSingleTop = true }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
fun NavController.popUpToMainTabScreen(): Boolean {
    val backStack = currentBackStack.value
    val targetIndex = backStack.indices.lastOrNull { index ->
        showsBottomBarAt(backStack, index) && selectedTabAt(backStack, index) in mainTabs
    } ?: return false
    val targetId = backStack[targetIndex].id
    while (currentBackStackEntry?.id != targetId) {
        if (!popBackStack()) return false
    }
    return true
}

@Composable
fun TabScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    onTabClick: (BottomNavItem) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val showsBottomBar =
        remember(backStackEntry.id) { navController.showsBottomBar(backStackEntry) }
    val selectedTab = remember(backStackEntry.id) { navController.selectedTab(backStackEntry) }

    if (showsBottomBar) {
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

@Composable
fun MainTabScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    onTabClick: (BottomNavItem) -> Unit,
    content: @Composable () -> Unit
) {
    TabScreen(
        navController = navController,
        backStackEntry = backStackEntry,
        onTabClick = onTabClick
    ) { contentModifier ->
        Box(
            modifier = contentModifier
                .fillMaxSize()
                .background(PickeTheme.colors.surfaceBeigeDefault)
        ) {
            content()
        }
    }
}

private fun NavController.navigateToMainTab(item: BottomNavItem) {
    navigate(item.route) {
        popUpTo(BottomNavItem.Home.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@SuppressLint("RestrictedApi")
private fun NavController.showsBottomBar(entry: NavBackStackEntry): Boolean {
    val backStack = currentBackStack.value
    val index = backStack.indexOfFirst { it.id == entry.id }
    return index >= 0 && showsBottomBarAt(backStack, index)
}

@SuppressLint("RestrictedApi")
private fun NavController.selectedTab(entry: NavBackStackEntry): BottomNavItem? {
    val backStack = currentBackStack.value
    val index = backStack.indexOfFirst { it.id == entry.id }
    return if (index >= 0) selectedTabAt(backStack, index) else null
}

private fun showsBottomBarAt(backStack: List<NavBackStackEntry>, index: Int): Boolean =
    when (backStack[index].destination.route) {
        in alwaysBottomBarRoutes -> true
        in entryPathBottomBarRoutes -> index > 0 && showsBottomBarAt(backStack, index - 1)
        else -> false
    }

private fun selectedTabAt(backStack: List<NavBackStackEntry>, index: Int): BottomNavItem? =
    (index downTo 0).firstNotNullOfOrNull { backStack[it].destination.route.toBottomNavItem() }

private fun String?.toBottomNavItem(): BottomNavItem? = when (this) {
    BottomNavItem.Home.route -> BottomNavItem.Home
    BottomNavItem.Explore.route -> BottomNavItem.Explore
    BottomNavItem.TodayBattle.route, AppRoute.TodayBattle.route -> BottomNavItem.TodayBattle
    BottomNavItem.Class.route -> BottomNavItem.Class
    BottomNavItem.My.route -> BottomNavItem.My
    else -> null
}