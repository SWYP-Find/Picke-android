package com.picke.presentation.util

import com.picke.domain.feature.alarm.model.AlarmItemBoard
import com.picke.domain.feature.battle.model.BattleTagBoard
import com.picke.domain.feature.scenario.model.SpeakerType
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateMessageUiModel
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateSpeaker
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateStatus
import com.picke.presentation.ui.classroom.classdebate.model.ClassDebateUiModel
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiModel
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyCommentUiModel
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyReplyUiModel
import com.picke.presentation.ui.classroom.classresult.model.ClassFeedbackCoverage
import com.picke.presentation.ui.classroom.classresult.model.ClassReportState
import com.picke.presentation.ui.classroom.classresult.model.ClassResultFeedbackItemUiModel
import com.picke.presentation.ui.classroom.classresult.model.ClassResultInsightUiModel
import com.picke.presentation.ui.classroom.classresult.model.ClassResultReportUiModel
import com.picke.presentation.ui.classroom.classresult.model.ClassResultStanceUiModel
import com.picke.presentation.ui.classroom.classresult.model.ClassResultSummaryUiModel
import com.picke.presentation.ui.classroom.model.ClassBattleOptionUiModel
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiModel
import com.picke.presentation.ui.comment.model.CommentUiModel
import com.picke.presentation.ui.explore.model.ExploreUiModel
import com.picke.presentation.ui.home.model.ContentUiType
import com.picke.presentation.ui.home.model.HomeBestCommentUiModel
import com.picke.presentation.ui.home.model.HomeContentUiModel
import com.picke.presentation.ui.home.model.PollQuizOptionStatUiModel
import com.picke.presentation.ui.home.model.TodayPickUiModel
import com.picke.presentation.ui.my.ticket.model.TicketUiModel
import com.picke.presentation.ui.perspective.model.PerspectiveUiModel
import com.picke.presentation.ui.perspective.model.PerspectiveVoteOptionUiModel
import com.picke.presentation.ui.recommend.model.RecommendUiModel
import com.picke.presentation.ui.scenario.model.PastChoice
import com.picke.presentation.ui.scenario.model.ScenarioNodeUiModel
import com.picke.presentation.ui.scenario.model.ScenarioOptionUiModel
import com.picke.presentation.ui.scenario.model.ScenarioScriptUiModel
import com.picke.presentation.ui.scenario.model.ScenarioUiModel
import com.picke.presentation.ui.todaybattle.model.TodayBattleOptionUiModel
import com.picke.presentation.ui.todaybattle.model.TodayBattleUiModel
import com.picke.presentation.ui.vote.model.BattleDetailUiModel
import com.picke.presentation.ui.vote.model.BattleInfoUiModel
import com.picke.presentation.ui.vote.model.BattleOptionUiModel
import com.picke.presentation.ui.vote.model.BattleTagUiModel
import java.time.LocalDate
import java.time.LocalDateTime

object DummyData {

    val dummyAlarmList = listOf(
        AlarmItemBoard(
            notificationId = 0,
            perspectiveId = 0,
            category = "ALL",
            detailCode = "",
            title = "test title1",
            body = "test body1",
            referenceId = 0,
            isRead = true,
            createdAt = ""
        ),
        AlarmItemBoard(
            notificationId = 1,
            perspectiveId = 1,
            category = "CONTENT",
            detailCode = "",
            title = "test title2 test title2 test title2 test title2",
            body = "test body2 test body2 test body2 test body2 test body2 test body2 test body2 test body2 test body2",
            referenceId = 1,
            isRead = true,
            createdAt = ""
        ),
        AlarmItemBoard(
            notificationId = 2,
            perspectiveId = 2,
            category = "NOTICE",
            detailCode = "",
            title = "test title3",
            body = "test body3",
            referenceId = 2,
            isRead = false,
            createdAt = ""
        )
    )

    val dummyMainPerspective = CommentUiModel(
        commentId = 101L,
        profileImageUrl = "",
        nickname = "사유하는 라쿤",
        stance = "변기는 변기다",
        optionId = 1L,
        content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠.\n\n처음에는 말기 환자만을 위한 제도였지만, 시간이 지나면서 대상이 점점 확대되었고, '합리적 선택'이라는 이름 아래 사회적으로 취약한 사람들이 스스로 짐이 된다고 느끼는 상황이 만들어지고 있습니다.",
        timeAgo = "2분 전",
        likeCount = 1340,
        isLiked = false,
        isMine = false,
        replyCount = 23
    )

    val dummyComments = listOf(
        CommentUiModel(
            commentId = 201L,
            profileImageUrl = "",
            nickname = "사색하는 사슴",
            stance = "변기는 변기다",
            optionId = 1L,
            content = "네덜란드 사례를 일반화하기엔 무리가 있지 않나요? 한국의 사회문화적 맥락은 다릅니다.",
            timeAgo = "2분 전",
            likeCount = 1340,
            isLiked = false,
            isMine = false
        ),
        CommentUiModel(
            commentId = 202L,
            profileImageUrl = "",
            nickname = "질문하는 부엉이",
            stance = "예술이다",
            optionId = 2L,
            content = "원가만 보면 그렇지만, 디자인과 브랜드가 쌓아 온 시간까지 원가에 넣어야 하지 않을까요?",
            timeAgo = "15분 전",
            likeCount = 87,
            isLiked = true,
            isMine = false
        ),
        CommentUiModel(
            commentId = 203L,
            profileImageUrl = "",
            nickname = "고민하는 고양이",
            stance = "변기는 변기다",
            optionId = 1L,
            content = "결국 쓰임새가 같다면 가격 차이는 허영이라고 생각해요.",
            timeAgo = "1시간 전",
            likeCount = 12,
            isLiked = false,
            isMine = false
        ),
        CommentUiModel(
            commentId = 204L,
            profileImageUrl = "",
            nickname = "사유하는 라쿤",
            stance = "변기는 변기다",
            optionId = 1L,
            content = "맞아요. 저도 처음엔 같은 생각이었는데, 직접 써 보고 나서 생각이 조금 바뀌었어요.",
            timeAgo = "3시간 전",
            likeCount = 3,
            isLiked = false,
            isMine = true
        )
    )

