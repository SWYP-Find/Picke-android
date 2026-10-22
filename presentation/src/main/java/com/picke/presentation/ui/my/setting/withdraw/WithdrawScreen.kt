package com.picke.presentation.ui.my.setting.withdraw

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.component.dialog.CustomConfirmDialog
import com.picke.presentation.ui.my.setting.SettingViewModel
import com.picke.presentation.ui.my.setting.model.SettingUiEvent
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun WithdrawScreen(
    onBackClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val reasons = listOf(
        stringResource(R.string.setting_withdraw_reason_rarely_use),
        stringResource(R.string.setting_withdraw_reason_no_topic),
        stringResource(R.string.setting_withdraw_reason_battle_style),
        stringResource(R.string.setting_withdraw_reason_inconvenient),
        stringResource(R.string.setting_withdraw_reason_no_time),
        stringResource(R.string.setting_withdraw_reason_etc)
    )
    val reasonRequiredMessage = stringResource(R.string.setting_withdraw_reason_required)

    var selectedReason by remember { mutableStateOf<String?>(null) }
    var showWithdrawDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                SettingUiEvent.NavigateToLogin -> onNavigateToLogin()
                is SettingUiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        context.getString(event.messageResId),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = PickeTheme.colors.backgroundBeige,
            bottomBar = {
                Box(modifier = Modifier.navigationBarsPadding()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    ) {
                        Button(
                            onClick = {
                                if (selectedReason != null) {
                                    showWithdrawDialog = true
                                } else {
                                    Toast.makeText(
                                        context,
                                        reasonRequiredMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = PickeTheme.colors.backgroundBeige)
                        ) {
                            Text(
                                text = stringResource(R.string.setting_withdraw_submit),
                                color = PickeTheme.colors.primary800,
                                style = PickeTheme.typography.headingSm
                            )
                        }
                        Button(
                            onClick = onBackClick,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = PickeTheme.colors.primary500)
                        ) {
                            Text(
                                text = stringResource(R.string.setting_withdraw_go_back),
                                color = PickeTheme.colors.buttonPrimaryTextDefault,
                                style = PickeTheme.typography.headingSm
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = stringResource(R.string.setting_withdraw_title),
                    style = PickeTheme.typography.headingMd,
                    color = PickeTheme.colors.primary800
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = stringResource(R.string.setting_withdraw_description),
                    color = PickeTheme.colors.gray400,
                    style = PickeTheme.typography.bodySmMedium
                )

                Spacer(modifier = Modifier.height(32.dp))

                LazyColumn {
                    items(reasons) { reason ->
                        WithdrawReasonItem(
                            text = reason,
                            isSelected = selectedReason == reason,
                            onClick = { selectedReason = reason }
                        )
                    }
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PickeTheme.colors.primary900)
            }
        }
    }

    if (showWithdrawDialog) {
        CustomConfirmDialog(
            message = stringResource(R.string.setting_withdraw_confirm_message),
            confirmText = stringResource(R.string.setting_withdraw_confirm),
            onConfirm = {
                showWithdrawDialog = false
                selectedReason?.let { viewModel.withdraw(it) }
            },
            onDismiss = {
                showWithdrawDialog = false
            }
        )
    }
}

@Composable
fun WithdrawReasonItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 커스텀 라디오 버튼
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = if (isSelected) PickeTheme.colors.primary500 else PickeTheme.colors.gray200,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // 선택되었을 때 안에 채워지는 작은 원
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(PickeTheme.colors.primary500)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.bodySmRegular
        )
    }
}