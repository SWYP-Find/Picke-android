package com.picke.data.di

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.picke.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MixpanelModule {

    @Provides
    @Singleton
    fun provideMixpanel(@ApplicationContext context: Context): MixpanelAPI {
        val projectToken = BuildConfig.MIXPANEL_PROJECT_TOKEN

        val mixpanel = MixpanelAPI.getInstance(context, projectToken, BuildConfig.DEBUG, true)

        if (BuildConfig.DEBUG) {
            mixpanel.optOutTracking()
        } else if (mixpanel.hasOptedOutTracking()) {
            mixpanel.optInTracking()
        }
        return mixpanel
    }
}