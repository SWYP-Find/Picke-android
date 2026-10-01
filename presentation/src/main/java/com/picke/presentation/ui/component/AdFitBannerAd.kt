package com.picke.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView
import com.picke.presentation.ui.theme.PickeTheme

private val AdFitBanner320x100Size = DpSize(320.dp, 100.dp)

@Composable
fun AdFitBannerAd(
    adUnitId: String,
    modifier: Modifier = Modifier,
    adSize: DpSize = AdFitBanner320x100Size,
) {
    if (adUnitId.isBlank()) return

    var isAdFailed by remember(adUnitId) { mutableStateOf(false) }
    if (isAdFailed) return

    val lifecycleOwner = LocalLifecycleOwner.current
    val adViewHolder = remember { arrayOfNulls<BannerAdView>(1) }

    Box(
        modifier = modifier.size(adSize),
        contentAlignment = Alignment.Center
    ) {
        AdFitBannerPlaceholder(modifier = Modifier.fillMaxSize())

        AndroidView(
            factory = { context ->
                BannerAdView(context).apply {
                    setAdUnitId(adUnitId)
                    setAdListener(object : AdListener {
                        override fun onAdLoaded() = Unit

                        override fun onAdFailed(errorCode: Int) {
                            isAdFailed = true
                        }

                        override fun onAdClicked() = Unit
                    })
                    adViewHolder[0] = this
                    loadAd()
                }
            }
        )
    }

    DisposableEffect(lifecycleOwner, adUnitId) {
        val observer = LifecycleEventObserver { _, event ->
            val adView = adViewHolder[0] ?: return@LifecycleEventObserver
            when (event) {
                Lifecycle.Event.ON_RESUME -> adView.resume()
                Lifecycle.Event.ON_PAUSE -> adView.pause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            adViewHolder[0]?.destroy()
            adViewHolder[0] = null
        }
    }
}

@Composable
private fun AdFitBannerPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PickeTheme.colors.surfaceSubtle)
    )
}

@Preview(showBackground = true)
@Composable
private fun AdFitBannerPlaceholderPreview() {
    PickeTheme {
        AdFitBannerPlaceholder(modifier = Modifier.size(AdFitBanner320x100Size))
    }
}
