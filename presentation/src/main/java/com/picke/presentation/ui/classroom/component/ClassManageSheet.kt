package com.picke.presentation.ui.classroom.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassManageSheetDimens {
    val titleLetterSpacing = (-0.6).sp
    val iconContainerSize = 40.dp
}

@Composable
fun ClassManageSheet(
    isOperator: Boolean,
    onEditClick: () -> Unit,
    onShareCodeClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onLeaveClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ClassBottomSheet(onDismiss = onDismiss, modifier = modifier) {
        ClassManageSheetContent(
            isOperator = isOperator,
            onEditClick = onEditClick,
            onShareCodeClick = onShareCodeClick,
            onDeleteClick = onDeleteClick,
            onLeaveClick = onLeaveClick
        )
    }
}

@Composable
private fun ClassManageSheetContent(
    isOperator: Boolean,
    onEditClick: () -> Unit,
    onShareCodeClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onLeaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.class_manage_title),
            modifier = Modifier.padding(bottom = SpacingTokens.s20),
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.headingXl.copy(letterSpacing = ClassManageSheetDimens.titleLetterSpacing)
        )

        if (isOperator) {
            ClassManageItem(
                iconResId = R.drawable.ic_edit,
                title = stringResource(R.string.class_manage_edit),
                description = stringResource(R.string.class_manage_edit_description),
                onClick = onEditClick
            )
            ClassManageDivider()
        }

        ClassManageItem(
            iconResId = R.drawable.ic_arrow_up_right,
            title = stringResource(R.string.class_manage_share),
            description = stringResource(R.string.class_manage_share_description),
            onClick = onShareCodeClick
        )
        ClassManageDivider()

        if (isOperator) {
            ClassManageItem(
                iconResId = R.drawable.ic_trash,
                title = stringResource(R.string.class_manage_delete),
                description = stringResource(R.string.class_manage_delete_description),
                onClick = onDeleteClick,
                iconSize = ComponentNumberTokens.iconMd
            )
        } else {
            ClassManageItem(
                iconResId = R.drawable.ic_log_out,
                title = stringResource(R.string.class_manage_leave),
                description = stringResource(R.string.class_manage_leave_description),
                onClick = onLeaveClick
            )
        }
    }
}

@Composable
private fun ClassManageDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = ComponentNumberTokens.borderWidthRegular,
        color = PickeTheme.colors.cardBaseBorderDefault
    )
}

@Composable
private fun ClassManageItem(
    @DrawableRes iconResId: Int,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = ComponentNumberTokens.iconLg
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = SpacingTokens.s12),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(ClassManageSheetDimens.iconContainerSize)
                .background(PickeTheme.colors.surfacePrimarySubtle, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = PickeTheme.colors.iconPrimaryDefault
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s2)
        ) {
            Text(
                text = title,
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.bodyMdSemiBold
            )
            Text(
                text = description,
                color = PickeTheme.colors.cardBaseTextDecription,
                style = PickeTheme.typography.bodyXsRegular
            )
        }
        
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = null,
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = PickeTheme.colors.iconGrayDefault
        )
    }
}

@Preview(name = "운영자", showBackground = true)
@Composable
private fun ClassManageSheetOperatorPreview() {
    PickeTheme {
        ClassManageSheetContent(
            isOperator = true,
            onEditClick = {},
            onShareCodeClick = {},
            onDeleteClick = {},
            onLeaveClick = {},
            modifier = Modifier
                .background(PickeTheme.colors.surfaceBeigeDefault)
                .padding(SpacingTokens.s16)
        )
    }
}

@Preview(name = "참여자", showBackground = true)
@Composable
private fun ClassManageSheetMemberPreview() {
    PickeTheme {
        ClassManageSheetContent(
            isOperator = false,
            onEditClick = {},
            onShareCodeClick = {},
            onDeleteClick = {},
            onLeaveClick = {},
            modifier = Modifier
                .background(PickeTheme.colors.surfaceBeigeDefault)
                .padding(SpacingTokens.s16)
        )
    }
}