    val dummyExploreList = listOf(
        ExploreUiModel(
            battleId = "",
            thumbnailUrl = "",
            type = "",
            title = "test explore1",
            summary = "test summary1",
            tags = listOf("tag1", "tag2", "tag3"),
            audioDurationText = "test audio duration text1",
            viewCountText = "view count text1"
        ),
        ExploreUiModel(
            battleId = "",
            thumbnailUrl = "",
            type = "",
            title = "test explore2",
            summary = "test summary2",
            tags = listOf("tag1"),
            audioDurationText = "test audio duration text2",
            viewCountText = "view count text2"
        ),
        ExploreUiModel(
            battleId = "",
            thumbnailUrl = "",
            type = "",
            title = "test explore3",
            summary = "test summary3",
            tags = listOf("tag2", "tag3"),
            audioDurationText = "test audio duration text3",
            viewCountText = "3"
        )
    )

    val dummyHomeContentItems = listOf(
        HomeContentUiModel(
            contentId = "",
            type = ContentUiType.BATTLE,
            title = "best battle title1",
            summary = "best battle summary1",
            thumbnailUrl = "",
            viewCountText = "123",
            timeInfoText = "best battle time info text1",
            tags = emptyList()
        ),
        HomeContentUiModel(
            contentId = "",
            type = ContentUiType.QUIZ,
            title = "best battle title2",
            summary = "best battle summary2",
            thumbnailUrl = "",
            viewCountText = "123456",
            timeInfoText = "best battle time info text2",
            tags = emptyList()
        ),
        HomeContentUiModel(
            contentId = "",
            type = ContentUiType.VOTE,
            title = "best battle title3",
            summary = "best battle summary3",
            thumbnailUrl = "",
            viewCountText = "1",
            timeInfoText = "best battle time info text3",
            tags = emptyList()
        ),
        HomeContentUiModel(
            contentId = "",
            type = ContentUiType.UNKNOWN,
            title = "best battle title4",
            summary = "best battle summary4",
            thumbnailUrl = "",
            viewCountText = "111",
            timeInfoText = "best battle time info text4",
            tags = emptyList()
        )
    )

    val dummyBestBattles = listOf(
        HomeContentUiModel(
            contentId = "1",
            type = ContentUiType.BATTLE,
            title = "인간은 본래 선한가, 악한가?",
            summary = "맹자와 순자가 말하는 인간의 본성",
            thumbnailUrl = "",
            viewCountText = "1,340",
            timeInfoText = "8분",
            tags = listOf("철학", "인문"),
            leftProfileName = "맹자",
            rightProfileName = "순자"
        ),
        HomeContentUiModel(
            contentId = "2",
            type = ContentUiType.BATTLE,
            title = "죽음을 앞둔 사람에게 진실을 말해야 하는가?",
            summary = "선의의 거짓말과 알 권리 사이",
            thumbnailUrl = "",
            viewCountText = "1,340",
            timeInfoText = "8분",
            tags = listOf("철학", "인문"),
            leftProfileName = "칸트",
            rightProfileName = "밀"
        ),
        HomeContentUiModel(
            contentId = "3",
            type = ContentUiType.BATTLE,
            title = "AI와 사랑에 빠지는 것, 진짜 사랑인가?",
            summary = "감정의 진정성은 상대가 사람이어야만 성립하는가",
            thumbnailUrl = "",
            viewCountText = "1,340",
            timeInfoText = "8분",
            tags = listOf("철학", "인문"),
            leftProfileName = "플라톤",
            rightProfileName = "튜링"
        )
    )

    val dummyBestComments = listOf(
        HomeBestCommentUiModel(
            battleId = 1L,
            battleTitle = "인간은 본래 선한가, 악한가?",
            optionTitle = "본성은 선하다",
            content = "아이가 우물에 빠지려는 걸 보면 누구나 먼저 손을 뻗잖아요. 계산하기 전에 움직이는 그 마음이 본성이라고 생각해요."
        ),
        HomeBestCommentUiModel(
            battleId = 2L,
            battleTitle = "죽음을 앞둔 사람에게 진실을 말해야 하는가?",
            optionTitle = "말해야 한다",
            content = "남은 시간을 어떻게 쓸지는 본인이 정해야 해요. 진실을 숨기면 그 선택권까지 빼앗는 거라고 봐요."
        ),
        HomeBestCommentUiModel(
            battleId = 3L,
            battleTitle = "AI와 사랑에 빠지는 것, 진짜 사랑인가?",
            optionTitle = "진짜 사랑이다",
            content = "상대가 무엇이든 내가 느끼는 감정은 진짜잖아요. 사랑을 상대의 조건으로만 판단하는 건 좁은 기준 같아요."
        )
    )

    val dummyVotePick = TodayPickUiModel.VotePick(
        contentId = "vote_001",
        titlePrefix = "Q. ",
        title = "평생 한 가지 음식만 먹어야 한다면?",
        titleSuffix = "",
        summary = "짜장면 vs 짬뽕, 당신의 영혼의 단짝은?",
        participantsCount = 1542,
        selectedOptionId = null,
        type = "VOTE",
        options = listOf(
            PollQuizOptionStatUiModel(
                optionId = 1L,
                title = "윤기 좔좔 짜장면",
                isCorrect = false,
                stance = "짜장면",
                voteCount = 848,
                ratio = 55.0f
            ),
            PollQuizOptionStatUiModel(
                optionId = 2L,
                title = "얼큰한 국물 짬뽕",
                isCorrect = false,
                stance = "짬뽕",
                voteCount = 694,
                ratio = 45.0f
            )
        )
    )

