package com.picke.presentation.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.picke.presentation.ui.main.BottomNavItem
import com.picke.presentation.ui.theme.PickeTheme

private val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Explore,
    BottomNavItem.TodayBattle,
    BottomNavItem.Class,
    BottomNavItem.My
)

@SuppressLint("RestrictedApi")
@Composable
fun CustomBottomNavigationBar(
    mainNavController: NavController,
    onRootTabClick: (BottomNavItem) -> Unit,
    onTabClick: (BottomNavItem) -> Unit = {},
    onHomeReselected: () -> Unit = {},
    onExploreReselected: () -> Unit = {}
) {
    val bottomTabRoutes = bottomNavItems.map { it.route }
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()

    val activeTabRoute = remember(navBackStackEntry) {
        mainNavController.currentBackStack.value.lastOrNull { entry ->
            entry.destination.route in bottomTabRoutes
        }?.destination?.route
    }

    CustomBottomNavigationBar(
        selectedRoute = activeTabRoute,
        onItemClick = { item ->
            onTabClick(item)

            when {
                item == BottomNavItem.TodayBattle || item == BottomNavItem.Class -> onRootTabClick(item)

                activeTabRoute == item.route -> {
                    if (navBackStackEntry?.destination?.route == item.route) {
                        when (item.route) {
                            BottomNavItem.Home.route -> onHomeReselected()
                            BottomNavItem.Explore.route -> onExploreReselected()
                        }
                    }
                }

                else -> {
                    mainNavController.navigate(item.route) {
                        popUpTo(mainNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    )
}

@Composable
fun CustomBottomNavigationBar(
    selectedRoute: String?,
    onItemClick: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = PickeTheme.colors.surfaceBeigeDefault
    ) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = selectedRoute == item.route,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = PickeTheme.typography.captionLgMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PickeTheme.colors.textDefault,
                    selectedTextColor = PickeTheme.colors.textDefault,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = PickeTheme.colors.textDefault.copy(alpha = 0.4f),
                    unselectedTextColor = PickeTheme.colors.textDefault.copy(alpha = 0.4f)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomBottomNavigationBarPreview() {
    PickeTheme {
        CustomBottomNavigationBar(
            selectedRoute = BottomNavItem.Class.route,
            onItemClick = {}
        )
    }
}