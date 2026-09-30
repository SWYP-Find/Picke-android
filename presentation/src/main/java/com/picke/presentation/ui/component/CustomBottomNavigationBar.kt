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
import androidx.navigation.compose.rememberNavController
import com.picke.presentation.ui.main.BottomNavItem
import com.picke.presentation.ui.theme.PickeTheme

@SuppressLint("RestrictedApi")
@Composable
fun CustomBottomNavigationBar(
    mainNavController: NavController,
    rootNavController: NavController,
    onTabClick: (BottomNavItem) -> Unit = {},
    onHomeReselected: () -> Unit = {},
    onExploreReselected: () -> Unit = {}
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.TodayBattle,
        BottomNavItem.My
    )

    val bottomTabRoutes = items.map { it.route }

    NavigationBar(
        containerColor = PickeTheme.colors.surface,
    ) {
        val navBackStackEntry by mainNavController.currentBackStackEntryAsState()

        val activeTabRoute = remember(navBackStackEntry) {
            mainNavController.currentBackStack.value.lastOrNull { entry ->
                entry.destination.route in bottomTabRoutes
            }?.destination?.route
        }

        items.forEach { item ->
            val isSelected = activeTabRoute == item.route

            NavigationBarItem(
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
                        style = PickeTheme.typography.label
                    )
                },

                selected = isSelected,

                onClick = {
                    onTabClick(item)

                    if (item.route == BottomNavItem.TodayBattle.route) {
                        rootNavController.navigate(BottomNavItem.TodayBattle.route)
                    }else {
                        if (isSelected) {
                            when (item.route) {
                                BottomNavItem.Home.route -> onHomeReselected()
                                BottomNavItem.Explore.route -> onExploreReselected()
                            }

                            mainNavController.popBackStack(
                                route = item.route,
                                inclusive = false
                            )
                        } else {
                            mainNavController.navigate(item.route) {
                                popUpTo(mainNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PickeTheme.colors.textPrimary,
                    selectedTextColor = PickeTheme.colors.textPrimary,
                    unselectedIconColor = PickeTheme.colors.textPrimary.copy(alpha = 0.4f),
                    unselectedTextColor = PickeTheme.colors.textPrimary.copy(alpha = 0.4f),
                    indicatorColor = Color.Transparent
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
            mainNavController = rememberNavController(),
            rootNavController = rememberNavController()
        )
    }
}