    val dummyQuizPick = TodayPickUiModel.QuizPick(
        contentId = "quiz_001",
        title = "다음 중 안드로이드의 공식 마스코트 이름은?",
        summary = "알쏭달쏭 IT 상식 퀴즈",
        participantsCount = 820,
        selectedOptionId = 1L,
        type = "QUIZ",
        options = listOf(
            PollQuizOptionStatUiModel(
                optionId = 1L,
                title = "버그드로이드 (Bugdroid)",
                isCorrect = true,
                stance = "NONE",
                voteCount = 697,
                ratio = 85.0f
            ),
            PollQuizOptionStatUiModel(
                optionId = 2L,
                title = "안디 (Andy)",
                isCorrect = false,
                stance = "NONE",
                voteCount = 82,
                ratio = 10.0f
            ),
            PollQuizOptionStatUiModel(
                optionId = 3L,
                title = "로보 (Robo)",
                isCorrect = false,
                stance = "NONE",
                voteCount = 41,
                ratio = 5.0f
            )
        )
    )

    val dummyVoteOptions = listOf(
        PerspectiveVoteOptionUiModel(
            optionId = 1L,
            title = "변기는 변기다",
            imageUrl = "",
            ratio = 59.5f
        ),
        PerspectiveVoteOptionUiModel(
            optionId = 2L,
            title = "예술이다",
            imageUrl = "",
            ratio = 40.5f
        )
    )

    val dummyPerspectives = listOf(
        PerspectiveUiModel(
            perspectiveId = 101L,
            profileImageUrl = "",
            nickname = "사유하는 라쿤",
            optionTitle = "변기는 변기다",
            optionId = 1L,
            content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠. 제도화가 진행될수록 개인의 판단보다 사회의 기준이 앞서게 됩니다.",
            timeAgo = "2분 전",
            replyCount = 23,
            likeCount = 1340,
            isLiked = true,
            isMine = false
        ),
        PerspectiveUiModel(
            perspectiveId = 102L,
            profileImageUrl = "",
            nickname = "질문하는 부엉이",
            optionTitle = "예술이다",
            optionId = 2L,
            content = "뒤샹의 '샘'이 미술관에 놓인 순간 변기는 더 이상 변기가 아니었어요. 가격은 물건이 아니라 그 물건이 던지는 질문에 붙는 거라고 봅니다.",
            timeAgo = "10분 전",
            replyCount = 8,
            likeCount = 842,
            isLiked = false,
            isMine = false
        ),
        PerspectiveUiModel(
            perspectiveId = 103L,
            profileImageUrl = "",
            nickname = "사색하는 사슴",
            optionTitle = "변기는 변기다",
            optionId = 1L,
            content = "원가 18만 원짜리를 수백만 원에 사는 건 결국 이름값을 사는 거죠.",
            timeAgo = "1시간 전",
            replyCount = 0,
            likeCount = 45,
            isLiked = false,
            isMine = false
        ),
        PerspectiveUiModel(
            perspectiveId = 104L,
            profileImageUrl = "",
            nickname = "고민하는 고양이",
            optionTitle = "예술이다",
            optionId = 2L,
            content = "쓰임새만 따지면 모든 디자인 제품이 사기가 됩니다. 아름다움에도 값이 있어요.",
            timeAgo = "3시간 전",
            replyCount = 2,
            likeCount = 15,
            isLiked = false,
            isMine = true
        )
    )

    val dummyRecommends = listOf(
        RecommendUiModel(
            battleId = "rec_001",
            title = "평생 한 가지 음식만 먹어야 한다면?",
            summary = "짜장면 vs 짬뽕, 당신의 소울푸드를 선택해주세요!",
            audioDuration = 145, // 2분 25초
            viewCount = 15420,
            participantsCount = 8900,
            tags = listOf("음식", "취향", "밸런스게임"),
            imageA = "",
            imageB = "",
            stanceA = "짜장면",
            stanceB = "짬뽕",
            representativeA = "윤기 좔좔 간짜장",
            representativeB = "얼큰한 차돌짬뽕"
        ),
        RecommendUiModel(
            battleId = "rec_002",
            title = "태블릿 PC, 정말 필수일까?",
            summary = "생산성 향상을 위한 필수템 vs 스마트폰과 노트북으로 충분하다",
            audioDuration = 340,
            viewCount = 8230,
            participantsCount = 3120,
            tags = listOf("IT", "전자기기", "소비"),
            imageA = "",
            imageB = "",
            stanceA = "필수템이다",
            stanceB = "사치템이다",
            representativeA = "아이패드 프로",
            representativeB = "스마트폰 & 노트북"
        ),
        RecommendUiModel(
            battleId = "rec_003",
            title = "가장 이상적인 근무 형태는?",
            summary = "출퇴근 시간 아끼는 재택근무 vs 동료들과 소통하는 사무실 출근",
            audioDuration = 275,
            viewCount = 21050,
            participantsCount = 12500,
            tags = listOf("직장인", "워라밸", "라이프스타일"),
            imageA = "",
            imageB = "",
            stanceA = "풀 재택근무",
            stanceB = "사무실 출근",
            representativeA = "내 방 데스크셋업",
            representativeB = "강남 오피스"
        )
    )

    val dummyScripts = listOf(
        ScenarioScriptUiModel(
            scriptId = "script_001",
            startTimeMs = 0L,
            speakerType = SpeakerType.NARRATOR,
            speakerName = "진행자",
            displayText = "지금부터 '평생 한 가지 음식만 먹어야 한다면?'을 주제로 배틀을 시작하겠습니다.",
            profileImageUrl = null
        ),
        ScenarioScriptUiModel(
            scriptId = "script_002",
            startTimeMs = 4500L,
            speakerType = SpeakerType.A,
            speakerName = "윤기좔좔 짜장파",
            displayText = "당연히 짜장면 아닌가요? 달콤하고 짭짤한 춘장 소스에 단무지 하나 올려 먹으면 매일 먹어도 안 질립니다.",
            profileImageUrl = ""
        ),
        ScenarioScriptUiModel(
            scriptId = "script_003",
            startTimeMs = 11000L,
            speakerType = SpeakerType.B,
            speakerName = "얼큰국물 짬뽕파",
            displayText = "비 오는 날 짜장면 드실 겁니까? 짬뽕의 얼큰한 국물과 불맛은 절대 포기할 수 없죠.",
            profileImageUrl = ""
        ),
        ScenarioScriptUiModel(
            scriptId = "script_004",
            startTimeMs = 17500L,
            speakerType = SpeakerType.A,
            speakerName = "윤기좔좔 짜장파",
            displayText = "짬뽕은 먹고 나면 옷에 국물 튀어서 불편하기만 합니다.",
            profileImageUrl = ""
        ),
        ScenarioScriptUiModel(
            scriptId = "script_005",
            startTimeMs = 21000L,
            speakerType = SpeakerType.USER,
            speakerName = "나",
            displayText = "음... 저는 짬짜면으로 합의 보겠습니다.",
            profileImageUrl = ""
        ),
        ScenarioScriptUiModel(
            scriptId = "script_006",
            startTimeMs = 24500L,
            speakerType = SpeakerType.UNKNOWN,
            speakerName = "익명의 방청객",
            displayText = "(웅성웅성) 짬짜면은 반칙 아닌가요?",
            profileImageUrl = null
        )
    )

