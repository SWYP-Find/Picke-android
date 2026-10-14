package com.picke.presentation.ads

import android.app.Activity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AdMob 보상형 광고 매니저. 현재 비활성 상태로, 모든 함수가 광고 없음으로 동작하는 no-op이다.
 * SDK 의존성과 App ID/광고 단위 ID 설정은 재사용을 위해 남겨 두었다.
 * 원래 로직(로드, SSV custom_data, 보상 지급, ad_revenue 트래킹)은 커밋 cc4e183b^ 의 `app/src/main/java/com/picke/app/di/AdMobManager.kt`를 참고해 복원한다.
 */
@Singleton
class AdMobManager @Inject constructor() {

    fun onMobileAdsInitialized() = Unit

    fun loadAd(userId: String) = Unit

    /**
     * @param placement 광고 노출 위치 식별자 (Mixpanel ad_revenue.placement, snake_case)
     * @return 광고를 띄웠으면 true. 비활성 상태에서는 항상 false
     */
    fun showAd(activity: Activity, placement: String, onRewardEarned: () -> Unit): Boolean = false
}
