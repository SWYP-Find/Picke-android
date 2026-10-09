package com.picke.presentation.ui.classroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.component.ClassShortcutCard
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassScreenDimens {
    val introTitleLetterSpacing = (-0.6).sp
    val introDescriptionLetterSpacing = (-0.4).sp
    val joinCardImageSize = 169.dp
    val joinCardImageEndOverflow = 69.dp
    val joinCardImageBottomOverflow = 54.dp
    val myClassCardImageSize = 130.dp
    val myClassCardImageEndOverflow = 6.dp
    val myClassCardImageBottomOverflow = 49.dp
    val ticketVerticalPadding = 10.dp
}

@Composable
fun ClassScreen(
    onBackClick: () -> Unit,
    onNavigateToJoin: () -> Unit,
    onNavigateToMyClass: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToTicket: () -> Unit,
    modifier: Modifier = Modifier
) {
    ClassContent(
        onBackClick = onBackClick,
        onJoinClick = onNavigateToJoin,
        onMyClassClick = onNavigateToMyClass,
        onCreateClick = onNavigateToCreate,
        onTicketClick = onNavigateToTicket,
        modifier = modifier
    )
}

@Composable
private fun ClassContent(
    onBackClick: () -> Unit,
    onJoinClick: () -> Unit,
    onMyClassClick: () -> Unit,
    onCreateClick: () -> Unit,
    onTicketClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundBeige)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_class_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            CustomTopAppBar(
                title = stringResource(R.string.class_title),
                onBackClick = onBackClick,
                showBackButton = true,
                backgroundColor = Color.Transparent,
                backIconColor = PickeTheme.colors.textInverse,
                titleColor = PickeTheme.colors.textInverse
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(
                        start = SpacingTokens.s16,
                        top = SpacingTokens.s24,
                        end = SpacingTokens.s16,
                        bottom = SpacingTokens.s16
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ClassIntroSection()

                ClassMenuSection(
                    onJoinClick = onJoinClick,
                    onMyClassClick = onMyClassClick,
                    onCreateClick = onCreateClick,
                    onTicketClick = onTicketClick
                )
            }
        }
    }
}

@Composable
private fun ClassIntroSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.class_intro_title),
            color = PickeTheme.colors.textInverse,
            textAlign = TextAlign.Center,
            style = PickeTheme.typography.headingXl.copy(letterSpacing = ClassScreenDimens.introTitleLetterSpacing)
        )
        Text(
            text = stringResource(R.string.class_intro_description),
            color = PickeTheme.colors.textMuted,
            textAlign = TextAlign.Center,
            style = PickeTheme.typography.headingSm.copy(letterSpacing = ClassScreenDimens.introDescriptionLetterSpacing)
        )
    }
}

@Composable
private fun ClassMenuSection(
    onJoinClick: () -> Unit,
    onMyClassClick: () -> Unit,
    onCreateClick: () -> Unit,
    onTicketClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6)) {
            ClassShortcutCard(
                title = stringResource(R.string.class_join_action),
                description = stringResource(R.string.class_join_code_description),
                imageResId = R.drawable.img_class_join_code,
                imageSize = ClassScreenDimens.joinCardImageSize,
                imageEndOverflow = ClassScreenDimens.joinCardImageEndOverflow,
                imageBottomOverflow = ClassScreenDimens.joinCardImageBottomOverflow,
                onClick = onJoinClick,
                modifier = Modifier.weight(1f)
            )
            ClassShortcutCard(
                title = stringResource(R.string.my_class_title),
                description = stringResource(R.string.class_my_class_description),
                imageResId = R.drawable.img_class_my_desk,
                imageSize = ClassScreenDimens.myClassCardImageSize,
                imageEndOverflow = ClassScreenDimens.myClassCardImageEndOverflow,
                imageBottomOverflow = ClassScreenDimens.myClassCardImageBottomOverflow,
                onClick = onMyClassClick,
                modifier = Modifier.weight(1f)
            )
        }

        Column {
            CustomButton(
                text = stringResource(R.string.class_create_title),
                onClick = onCreateClick,
                modifier = Modifier.height(ComponentNumberTokens.buttonPrimaryLargeHeight),
                backgroundColor = PickeTheme.colors.buttonPrimaryBackgroundDefault,
                textColor = PickeTheme.colors.buttonPrimaryTextDefault
            )
            Text(
                text = stringResource(R.string.class_ticket_register),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onTicketClick)
                    .padding(vertical = ClassScreenDimens.ticketVerticalPadding),
                color = PickeTheme.colors.textMuted,
                textAlign = TextAlign.Center,
                style = PickeTheme.typography.bodyXxsMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassContentPreview() {
    PickeTheme {
        ClassContent(
            onBackClick = {},
            onJoinClick = {},
            onMyClassClick = {},
            onCreateClick = {},
            onTicketClick = {}
        )
    }
}