    val dummyPastChoices = listOf(
        PastChoice(
            scriptIndex = 5,
            options = listOf(
                ScenarioOptionUiModel(
                    label = "짜장면 측 반론 듣기",
                    nextNodeId = "node_pro_rebuttal"
                ),
                ScenarioOptionUiModel(
                    label = "짬뽕 측 반론 듣기",
                    nextNodeId = "node_con_rebuttal"
                )
            ),
            selectedNextNodeId = "node_con_rebuttal"
        ),
        PastChoice(
            scriptIndex = 12,
            options = listOf(
                ScenarioOptionUiModel(
                    label = "최종 결론 듣기",
                    nextNodeId = "node_conclusion"
                ),
                ScenarioOptionUiModel(
                    label = "전문가 의견 듣기",
                    nextNodeId = "node_expert"
                ),
                ScenarioOptionUiModel(
                    label = "바로 투표하기",
                    nextNodeId = "node_vote"
                )
            ),
            selectedNextNodeId = "node_conclusion"
        )
    )

    val dummyTodayBattles = listOf(
        TodayBattleUiModel(
            battleId = "battle_001",
            imageUrl = "",
            tags = listOf("음식", "취향", "국룰"),
            title = "영원한 난제, 탕수육 먹을 때 당신의 선택은?",
            description = "바삭함이 생명인 찍먹파 vs 소스가 촉촉하게 스며든 부먹파",
            timeLeft = "02:15:30",
            options = listOf(
                TodayBattleOptionUiModel(
                    optionId = "opt_1",
                    name = "찍먹",
                    opinion = "마지막 한 조각까지 바삭함을 잃지 않아야 진짜 탕수육이죠. 눅눅한 고기는 용납할 수 없습니다.",
                    quote = "바삭함은 생명이다!"
                ),
                TodayBattleOptionUiModel(
                    optionId = "opt_2",
                    name = "부먹",
                    opinion = "달콤한 소스가 고기 튀김옷에 촉촉하게 스며들었을 때의 그 부드러움이 진리입니다.",
                    quote = "촉촉함이 근본이다!"
                )
            )
        ),
        TodayBattleUiModel(
            battleId = "battle_002",
            imageUrl = "",
            tags = listOf("직장인", "워라밸", "라이프"),
            title = "회사 복지, 하나만 선택할 수 있다면?",
            description = "둘 다 포기할 수 없지만... 당신의 출근길을 더 행복하게 만들 복지는?",
            timeLeft = "14:30:00",
            options = listOf(
                TodayBattleOptionUiModel(
                    optionId = "opt_3",
                    name = "주 4일제",
                    opinion = "월급이 10% 줄어들더라도 나만의 휴식과 취미 생활을 즐길 수 있는 시간이 훨씬 소중합니다.",
                    quote = "월급보단 내 시간!"
                ),
                TodayBattleOptionUiModel(
                    optionId = "opt_4",
                    name = "주 5일제",
                    opinion = "어차피 일할 거 20% 더 벌어서 주말을 화려하고 윤택하게 보내는 것이 낫습니다.",
                    quote = "결국 남는 건 돈이다!"
                )
            )
        ),
        TodayBattleUiModel(
            battleId = "battle_003",
            imageUrl = "",
            tags = listOf("상상", "초능력", "밸런스게임"),
            title = "당신에게 초능력이 생긴다면?",
            description = "누구나 한 번쯤 상상해본 초능력! 실생활에 더 유용한 능력은?",
            timeLeft = "D-2",
            options = listOf(
                TodayBattleOptionUiModel(
                    optionId = "opt_5",
                    name = "순간이동",
                    opinion = "지옥철 출퇴근 시간을 아끼고, 전 세계 어디든 1초 만에 여행 갈 수 있는 최고의 능력입니다.",
                    quote = "지옥철, 이제 안녕!"
                ),
                TodayBattleOptionUiModel(
                    optionId = "opt_6",
                    name = "독심술",
                    opinion = "사람들의 진짜 속마음을 파악하면 직장 생활, 연애, 인간관계 모든 것에서 성공할 수 있습니다.",
                    quote = "네 속마음 다 보여!"
                )
            )
        )
    )

