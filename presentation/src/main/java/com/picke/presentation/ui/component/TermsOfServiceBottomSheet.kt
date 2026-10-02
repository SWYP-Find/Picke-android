package com.picke.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.picke.presentation.R
import com.picke.presentation.ui.my.setting.policy.PolicyWebViewScreen
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.PolicyUrls

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceBottomSheet(
    onConfirm: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )
    var isServiceTermsAgreed by remember { mutableStateOf(false) }
    var isPrivacyPolicyAgreed by remember { mutableStateOf(false) }
    var openedPolicy by remember { mutableStateOf<Pair<Int, String>?>(null) }

    val isAllAgreed = isServiceTermsAgreed && isPrivacyPolicyAgreed

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        containerColor = PickeTheme.colors.surfaceDefault,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .background(PickeTheme.colors.neutral100, RoundedCornerShape(2.dp))
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 20.dp)
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Box(
                modifier = Modifier
                    .border(1.dp, PickeTheme.colors.neutral50, RoundedCornerShape(50))
                    .background(
                        color = PickeTheme.colors.backgroundBrand,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_picke),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .size(width = 58.dp, height = 58.dp),
                    colorFilter = ColorFilter.tint(PickeTheme.colors.primaryPressed)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.terms_sheet_title),
                style = PickeTheme.typography.headingMd,
                color = PickeTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.terms_sheet_description),
                style = PickeTheme.typography.bodySmRegular,
                color = PickeTheme.colors.textMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Terms Items
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TermsItem(
                    text = stringResource(R.string.terms_sheet_required_terms),
                    isAgreed = isServiceTermsAgreed,
                    onToggle = { isServiceTermsAgreed = !isServiceTermsAgreed },
                    onViewDetail = { openedPolicy = R.string.policy_title_terms to PolicyUrls.TERMS_OF_SERVICE }
                )
                TermsItem(
                    text = stringResource(R.string.terms_sheet_required_privacy),
                    isAgreed = isPrivacyPolicyAgreed,
                    onToggle = { isPrivacyPolicyAgreed = !isPrivacyPolicyAgreed },
                    onViewDetail = { openedPolicy = R.string.policy_title_privacy to PolicyUrls.PRIVACY_POLICY }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Confirm Button
            CustomButton(
                text = stringResource(R.string.terms_sheet_agree),
                onClick = { if (isAllAgreed) onConfirm() },
                backgroundColor = if (isAllAgreed) PickeTheme.colors.buttonPrimaryBackground else PickeTheme.colors.buttonPrimaryBackgroundDisabled,
                textColor = PickeTheme.colors.textInverse,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    openedPolicy?.let { (titleRes, url) ->
        Dialog(
            onDismissRequest = { openedPolicy = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            PolicyWebViewScreen(
                titleRes = titleRes,
                url = url,
                onBackClick = { openedPolicy = null }
            )
        }
    }
}

@Composable
private fun TermsItem(
    text: String,
    isAgreed: Boolean,
    onToggle: () -> Unit,
    onViewDetail: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.surfaceSubtle, RoundedCornerShape(8.dp))
            .border(1.dp, PickeTheme.colors.neutral100, RoundedCornerShape(8.dp))
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 커스텀 체크박스
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isAgreed) PickeTheme.colors.buttonPrimaryBackground else PickeTheme.colors.buttonPrimaryBackgroundDisabled)
                .border(
                    1.dp,
                    if (isAgreed) PickeTheme.colors.borderDefault else PickeTheme.colors.borderDisabled,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isAgreed) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = PickeTheme.colors.textInverse,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = PickeTheme.typography.bodySmRegular,
            color = PickeTheme.colors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(R.drawable.ic_arrow_right_a),
            contentDescription = stringResource(R.string.terms_sheet_view_detail),
            tint = PickeTheme.colors.textPrimary,
            modifier = Modifier
                .size(20.dp)
                .clickable { onViewDetail() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TermsOfServiceBottomSheetPreview() {
    PickeTheme {
        TermsOfServiceBottomSheet(
            onConfirm = {}
        )
    }
}
