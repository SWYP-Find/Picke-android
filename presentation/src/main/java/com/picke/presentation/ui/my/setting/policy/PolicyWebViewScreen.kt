package com.picke.presentation.ui.my.setting.policy

import android.net.Uri
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun PolicyWebViewScreen(
    @StringRes titleRes: Int,
    url: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = stringResource(titleRes),
                    centerTitle = true,
                    showLogo = false,
                    onBackClick = onBackClick,
                    backgroundColor = PickeTheme.colors.backgroundBeige
                )
            }
        },
        containerColor = PickeTheme.colors.backgroundBeige
    ) { innerPadding ->
        PolicyWebViewContent(
            url = url,
            modifier = modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        )
    }
}

@Composable
private fun PolicyWebViewContent(
    url: String,
    modifier: Modifier = Modifier,
) {
    var isLoading by remember(url) { mutableStateOf(true) }
    var isError by remember(url) { mutableStateOf(false) }

    Box(
        modifier = modifier.background(PickeTheme.colors.surfaceBeigeDefault),
        contentAlignment = Alignment.Center
    ) {
        if (!LocalInspectionMode.current) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = false
                        settings.allowFileAccess = false
                        settings.allowContentAccess = false
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView,
                                request: WebResourceRequest
                            ): Boolean {
                                val target = request.url
                                return target.scheme != "https" || target.host != Uri.parse(url).host
                            }

                            override fun onPageFinished(view: WebView, url: String) {
                                isLoading = false
                            }

                            override fun onReceivedError(
                                view: WebView,
                                request: WebResourceRequest,
                                error: WebResourceError
                            ) {
                                if (request.isForMainFrame) {
                                    isError = true
                                    isLoading = false
                                }
                            }
                        }
                        loadUrl(url)
                    }
                },
                onRelease = { webView -> webView.destroy() }
            )
        }

        if (isError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PickeTheme.colors.surfaceBeigeDefault),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.policy_load_error),
                    style = PickeTheme.typography.bodySmRegular,
                    color = PickeTheme.colors.textMuted,
                    textAlign = TextAlign.Center
                )
            }
        } else if (isLoading) {
            CircularProgressIndicator(
                color = PickeTheme.colors.primary900,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PolicyWebViewScreenPreview() {
    PickeTheme {
        PolicyWebViewScreen(
            titleRes = R.string.policy_title_privacy,
            url = BuildConfig.PRIVACY_POLICY_URL,
            onBackClick = {}
        )
    }
}