    val dummyBattleDetailList = listOf(
        BattleDetailUiModel(
            battleInfo = BattleInfoUiModel(
                battleId = "battle_detail_001",
                title = "브레이크가 고장 난 트롤리, 당신의 선택은?",
                summary = "다수를 위한 소수의 희생은 정당한가?",
                thumbnailUrl = "",
                viewCount = 45210,
                participantsCount = 12500,
                audioDuration = 180,
                tags = listOf(
                    BattleTagBoard("board_tag_1", "철학", "PHILOSOPHER"),
                    BattleTagBoard("board_tag_2", "윤리", "VALUE")
                ),
                options = listOf(
                    BattleOptionUiModel(
                        optionId = "opt_1",
                        title = "레버를 당긴다 (1명 희생)",
                        stance = "PRO",
                        representative = "공리주의",
                        imageUrl = "",
                        tags = listOf(
                            BattleTagUiModel("opt_tag_1", "제러미 벤담", "PHILOSOPHER"),
                            BattleTagUiModel("opt_tag_2", "최대 다수의 최대 행복", "VALUE")
                        )
                    ),
                    BattleOptionUiModel(
                        optionId = "opt_2",
                        title = "개입하지 않는다 (5명 희생)",
                        stance = "CON",
                        representative = "의무론",
                        imageUrl = "",
                        tags = listOf(
                            BattleTagUiModel("opt_tag_3", "임마누엘 칸트", "PHILOSOPHER"),
                            BattleTagUiModel("opt_tag_4", "인간은 수단이 아닌 목적", "VALUE")
                        )
                    )
                )
            ),
            description = "브레이크가 고장 난 트롤리가 질주하고 있습니다. 이대로면 선로 위에 묶인 5명의 인부가 목숨을 잃게 됩니다. 당신은 선로를 바꿀 수 있는 레버 앞에 서 있습니다. 레버를 당기면 5명을 살릴 수 있지만, 다른 선로에 있는 1명의 인부가 희생됩니다. 당신은 어떤 선택을 내리시겠습니까?",
            shareUrl = "https://picke.com/battle/001",
            userVoteStatus = "VOTED_A",
            currentStep = "PERSPECTIVE",
            categoryTags = listOf(
                BattleTagUiModel("cat_1", "철학", "CATEGORY")
            ),
            philosopherTags = listOf(
                BattleTagUiModel("phil_1", "제러미 벤담", "PHILOSOPHER"),
                BattleTagUiModel("phil_2", "임마누엘 칸트", "PHILOSOPHER")
            ),
            valueTags = listOf(
                BattleTagUiModel("val_1", "공리주의", "VALUE"),
                BattleTagUiModel("val_2", "의무론", "VALUE")
            )
        ),
        BattleDetailUiModel(
            battleInfo = BattleInfoUiModel(
                battleId = "battle_detail_002",
                title = "인공지능(AI) 판사, 도입해야 할까?",
                summary = "감정 없는 공정한 판결 vs 인간에 의한 맥락적 이해",
                thumbnailUrl = "",
                viewCount = 38900,
                participantsCount = 9800,
                audioDuration = 240,
                tags = listOf(
                    BattleTagBoard("board_tag_3", "사회", "SOCIAL"),
                    BattleTagBoard("board_tag_4", "과학", "SCIENCE")
                ),
                options = listOf(
                    BattleOptionUiModel(
                        optionId = "opt_3",
                        title = "도입 찬성 (AI 판사)",
                        stance = "PRO",
                        representative = "객관성과 효율성",
                        imageUrl = "",
                        tags = listOf(
                            BattleTagUiModel("opt_tag_5", "데이터 기반 판결", "VALUE")
                        )
                    ),
                    BattleOptionUiModel(
                        optionId = "opt_4",
                        title = "도입 반대 (인간 판사)",
                        stance = "CON",
                        representative = "인간적 맥락 이해",
                        imageUrl = "",
                        tags = listOf(
                            BattleTagUiModel("opt_tag_6", "법적 안정성", "VALUE")
                        )
                    )
                )
            ),
            description = "인공지능 기술이 발전하면서 법률 분야에도 AI가 도입되고 있습니다. 과거의 판례를 분석하여 편향 없이 객관적이고 일관된 판결을 내릴 수 있다는 찬성 의견과, 법은 수학 공식이 아니며 피고인의 반성 태도나 정황 등 인간만이 이해할 수 있는 맥락을 고려해야 한다는 반대 의견이 맞서고 있습니다.",
            shareUrl = "https://picke.com/battle/002",
            userVoteStatus = "NONE",
            currentStep = "SCENARIO",
            categoryTags = listOf(
                BattleTagUiModel("cat_2", "사회", "CATEGORY"),
                BattleTagUiModel("cat_3", "과학", "CATEGORY")
            ),
            philosopherTags = emptyList(),
            valueTags = listOf(
                BattleTagUiModel("val_3", "공정성", "VALUE"),
                BattleTagUiModel("val_4", "인도주의", "VALUE")
            )
        )
    )

    const val dummyClassCode = "PK7M2Q"
    val dummyClassDeadline: LocalDateTime = LocalDateTime.of(2026, 9, 23, 18, 0)

    val dummyClassBattles = listOf(
        ClassBattleUiModel(
            battleId = 101L,
            tag = "#사회",
            title = "촉법소년 연령을 낮춰야 할까?",
            description = "날로 잔혹해지는 청소년 범죄, 당신은 강력한 처벌을 원하십니까, 아니면 기회와 교정을 원하십니까?",
            durationMinutes = 5,
            options = listOf(
                ClassBattleOptionUiModel(
                    stance = "낮춰야 한다",
                    subText = "칸트",
                    imageUrl = null
                ),
                ClassBattleOptionUiModel(
                    stance = "교정이 우선이다",
                    subText = "벤담",
                    imageUrl = null
                )
            )
        ),
        ClassBattleUiModel(
            battleId = 102L,
            tag = "#철학",
            title = "인간은 본래 선한가, 악한가?",
            description = "인간 본성의 선악과 문명의 역할에 관한 철학적 대결!",
            durationMinutes = 5,
            options = listOf(
                ClassBattleOptionUiModel(
                    stance = "악하다",
                    subText = "순자",
                    imageUrl = null
                ),
                ClassBattleOptionUiModel(
                    stance = "선하다",
                    subText = "노자",
                    imageUrl = null
                )
            )
        ),
        ClassBattleUiModel(
            battleId = 103L,
            tag = "#철학",
            title = "불매운동은 소비자의 권리인가?",
            description = "기업 불매운동은 사회를 바꾸는 소비자의 권리일까, 도덕적 우월감일까?",
            durationMinutes = 5,
            options = listOf(
                ClassBattleOptionUiModel(
                    stance = "소비자의 권리",
                    subText = "마르크스",
                    imageUrl = null
                ),
                ClassBattleOptionUiModel(
                    stance = "도덕적 우월감",
                    subText = "니체",
                    imageUrl = null
                )
            )
        )
    )

