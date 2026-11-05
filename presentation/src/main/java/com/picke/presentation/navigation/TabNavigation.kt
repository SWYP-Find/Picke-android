package com.picke.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.picke.presentation.ui.component.PickeTabScaffold

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

fun NavController.navigateToTab(item: BottomNavItem) {
    if (!popUpToTabScreen()) {
        navigate(BottomNavItem.Home.route) { popUpTo(0) }
    }
    val currentTabEntry = currentTabEntry() ?: return
    if (BottomNavItem.fromRoute(currentTabEntry.destination.route) == item) {
        popUpToEntry(currentTabEntry)
    } else {
        navigate(item.route) {
            popUpTo(BottomNavItem.Home.route) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@SuppressLint("RestrictedApi")
fun NavController.popUpToTabScreen(): Boolean {
    val backStack = currentBackStack.value
    val targetIndex = backStack.indices.lastOrNull { index ->
        showsBottomBarAt(backStack, index) || BottomNavItem.fromRoute(backStack[index].destination.route) != null
    } ?: return false
    return popUpToEntry(backStack[targetIndex])
}

@Composable
fun TabScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    onTabClick: (BottomNavItem) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val selectedTab = remember(backStackEntry.id) { navController.bottomBarTab(backStackEntry) }

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

@SuppressLint("RestrictedApi")
private fun NavController.currentTabEntry(): NavBackStackEntry? =
    currentBackStack.value.lastOrNull { BottomNavItem.fromRoute(it.destination.route) != null }

@SuppressLint("RestrictedApi")
private fun NavController.bottomBarTab(entry: NavBackStackEntry): BottomNavItem? {
    val backStack = currentBackStack.value
    val index = backStack.indexOfFirst { it.id == entry.id }
    if (index < 0 || !showsBottomBarAt(backStack, index)) return null
    return backStack.subList(0, index + 1).asReversed()
        .firstNotNullOfOrNull { BottomNavItem.fromRoute(it.destination.route) }
}

private fun NavController.popUpToEntry(entry: NavBackStackEntry): Boolean {
    while (currentBackStackEntry?.id != entry.id) {
        if (!popBackStack()) return false
    }
    return true
}

private fun showsBottomBarAt(backStack: List<NavBackStackEntry>, index: Int): Boolean =
    when (backStack[index].destination.route) {
        in alwaysBottomBarRoutes -> true
        in entryPathBottomBarRoutes -> index > 0 && showsBottomBarAt(backStack, index - 1)
        else -> false
    }