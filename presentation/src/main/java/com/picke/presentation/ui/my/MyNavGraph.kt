package com.picke.presentation.ui.my

import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.navigation.AppRoute
import com.picke.presentation.navigation.BottomNavItem
import com.picke.presentation.navigation.TabScreen
import com.picke.presentation.ui.my.content.ContentActivityScreen
import com.picke.presentation.ui.my.discussion.DiscussionHistoryScreen
import com.picke.presentation.ui.my.makebattle.MakeBattleScreen
import com.picke.presentation.ui.my.notice.NoticeEventScreen
import com.picke.presentation.ui.my.philosopher.PhilosopherTypeScreen
import com.picke.presentation.ui.my.point.PointScreen
import com.picke.presentation.ui.my.setting.SettingScreen
import com.picke.presentation.ui.my.setting.alarm.SettingAlarmScreen
import com.picke.presentation.ui.my.setting.policy.PolicyWebViewScreen
import com.picke.presentation.ui.my.setting.profile.SettingProfileScreen
import com.picke.presentation.ui.my.setting.withdraw.WithdrawScreen
import com.picke.presentation.ui.my.ticket.TicketScreen
import com.picke.presentation.ui.my.ticket.ticketregister.TicketRegisterScreen
import com.picke.presentation.ui.my.ticket.ticketregistercomplete.TicketRegisterCompleteScreen
import com.picke.presentation.ui.my.user.MyScreen