    val dummyClassAiQuestions = listOf(
        ClassBattleUiModel(
            battleId = 201L,
            tag = "#도덕",
            title = "슬픔을 드러내지 않은 뫼르소를 비난할 수 있을까?",
            description = "감정을 표현하지 않는 태도를 도덕적으로 판단할 수 있는지 이야기해요.",
            options = listOf(
                ClassBattleOptionUiModel(stance = "비난할 수 있다", subText = "사회적 공감도 중요하다"),
                ClassBattleOptionUiModel(stance = "유지해야 한다", subText = "연령과 교정 가능성 고려")
            ),
            isAiQuestion = true,
            commentCount = 23
        ),
        ClassBattleUiModel(
            battleId = 202L,
            tag = "#범죄",
            title = "재판에서 삶의 태도까지 판단 근거가 되어도 될까?",
            description = "뫼르소의 범죄와 무관한 태도가 재판에 영향을 주는 것이 정당할까요?",
            options = listOf(
                ClassBattleOptionUiModel(stance = "고려해도 된다", subText = "인물의 태도도 판단의 일부다"),
                ClassBattleOptionUiModel(stance = "범죄만 봐야 한다", subText = "행위와 증거만 판단해야 한다")
            ),
            isAiQuestion = true
        ),
        ClassBattleUiModel(
            battleId = 203L,
            tag = "#철학",
            title = "삶에 정해진 의미가 없다는 태도는 자유일까?",
            description = "뫼르소의 삶의 태도를 개인의 자유와 책임이라는 관점에서 생각해봐요.",
            options = listOf(
                ClassBattleOptionUiModel(stance = "개인의 자유다", subText = "의미는 스스로 정할 수 있다"),
                ClassBattleOptionUiModel(stance = "무책임한 태도다", subText = "타인과 사회에 대한 책임이 있다")
            ),
            isAiQuestion = true
        )
    )

    val dummyMyClasses = listOf(
        MyClassUiModel(
            1L,
            "1학년 3반 사회 토론",
            "촉법소년 연령을 낮춰야 할까?",
            true,
            32,
            LocalDateTime.of(2026, 11, 23, 20, 0),
            isOperator = true
        ),
        MyClassUiModel(
            2L,
            "윤리와 사상 토론 클래스",
            "인간은 본래 선한가?",
            true,
            13,
            LocalDateTime.of(2026, 10, 11, 23, 59),
            isOperator = false
        ),
        MyClassUiModel(
            3L,
            "주간 독서토론 모임",
            "무지는 죄인가?",
            false,
            4,
            LocalDateTime.of(2026, 9, 23, 18, 0),
            isOperator = false
        )
    )

    val dummyClassResultSummary = ClassResultSummaryUiModel(
        memberCount = 32,
        participatedCount = 28,
        commentedCount = 24,
        stances = listOf(
            ClassResultStanceUiModel(label = "낮춰야 한다", count = 18),
            ClassResultStanceUiModel(label = "교정이 우선이다", count = 10)
        )
    )

    val dummyClassResultInsight = ClassResultInsightUiModel(
        state = ClassReportState.SUCCEEDED,
        insightText = "처음에는 처벌을 강화해야 한다는 의견이 많았지만, 대화 후에는 교정과 보호를 함께 봐야 한다는 의견이 늘어났어요.",
        isRetryable = false,
        bestComment = "처벌만 강화하면 아이들이 다시 사회로 돌아올 길이 더 좁아진다고 생각해요.",
        mostDebatedComment = "피해자의 고통을 생각하면 나이와 상관없이 책임을 지게 해야 한다고 생각해요."
    )

    val dummyClassResultReport = ClassResultReportUiModel(
        state = ClassReportState.SUCCEEDED,
        preVoteStance = "낮춰야 한다",
        postVoteStance = "교정이 우선이다",
        perspectiveCount = 1,
        commentCount = 2,
        receivedLikeCount = 12,
        judgmentBasis = "마지막 의견에서는 재범을 줄이는 방법으로 교육과 보호를 중요하게 봤어요.",
        feedbackItems = listOf(
            ClassResultFeedbackItemUiModel(
                titleRes = R.string.class_result_strength,
                content = "“처벌만으로는 해결되지 않는다”는 반대 의견에 그치지 않고, 교육과 보호라는 대안을 제시했어요. 해결 방법까지 논의를 이어 간 점이 좋았어요."
            ),
            ClassResultFeedbackItemUiModel(
                titleRes = R.string.class_result_next_focus,
                content = "교육과 보호가 어떤 상황에서 효과적일지는 아직 설명되지 않았어요. 대안을 제시한 만큼, 그 대안이 효과를 낼 조건을 덧붙이면 주장이 더 분명해져요."
            ),
            ClassResultFeedbackItemUiModel(
                titleRes = R.string.class_result_next_question,
                content = "교육과 보호가 재범을 줄이려면, 어떤 조건이 가장 먼저 갖춰져야 할까요?"
            )
        ),
        coverage = ClassFeedbackCoverage.FULL
    )

    val dummyClassMyComments = listOf(
        ClassMyCommentUiModel(
            commentId = 1L,
            stance = "낮춰야 한다",
            content = "처벌만 강화하면 재범을 막기 어렵다고 생각해요. 교육과 보호가 함께 가야 해요.",
            likeCount = 1340,
            replyCount = 23
        )
    )

    val dummyClassMyReplies = listOf(
        ClassMyReplyUiModel(
            replyId = 1L,
            parentAuthorName = "김민지",
            parentStance = "교정이 우선이다",
            parentContent = "나이가 어리다는 이유만으로 책임을 덜어 주면 피해자는 누가 보호하나요?",
            authorName = "공은지",
            content = "피해자 보호도 중요하지만, 교정이 이루어져야 같은 피해가 다시 생기지 않는다고 생각해요."
        ),
        ClassMyReplyUiModel(
            replyId = 2L,
            parentAuthorName = "이서준",
            parentStance = "낮춰야 한다",
            parentContent = "요즘 청소년 범죄는 예전보다 훨씬 계획적이에요.",
            authorName = "공은지",
            content = "계획적인 범죄라면 처벌 기준을 따로 두는 방법도 함께 생각해 볼 수 있을 것 같아요."
        )
    )

