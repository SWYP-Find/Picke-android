package com.picke.presentation.ui.my

import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.picke.presentation.AppRoute
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.ui.my.makebattle.MakeBattleScreen
import com.picke.presentation.ui.my.notice.NoticeEventScreen
import com.picke.presentation.ui.my.philosopher.PhilosopherTypeScreen
import com.picke.presentation.ui.my.point.PointScreen
import com.picke.presentation.ui.my.setting.alarm.SettingAlarmScreen
import com.picke.presentation.ui.my.setting.policy.PolicyWebViewScreen
import com.picke.presentation.ui.my.setting.profile.SettingProfileScreen
import com.picke.presentation.ui.my.setting.withdraw.WithdrawScreen

fun NavGraphBuilder.myGraph(
    navController: NavController,
    onNavigateToExplore: () -> Unit
) {
    composable(
        route = AppRoute.NoticeEvent.route,
        arguments = listOf(navArgument("noticeId") {
            type = NavType.LongType; defaultValue = -1L
        })
    ) { backStackEntry ->
        val noticeId = backStackEntry.arguments?.getLong("noticeId")?.takeIf { it != -1L }
        NoticeEventScreen(
            onBackClick = dropUnlessResumed { navController.popBackStack() },
            initialNoticeId = noticeId
        )
    }

    composable(AppRoute.Point.route) {
        PointScreen(
            onBackClick = dropUnlessResumed { navController.popBackStack() },
            onNavigateToMakeBattle = { navController.navigate(AppRoute.MakeBattle.route) }
        )
    }

    composable(AppRoute.MakeBattle.route) {
        MakeBattleScreen(
            onBackClick = dropUnlessResumed { navController.popBackStack() },
            onNavigateToExplore = onNavigateToExplore
        )
    }

    composable(AppRoute.SettingAlarm.route) {
        SettingAlarmScreen(onBackClick = dropUnlessResumed { navController.popBackStack() })
    }

    composable(AppRoute.SettingProfile.route) {
        SettingProfileScreen(onBackClick = dropUnlessResumed { navController.popBackStack() })
    }

    composable(AppRoute.PrivacyPolicy.route) {
        PolicyWebViewScreen(
            titleRes = R.string.policy_title_privacy,
            url = BuildConfig.PRIVACY_POLICY_URL,
            onBackClick = dropUnlessResumed { navController.popBackStack() }
        )
    }

    composable(AppRoute.TermsOfService.route) {
        PolicyWebViewScreen(
            titleRes = R.string.policy_title_terms,
            url = BuildConfig.TERMS_OF_SERVICE_URL,
            onBackClick = dropUnlessResumed { navController.popBackStack() }
        )
    }

    composable(
        route = AppRoute.OtherPhilosopher.route,
        arguments = listOf(navArgument("reportId") { type = NavType.StringType })
    ) { backStackEntry ->
        val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
        PhilosopherTypeScreen(
            reportId = reportId,
            onBackClick = dropUnlessResumed {
                val prevRoute = navController.previousBackStackEntry?.destination?.route
                if (prevRoute == null || prevRoute == AppRoute.Splash.route || prevRoute == AppRoute.Login.route) {
                    navController.navigate(AppRoute.Main.createRoute()) {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    navController.popBackStack()
                }
            },
            onGoToSplashClick = {
                navController.navigate(AppRoute.Main.createRoute()) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }

    composable(AppRoute.Withdraw.route) {
        WithdrawScreen(
            onBackClick = dropUnlessResumed { navController.popBackStack() },
            onNavigateToLogin = {
                navController.navigate(AppRoute.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}