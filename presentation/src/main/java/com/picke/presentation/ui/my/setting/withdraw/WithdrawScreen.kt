package com.picke.presentation.ui.my.setting.withdraw

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.picke.presentation.ui.component.CustomConfirmDialog
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
    val context = LocalContext.current
    val reasons = listOf(
        "자주 이용하지 않아요",
        "보고 싶은 배틀 주제가 없어요",
        "배틀 방식이 제게 잘 맞지 않아요",
        "서비스 이용이 불편해요",
        "이용할 시간이 없어요",
        "기타"
    )

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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = PickeTheme.colors.backgroundBeige,
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    // 1. 제출하기 버튼
                    Button(
                        onClick = {
                            if (selectedReason != null) {
                                showWithdrawDialog = true
                            } else {
                                Toast.makeText(context, "탈퇴 사유를 선택해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = PickeTheme.colors.backgroundBeige)
                    ) {
                        Text(
                            text = "제출하기",
                            color = PickeTheme.colors.primary800,
                            style = PickeTheme.typography.headingSm
                        )
                    }
                    // 2. 돌아가기 버튼
                    Button(
                        onClick = onBackClick,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = PickeTheme.colors.primary500)
                    ) {
                        Text(
                            text = "픽케로 다시 돌아가기",
                            color = Color.White,
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

            // 타이틀
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "정말 떠나시나요? 아쉬워요 😢",
                style = PickeTheme.typography.headingMd,
                color = PickeTheme.colors.primary800
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 서브 타이틀
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "지금까지 픽케를 이용해주셔서 감사합니다.\n더 나은 서비스를 만들기 위해, 탈퇴 이유를 알려주세요.",
                color = PickeTheme.colors.gray400,
                style = PickeTheme.typography.bodySmMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 탈퇴 사유 리스트
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

    if (showWithdrawDialog) {
        CustomConfirmDialog(
            message = "탈퇴 시 지금까지의 이용기록이 영구 삭제 됩니다.\n그럼에도 탈퇴하시겠습니까?",
            confirmText = "네, 탈퇴합니다",
            dismissText = "뒤로가기",
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
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
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