    val dummyClassAiBattleDetail = BattleDetailUiModel(
        battleInfo = BattleInfoUiModel(
            battleId = "201",
            title = "슬픔을 드러내지 않은 뫼르소를 비난할 수 있을까?",
            summary = "감정을 표현하지 않는 태도를 도덕적으로 판단할 수 있는지 이야기해요.",
            thumbnailUrl = "",
            viewCount = 0,
            participantsCount = 0,
            audioDuration = 0,
            tags = listOf(BattleTagBoard("class_tag_1", "도덕", "CATEGORY")),
            options = listOf(
                BattleOptionUiModel(
                    optionId = "201_1",
                    title = "비난할 수 있다",
                    stance = "PRO",
                    representative = "사회적 공감도 중요하다",
                    imageUrl = "",
                    tags = emptyList()
                ),
                BattleOptionUiModel(
                    optionId = "201_2",
                    title = "유지해야 한다",
                    stance = "CON",
                    representative = "연령과 교정 가능성 고려",
                    imageUrl = "",
                    tags = emptyList()
                )
            )
        ),
        description = "감정을 표현하지 않는 태도를 도덕적으로 판단할 수 있는지 이야기해요.",
        shareUrl = "",
        userVoteStatus = "",
        currentStep = "",
        categoryTags = emptyList(),
        philosopherTags = emptyList(),
        valueTags = emptyList()
    )

    val dummyClassBattlePreviewScenarios = mapOf(
        101L to ScenarioUiModel(
            battleId = "101",
            isInteractive = false,
            startNodeId = "node_opening",
            audios = emptyMap(),
            nodes = mapOf(
                "node_opening" to ScenarioNodeUiModel(
                    nodeId = "node_opening",
                    nodeName = "opening",
                    audioDuration = 75,
                    autoNextNodeId = null,
                    scripts = listOf(
                            ScenarioScriptUiModel(
                                scriptId = "preview_101_1",
                                startTimeMs = 0L,
                                speakerType = SpeakerType.A,
                                speakerName = "칸트",
                                displayText = "범죄는 나이와 상관없이 도덕 법칙을 어긴 행위입니다. 스스로 판단할 수 있다면 그 결과에도 책임을 져야 합니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_101_2",
                                startTimeMs = 9000L,
                                speakerType = SpeakerType.B,
                                speakerName = "벤담",
                                displayText = "책임을 묻는 것과 처벌을 늘리는 것은 다릅니다. 처벌은 사회 전체의 고통을 줄일 때만 정당합니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_101_3",
                                startTimeMs = 19000L,
                                speakerType = SpeakerType.A,
                                speakerName = "칸트",
                                displayText = "그렇다면 피해자의 고통은 누가 갚습니까? 처벌은 계산이 아니라 정의의 요구입니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_101_4",
                                startTimeMs = 29000L,
                                speakerType = SpeakerType.B,
                                speakerName = "벤담",
                                displayText = "청소년을 교정하면 앞으로 생길 범죄를 줄일 수 있습니다. 그것이 더 많은 사람을 지키는 길입니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_101_5",
                                startTimeMs = 40000L,
                                speakerType = SpeakerType.A,
                                speakerName = "칸트",
                                displayText = "사람을 결과를 위한 수단으로 다루면 안 됩니다. 소년도 이성을 가진 존재로 존중해야 합니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_101_6",
                                startTimeMs = 51000L,
                                speakerType = SpeakerType.B,
                                speakerName = "벤담",
                                displayText = "존중한다면 다시 설 기회를 주어야지요. 처벌만으로는 아무도 행복해지지 않습니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_101_7",
                                startTimeMs = 62000L,
                                speakerType = SpeakerType.A,
                                speakerName = "칸트",
                                displayText = "기회는 책임을 인정한 뒤에야 의미가 있습니다."
                            )
                    ),
                    interactiveOptions = emptyList()
                )
            )
        ),
        102L to ScenarioUiModel(
            battleId = "102",
            isInteractive = false,
            startNodeId = "node_opening",
            audios = emptyMap(),
            nodes = mapOf(
                "node_opening" to ScenarioNodeUiModel(
                    nodeId = "node_opening",
                    nodeName = "opening",
                    audioDuration = 75,
                    autoNextNodeId = null,
                    scripts = listOf(
                            ScenarioScriptUiModel(
                                scriptId = "preview_102_1",
                                startTimeMs = 0L,
                                speakerType = SpeakerType.A,
                                speakerName = "순자",
                                displayText = "사람은 태어날 때부터 이익을 좋아하고 시기하는 마음을 가지고 있습니다. 그래서 예와 교육으로 바로잡아야 합니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_102_2",
                                startTimeMs = 9000L,
                                speakerType = SpeakerType.B,
                                speakerName = "노자",
                                displayText = "억지로 바로잡으려 할수록 사람은 본래의 모습을 잃습니다. 갓난아이의 마음은 꾸밈이 없고 부드럽습니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_102_3",
                                startTimeMs = 19000L,
                                speakerType = SpeakerType.A,
                                speakerName = "순자",
                                displayText = "꾸밈이 없다고 선한 것은 아닙니다. 배우지 않은 아이는 다툼부터 배웁니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_102_4",
                                startTimeMs = 29000L,
                                speakerType = SpeakerType.B,
                                speakerName = "노자",
                                displayText = "다툼은 욕심을 부추기는 세상이 만든 것입니다. 물처럼 낮은 곳으로 흐르면 다툴 일이 없습니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_102_5",
                                startTimeMs = 40000L,
                                speakerType = SpeakerType.A,
                                speakerName = "순자",
                                displayText = "세상이 저절로 바로 선 적은 없습니다. 성인이 예를 만들었기에 질서가 생긴 것입니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_102_6",
                                startTimeMs = 51000L,
                                speakerType = SpeakerType.B,
                                speakerName = "노자",
                                displayText = "예가 생긴 것은 이미 도를 잃었기 때문입니다. 규칙이 많아질수록 사람은 더 영리하게 어깁니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_102_7",
                                startTimeMs = 62000L,
                                speakerType = SpeakerType.A,
                                speakerName = "순자",
                                displayText = "그렇다면 규칙 없는 세상에서 약한 사람은 누가 지킵니까?"
                            )
                    ),
                    interactiveOptions = emptyList()
                )
            )
        ),
        103L to ScenarioUiModel(
            battleId = "103",
            isInteractive = false,
            startNodeId = "node_opening",
            audios = emptyMap(),
            nodes = mapOf(
                "node_opening" to ScenarioNodeUiModel(
                    nodeId = "node_opening",
                    nodeName = "opening",
                    audioDuration = 75,
                    autoNextNodeId = null,
                    scripts = listOf(
                            ScenarioScriptUiModel(
                                scriptId = "preview_103_1",
                                startTimeMs = 0L,
                                speakerType = SpeakerType.A,
                                speakerName = "마르크스",
                                displayText = "불매운동은 소비자가 자본에 맞설 수 있는 몇 안 되는 수단입니다. 지갑을 닫는 것은 정치적 행동입니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_103_2",
                                startTimeMs = 9000L,
                                speakerType = SpeakerType.B,
                                speakerName = "니체",
                                displayText = "정말 그럴까요? 많은 불매는 남보다 도덕적이라고 느끼고 싶은 마음에서 나옵니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_103_3",
                                startTimeMs = 19000L,
                                speakerType = SpeakerType.A,
                                speakerName = "마르크스",
                                displayText = "동기가 무엇이든 노동자를 착취하는 기업에 손실을 준다면 의미가 있습니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_103_4",
                                startTimeMs = 29000L,
                                speakerType = SpeakerType.B,
                                speakerName = "니체",
                                displayText = "남을 심판하며 얻는 쾌감은 약자의 원한에서 나옵니다. 그것은 강해지는 길이 아닙니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_103_5",
                                startTimeMs = 40000L,
                                speakerType = SpeakerType.A,
                                speakerName = "마르크스",
                                displayText = "원한이 아니라 연대입니다. 혼자서는 약해도 함께하면 구조를 바꿀 수 있습니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_103_6",
                                startTimeMs = 51000L,
                                speakerType = SpeakerType.B,
                                speakerName = "니체",
                                displayText = "무리를 따르는 것을 연대라고 부르는군요. 스스로 가치를 만드는 사람은 군중 뒤에 숨지 않습니다."
                            ),
                            ScenarioScriptUiModel(
                                scriptId = "preview_103_7",
                                startTimeMs = 62000L,
                                speakerType = SpeakerType.A,
                                speakerName = "마르크스",
                                displayText = "구조를 보지 않는 개인주의야말로 자본이 바라는 것입니다."
                            )
                    ),
                    interactiveOptions = emptyList()
                )
            )
        )
    )