fun NavGraphBuilder.myGraph(
    navController: NavController,
    onTabClick: (BottomNavItem) -> Unit,
    onNavigateToExplore: () -> Unit
) {
    val showsBottomBarArgument = navArgument(AppRoute.ARG_SHOWS_BOTTOM_BAR) {
        type = NavType.BoolType
        defaultValue = false
    }

    composable(BottomNavItem.My.route) { backStackEntry ->
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            MyScreen(
                onNavigateToAlarm = {
                    navController.navigate(AppRoute.Alarm.route)
                },
                onNavigateToSetting = {
                    navController.navigate(AppRoute.Setting.route)
                },
                onNavigateToDiscussion = {
                    navController.navigate(AppRoute.DiscussionHistory.route)
                },
                onNavigateToPhilosopher = {
                    navController.navigate(AppRoute.PhilosopherType.route)
                },
                onNavigateToContent = {
                    navController.navigate(AppRoute.ContentActivity.route)
                },
                onNavigateToNotice = {
                    navController.navigate(AppRoute.NoticeEvent.createRoute(showsBottomBar = true))
                },
                onNavigateToPoint = {
                    navController.navigate(AppRoute.Point.createRoute(showsBottomBar = true))
                },
                onNavigateToTicket = {
                    navController.navigate(AppRoute.Ticket.route)
                },
                modifier = contentModifier
            )
        }
    }

    composable(AppRoute.Ticket.route) { backStackEntry ->
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            TicketScreen(
                onBackClick = dropUnlessResumed { navController.popBackStack() },
                onNavigateToRegister = { navController.navigate(AppRoute.TicketRegister.route) },
                onReplaceWithRegister = {
                    navController.navigate(AppRoute.TicketRegister.route) {
                        popUpTo(AppRoute.Ticket.route) { inclusive = true }
                    }
                },
                onNavigateBackAfterLoadFailed = {
                    navController.popBackStack(AppRoute.Ticket.route, inclusive = true)
                },
                modifier = contentModifier
            )
        }
    }

    composable(AppRoute.TicketRegister.route) { backStackEntry ->
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            TicketRegisterScreen(
                onBackClick = dropUnlessResumed { navController.popBackStack() },
                onNavigateToRegisterComplete = { ticketId ->
                    navController.navigate(AppRoute.TicketRegisterComplete.createRoute(ticketId)) {
                        popUpTo(AppRoute.TicketRegister.route) { inclusive = true }
                    }
                },
                onNavigateToTicket = {
                    navController.navigate(AppRoute.Ticket.route) {
                        popUpTo(BottomNavItem.My.route)
                    }
                },
                modifier = contentModifier
            )
        }
    }

    composable(
        route = AppRoute.TicketRegisterComplete.route,
        arguments = listOf(navArgument(AppRoute.ARG_TICKET_ID) { type = NavType.LongType })
    ) { backStackEntry ->
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            TicketRegisterCompleteScreen(
                onBackClick = dropUnlessResumed { navController.popBackStack() },
                onNavigateToTicket = dropUnlessResumed {
                    navController.navigate(AppRoute.Ticket.route) {
                        popUpTo(BottomNavItem.My.route)
                    }
                },
                modifier = contentModifier
            )
        }
    }

    composable(
        route = AppRoute.Point.route,
        arguments = listOf(showsBottomBarArgument)
    ) { backStackEntry ->
        val showsBottomBar =
            backStackEntry.arguments?.getBoolean(AppRoute.ARG_SHOWS_BOTTOM_BAR) ?: false
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            PointScreen(
                onBackClick = dropUnlessResumed { navController.popBackStack() },
                onNavigateToMakeBattle = {
                    navController.navigate(AppRoute.MakeBattle.createRoute(showsBottomBar))
                },
                modifier = contentModifier
            )
        }
    }

    composable(
        route = AppRoute.MakeBattle.route,
        arguments = listOf(showsBottomBarArgument)
    ) { backStackEntry ->
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            MakeBattleScreen(
                onBackClick = dropUnlessResumed { navController.popBackStack() },
                onNavigateToExplore = onNavigateToExplore,
                modifier = contentModifier
            )
        }
    }

    composable(AppRoute.DiscussionHistory.route) { backStackEntry ->
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            DiscussionHistoryScreen(
                onBackClick = dropUnlessResumed { navController.popBackStack() },
                onNavigateToDetail = { battleId ->
                    navController.navigate(AppRoute.Perspective.createRoute(battleId))
                },
                modifier = contentModifier
            )
        }
    }

    composable(AppRoute.ContentActivity.route) { backStackEntry ->
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            ContentActivityScreen(
                onBackClick = dropUnlessResumed { navController.popBackStack() },
                onNavigateToComment = { itemId ->
                    navController.navigate(AppRoute.Comment.createRoute(itemId))
                },
                modifier = contentModifier
            )
        }
    }

    composable(AppRoute.PhilosopherType.route) { backStackEntry ->
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            PhilosopherTypeScreen(
                onBackClick = dropUnlessResumed { navController.popBackStack() },
                modifier = contentModifier
            )
        }
    }

    composable(
        route = AppRoute.NoticeEvent.route,
        arguments = listOf(
            navArgument("noticeId") { type = NavType.LongType; defaultValue = -1L },
            showsBottomBarArgument
        )
    ) { backStackEntry ->
        val noticeId = backStackEntry.arguments?.getLong("noticeId")?.takeIf { it != -1L }
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            NoticeEventScreen(
                onBackClick = dropUnlessResumed { navController.popBackStack() },
                initialNoticeId = noticeId,
                modifier = contentModifier
            )
        }
    }

    composable(AppRoute.Setting.route) { backStackEntry ->
        TabScreen(
            backStackEntry = backStackEntry,
            onTabClick = onTabClick
        ) { contentModifier ->
            SettingScreen(
                onBackClick = dropUnlessResumed { navController.popBackStack() },
                onNavigateToSettingProfile = { navController.navigate(AppRoute.SettingProfile.route) },
                onNavigateToSettingAlarm = { navController.navigate(AppRoute.SettingAlarm.route) },
                onNavigateToPrivacyPolicy = { navController.navigate(AppRoute.PrivacyPolicy.route) },
                onNavigateToTermsOfService = { navController.navigate(AppRoute.TermsOfService.route) },
                onNavigateToWithdraw = { navController.navigate(AppRoute.Withdraw.route) },
                onNavigateToLogin = {
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = contentModifier
            )
        }
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
                    navController.navigate(BottomNavItem.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    navController.popBackStack()
                }
            },
            onGoToSplashClick = {
                navController.navigate(BottomNavItem.Home.route) {
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