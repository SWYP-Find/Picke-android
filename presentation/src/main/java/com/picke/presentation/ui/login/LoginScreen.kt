package com.picke.presentation.ui.login

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.TermsOfServiceBottomSheet
import com.picke.presentation.ui.login.model.LoginUiState
import com.picke.presentation.ui.login.model.Provider
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun LoginScreen(
    onNavigateToMain: (isNewUser: Boolean) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showTermsSheet by rememberSaveable { mutableStateOf(false) }
    var pendingIsNewUser by rememberSaveable { mutableStateOf(false) }

    BackHandler {
        (context as? Activity)?.finish()
    }

    val kakaoLoginFailedMessage = stringResource(R.string.login_kakao_failed)
    val googleLoginFailedMessage = stringResource(R.string.login_google_failed)

    val launchGoogleLogin = rememberGoogleLoginLauncher(
        onAuthCodeReceive = { authCode ->
            viewModel.handleSocialLoginSuccess(Provider.GOOGLE, authCode)
        },
        onFailure = {
            Toast.makeText(context, googleLoginFailedMessage, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        },
        onCancel = { viewModel.resetState() }
    )

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                if (state.needsTermsAgreement) {
                    pendingIsNewUser = state.isNewUser
                    showTermsSheet = true
                } else {
                    onNavigateToMain(state.isNewUser)
                }
            }

            is LoginUiState.Error -> {
                Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }

            else -> {}
        }
    }

    if (showTermsSheet) {
        TermsOfServiceBottomSheet(
            onConfirm = {
                viewModel.markTermsAgreed()
                showTermsSheet = false
                onNavigateToMain(pendingIsNewUser)
            }
        )
    }

    LoginScreenContent(
        isLoading = uiState is LoginUiState.Loading,
        onKakaoClick = {
            viewModel.onKakaoLoginStart()
            loginWithKakao(
                context = context,
                onAuthCodeReceive = { authCode ->
                    viewModel.handleSocialLoginSuccess(Provider.KAKAO, authCode)
                },
                onFailure = {
                    Toast.makeText(context, kakaoLoginFailedMessage, Toast.LENGTH_SHORT).show()
                    viewModel.resetState()
                },
                onCancel = { viewModel.resetState() }
            )
        },
        onGoogleClick = {
            if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isEmpty()) {
                Toast.makeText(context, "로그인 설정 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.onGoogleLoginStart()
                launchGoogleLogin()
            }
        },
    )
}

@Composable
private fun LoginScreenContent(
    isLoading: Boolean,
    onKakaoClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundSubtle),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = PickeTheme.typography.headingSm,
                color = PickeTheme.colors.gray200,
                text = stringResource(R.string.login_your_think)
            )
            Image(
                painter = painterResource(id = R.drawable.logo_picke),
                contentDescription = "Picke Logo",
                modifier = Modifier.size(width = 240.dp, height = 100.dp)
            )
            Spacer(modifier = Modifier.height(72.dp))
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = PickeTheme.colors.primary900)
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                CustomButton(
                    text = stringResource(R.string.login_with_kakao),
                    onClick = onKakaoClick,
                    backgroundColor = Color(0xFFFEE500),
                    textColor = PickeTheme.colors.textDefault,
                    iconResId = R.drawable.logo_login_kakao
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomButton(
                    text = stringResource(R.string.login_with_google),
                    onClick = onGoogleClick,
                    backgroundColor = Color.White,
                    textColor = PickeTheme.colors.textDefault,
                    iconResId = R.drawable.logo_login_google
                )
            }
        }
    }
}

@Preview(showSystemUi = true, name = "Login - 기본")
@Composable
private fun LoginScreenPreview() {
    PickeTheme {
        LoginScreenContent(isLoading = false)
    }
}

@Preview(showSystemUi = true, name = "Login - 로딩중")
@Composable
private fun LoginScreenLoadingPreview() {
    PickeTheme {
        LoginScreenContent(isLoading = true)
    }
}