    val dummyClassBattleByClassId = mapOf(
        1L to dummyClassBattles[0],
        2L to dummyClassAiQuestions[0],
        3L to dummyClassBattles[2]
    )

    val dummyClassDebateOpening = listOf(
        ClassDebateMessageUiModel(
            messageId = 1L,
            speaker = ClassDebateSpeaker.AI,
            text = "감정을 겉으로 드러내지 않았다고 해서 그 사람을 비난할 수는 없습니다."
        ),
        ClassDebateMessageUiModel(
            messageId = 2L,
            speaker = ClassDebateSpeaker.AI,
            text = "슬픔은 저마다의 방식으로 존재하고, 눈물만이 그 증거는 아닙니다."
        ),
        ClassDebateMessageUiModel(
            messageId = 3L,
            speaker = ClassDebateSpeaker.AI,
            text = "보이는 모습만으로 마음을 판단하는 것은 진리를 놓치는 일입니다."
        )
    )

    val dummyClassDebateMyMessage = ClassDebateMessageUiModel(
        messageId = 4L,
        speaker = ClassDebateSpeaker.USER,
        text = "하지만 장례식처럼 함께 슬퍼하는 자리에서는 최소한의 공감을 보여야 하지 않을까요?"
    )

    val dummyClassDebateReplies = listOf(
        listOf(
            ClassDebateMessageUiModel(
                messageId = 5L,
                speaker = ClassDebateSpeaker.AI,
                text = "공감을 보여야 한다는 요구는 결국 남의 눈에 맞춘 연기를 강요하는 것 아닐까요?"
            )
        ),
        listOf(
            ClassDebateMessageUiModel(
                messageId = 6L,
                speaker = ClassDebateSpeaker.AI,
                text = "사회의 약속이 중요하더라도, 그 약속을 지키지 않았다는 이유로 마음까지 단정할 수는 없습니다."
            )
        ),
        listOf(
            ClassDebateMessageUiModel(
                messageId = 7L,
                speaker = ClassDebateSpeaker.AI,
                text = "좋은 토론이었습니다. 이제 최종 투표로 당신의 생각을 정리해 보세요."
            )
        )
    )

    val dummyClassDebate = ClassDebateUiModel(
        round = 1,
        status = ClassDebateStatus.WAITING_USER,
        canReply = true,
        myChoice = "비난할 수 있다",
        opponentName = "플라톤",
        messages = dummyClassDebateOpening,
        replyMinLength = 50,
        replyMaxLength = 500
    )

    val dummyClassMembers = listOf(
        ClassMemberUiModel(1L, "김선생", null, isOperator = true, isMe = true),
        ClassMemberUiModel(2L, "공은지", null, isOperator = false, isMe = false),
        ClassMemberUiModel(3L, "권동현", null, isOperator = false, isMe = false),
        ClassMemberUiModel(4L, "김민지", null, isOperator = false, isMe = false),
        ClassMemberUiModel(5L, "천다올", null, isOperator = false, isMe = false),
        ClassMemberUiModel(6L, "유시영", null, isOperator = false, isMe = false)
    )

    val dummyParticipantClassMembers = dummyClassMembers.map { member -> member.copy(isMe = member.memberId == 3L) }

    val dummyTicket = TicketUiModel(
        maxMemberCount = 30,
        durationMonths = 6
    )

    val dummyRegisteredTicket = dummyTicket.copy(
        startDate = LocalDate.of(2026, 10, 9),
        endDate = LocalDate.of(2027, 4, 8)
    )

    val dummyRegisteredTickets = listOf(dummyRegisteredTicket)
}