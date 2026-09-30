package com.picke.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.picke.presentation.R

// 1. Pretendard 폰트 패밀리 세팅
val Pretendard = FontFamily(
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),   // 800
    Font(R.font.pretendard_bold, FontWeight.Bold),                      // 700
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),              // 600
    Font(R.font.pretendard_medium, FontWeight.Medium),                  // 500
    Font(R.font.pretendard_regular, FontWeight.Normal)                  // 400
)

data class SwypTypography(
    // display
    val displayLg: TextStyle,
    val displayMd: TextStyle,

    // heading
    val headingXl: TextStyle,
    val headingLg: TextStyle,
    val headingMd: TextStyle,
    val headingSm: TextStyle,
    val headingXs: TextStyle,

    // body/lg (16)
    val bodyLgRegular: TextStyle,
    val bodyLgMedium: TextStyle,
    val bodyLgSemiBold: TextStyle,
    val bodyLgBold: TextStyle,

    // body/md (15)
    val bodyMdMedium: TextStyle,
    val bodyMdSemiBold: TextStyle,
    val bodyMdBold: TextStyle,

    // body/sm (14)
    val bodySmRegular: TextStyle,
    val bodySmMedium: TextStyle,
    val bodySmSemiBold: TextStyle,

    // body/xs (13)
    val bodyXsRegular: TextStyle,
    val bodyXsMedium: TextStyle,
    val bodyXsSemiBold: TextStyle,

    // body/xxs (12)
    val bodyXxsRegular: TextStyle,
    val bodyXxsMedium: TextStyle,
    val bodyXxsSemiBold: TextStyle,

    // caption/lg (12)
    val captionLgRegular: TextStyle,
    val captionLgMedium: TextStyle,
    val captionLgSemiBold: TextStyle,
    val captionLgBold: TextStyle,

    // caption/md (11)
    val captionMdRegular: TextStyle,
    val captionMdMedium: TextStyle,
    val captionMdSemiBold: TextStyle,
    val captionMdBold: TextStyle,

    // caption/sm (10)
    val captionSmRegular: TextStyle,
    val captionSmMedium: TextStyle,
    val captionSmSemiBold: TextStyle,
    val captionSmBold: TextStyle,
)

private fun pretendard(
    fontSize: TextUnit,
    fontWeight: FontWeight,
    lineHeight: TextUnit,
) = TextStyle(
    fontFamily = Pretendard,
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = 0.sp,
)

private val LineHeightHeading = 1.3.em
private val LineHeightBodyLarge = 1.5.em
private val LineHeightBodySmall = 1.4.em

val swypTypography = SwypTypography(
    displayLg = pretendard(40.sp, FontWeight.ExtraBold, LineHeightHeading),
    displayMd = pretendard(30.sp, FontWeight.SemiBold, LineHeightHeading),

    headingXl = pretendard(24.sp, FontWeight.SemiBold, LineHeightHeading),
    headingLg = pretendard(20.sp, FontWeight.SemiBold, LineHeightHeading),
    headingMd = pretendard(18.sp, FontWeight.SemiBold, LineHeightHeading),
    headingSm = pretendard(16.sp, FontWeight.SemiBold, LineHeightHeading),
    headingXs = pretendard(14.sp, FontWeight.SemiBold, LineHeightHeading),

    bodyLgRegular = pretendard(16.sp, FontWeight.Normal, LineHeightBodyLarge),
    bodyLgMedium = pretendard(16.sp, FontWeight.Medium, LineHeightBodyLarge),
    bodyLgSemiBold = pretendard(16.sp, FontWeight.SemiBold, LineHeightBodyLarge),
    bodyLgBold = pretendard(16.sp, FontWeight.Bold, LineHeightBodyLarge),

    bodyMdMedium = pretendard(15.sp, FontWeight.Medium, LineHeightBodyLarge),
    bodyMdSemiBold = pretendard(15.sp, FontWeight.SemiBold, LineHeightBodyLarge),
    bodyMdBold = pretendard(15.sp, FontWeight.Bold, LineHeightBodyLarge),

    bodySmRegular = pretendard(14.sp, FontWeight.Normal, LineHeightBodySmall),
    bodySmMedium = pretendard(14.sp, FontWeight.Medium, LineHeightBodySmall),
    bodySmSemiBold = pretendard(14.sp, FontWeight.SemiBold, LineHeightBodySmall),

    bodyXsRegular = pretendard(13.sp, FontWeight.Normal, LineHeightBodySmall),
    bodyXsMedium = pretendard(13.sp, FontWeight.Medium, LineHeightBodySmall),
    bodyXsSemiBold = pretendard(13.sp, FontWeight.SemiBold, LineHeightBodySmall),

    bodyXxsRegular = pretendard(12.sp, FontWeight.Normal, LineHeightBodySmall),
    bodyXxsMedium = pretendard(12.sp, FontWeight.Medium, LineHeightBodySmall),
    bodyXxsSemiBold = pretendard(12.sp, FontWeight.SemiBold, LineHeightBodySmall),

    captionLgRegular = pretendard(12.sp, FontWeight.Normal, LineHeightBodySmall),
    captionLgMedium = pretendard(12.sp, FontWeight.Medium, LineHeightBodySmall),
    captionLgSemiBold = pretendard(12.sp, FontWeight.SemiBold, LineHeightBodySmall),
    captionLgBold = pretendard(12.sp, FontWeight.Bold, LineHeightBodySmall),

    captionMdRegular = pretendard(11.sp, FontWeight.Normal, LineHeightBodySmall),
    captionMdMedium = pretendard(11.sp, FontWeight.Medium, LineHeightBodySmall),
    captionMdSemiBold = pretendard(11.sp, FontWeight.SemiBold, LineHeightBodySmall),
    captionMdBold = pretendard(11.sp, FontWeight.Bold, LineHeightBodySmall),

    captionSmRegular = pretendard(10.sp, FontWeight.Normal, LineHeightBodySmall),
    captionSmMedium = pretendard(10.sp, FontWeight.Medium, LineHeightBodySmall),
    captionSmSemiBold = pretendard(10.sp, FontWeight.SemiBold, LineHeightBodySmall),
    captionSmBold = pretendard(10.sp, FontWeight.Bold, LineHeightBodySmall),
)

// 4. Compose 전역에서 쓸 수 있게 Local 객체 생성 (Theme.kt에서 사용)
val LocalSwypTypography = staticCompositionLocalOf { swypTypography }