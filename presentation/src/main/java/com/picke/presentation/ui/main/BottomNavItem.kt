package com.picke.presentation.ui.main

import com.picke.presentation.R
import com.picke.presentation.analytics.UiActionName

sealed class BottomNavItem(val route: String, val title: String, val icon: Int) {
    object Home : BottomNavItem("tab_home", "홈", R.drawable.ic_nav_home)
    object Explore : BottomNavItem("tab_explore", "탐색", R.drawable.ic_nav_explore)
    object TodayBattle : BottomNavItem("tab_battle", "빠른배틀", R.drawable.ic_nav_battle)
    object Class : BottomNavItem("tab_class", "클래스", R.drawable.ic_nav_classroom)
    object My : BottomNavItem("tab_my", "마이", R.drawable.ic_nav_my)
}

fun BottomNavItem.toTabAction(): String = when (this) {
    BottomNavItem.Home -> UiActionName.TAB_HOME
    BottomNavItem.Explore -> UiActionName.TAB_EXPLORE
    BottomNavItem.TodayBattle -> UiActionName.TAB_QUICK_BATTLE
    BottomNavItem.Class -> UiActionName.TAB_CLASS
    BottomNavItem.My -> UiActionName.TAB_MYPAGE
}