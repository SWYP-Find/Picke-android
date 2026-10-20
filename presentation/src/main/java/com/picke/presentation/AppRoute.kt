package com.picke.presentation

import com.picke.presentation.ui.vote.model.VoteType

sealed class AppRoute(val route: String) {
    object Splash : AppRoute("splash_screen")
    object Login : AppRoute("login_screen")
    object Onboarding : AppRoute("onboarding_screen")
    object Main : AppRoute("main_screen") {
        fun createRoute() = route
    }

    object Alarm : AppRoute("alarm_screen")
    object Setting : AppRoute("setting_screen")

    object BattleRouting : AppRoute("battle_routing_screen/{battleId}") {
        fun createRoute(battleId: String) = "battle_routing_screen/$battleId"
    }

    object DiscussionHistory : AppRoute("discussion_history_screen")
    object PhilosopherType : AppRoute("philosopher_type_screen")
    object OtherPhilosopher : AppRoute("other_philosopher_screen/{reportId}") {
        fun createRoute(reportId: String) = "other_philosopher_screen/$reportId"
    }

    object ContentActivity : AppRoute("content_activity_screen")
    object NoticeEvent : AppRoute("notice_event_screen?noticeId={noticeId}") {
        fun createRoute() = "notice_event_screen"
        fun createRoute(noticeId: Long) = "notice_event_screen?noticeId=$noticeId"
    }

    object SettingProfile : AppRoute("setting_profile_screen")
    object SettingAlarm : AppRoute("setting_alarm_screen")

    object PreVote : AppRoute("pre_vote_screen/{battleId}") {
        fun createRoute(battleId: String) = "pre_vote_screen/$battleId"
    }

    object Scenario : AppRoute("scenario_screen/{battleId}") {
        fun createRoute(battleId: String) = "scenario_screen/$battleId"
    }

    object PostVote : AppRoute("post_vote_screen/{battleId}") {
        fun createRoute(battleId: String) = "post_vote_screen/$battleId"
    }

    object Perspective : AppRoute("perspective_screen/{battleId}?commentId={commentId}") {
        fun createRoute(battleId: String) = "perspective_screen/$battleId"
        fun createRoute(battleId: String, commentId: String) =
            "perspective_screen/$battleId?commentId=$commentId"
    }

    object Comment :
        AppRoute("comment_screen/{itemId}?firstOptionId={firstOptionId}&commentId={commentId}") {
        fun createRoute(itemId: String) = "comment_screen/$itemId"
        fun createRoute(itemId: String, firstOptionId: Long) =
            "comment_screen/$itemId?firstOptionId=$firstOptionId"

        fun createRoute(itemId: String, commentId: String) =
            "comment_screen/$itemId?commentId=$commentId"
    }

    object Recommend : AppRoute("recommend_screen/{battleId}") {
        fun createRoute(battleId: String) = "recommend_screen/$battleId"
    }

    object PrivacyPolicy : AppRoute("privacy_policy_screen")
    object TermsOfService : AppRoute("terms_of_service_screen")
    object Withdraw : AppRoute("withdraw_screen")
    object Point : AppRoute("point_screen")
    object Ticket : AppRoute("ticket_screen")
    object TicketRegister : AppRoute("ticket_register_screen")
    object TicketRegisterComplete : AppRoute("ticket_register_complete_screen/{$ARG_TICKET_ID}") {
        fun createRoute(ticketId: Long) = "ticket_register_complete_screen/$ticketId"
    }

    object MakeBattle : AppRoute("makebattle_screen")

    object ClassCreate : AppRoute("class_create_screen")
    object ClassBattlePreview : AppRoute("class_battle_preview_screen/{$ARG_BATTLE_ID}") {
        fun createRoute(battleId: Long) = "class_battle_preview_screen/$battleId"
    }

    object ClassJoin : AppRoute("class_join_screen")
    object MyClass : AppRoute("my_class_screen")
    object ClassShare : AppRoute("class_share_screen/{$ARG_CLASS_ID}") {
        fun createRoute(classId: Long) = "class_share_screen/$classId"
    }

    object ClassDetail : AppRoute("class_detail_screen/{$ARG_CLASS_ID}") {
        fun createRoute(classId: Long) = "class_detail_screen/$classId"
    }

    object ClassMember : AppRoute("class_member_screen/{$ARG_CLASS_ID}") {
        fun createRoute(classId: Long) = "class_member_screen/$classId"
    }

    object ClassVote : AppRoute("class_vote_screen/{$ARG_CLASS_ID}/{$ARG_VOTE_TYPE}") {
        fun createRoute(classId: Long, voteType: VoteType) =
            "class_vote_screen/$classId/${voteType.name}"
    }

    object ClassScenario : AppRoute("class_scenario_screen/{$ARG_CLASS_ID}/{$ARG_BATTLE_ID}") {
        fun createRoute(classId: Long, battleId: Long) = "class_scenario_screen/$classId/$battleId"
    }

    object ClassPerspective :
        AppRoute("class_perspective_screen/{$ARG_CLASS_ID}/{$ARG_BATTLE_ID}") {
        fun createRoute(classId: Long, battleId: Long) =
            "class_perspective_screen/$classId/$battleId"
    }

    object ClassDebate : AppRoute("class_debate_screen/{$ARG_CLASS_ID}") {
        fun createRoute(classId: Long) = "class_debate_screen/$classId"
    }

    object ClassResult : AppRoute("class_result_screen/{$ARG_CLASS_ID}") {
        fun createRoute(classId: Long) = "class_result_screen/$classId"
    }

    object ClassMyOpinion : AppRoute("class_my_opinion_screen/{$ARG_CLASS_ID}") {
        fun createRoute(classId: Long) = "class_my_opinion_screen/$classId"
    }

    object TodayBattle : AppRoute("tab_battle?battleId={battleId}") {
        fun createRoute(battleId: String) = "tab_battle?battleId=$battleId"
    }

    companion object {
        const val ARG_CLASS_ID = "classId"
        const val ARG_VOTE_TYPE = "voteType"
        const val ARG_BATTLE_ID = "battleId"
        const val ARG_TICKET_ID = "ticketId"
    }
}