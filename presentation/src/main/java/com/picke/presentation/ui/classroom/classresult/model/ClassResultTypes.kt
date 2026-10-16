package com.picke.presentation.ui.classroom.classresult.model

import androidx.annotation.StringRes
import com.picke.presentation.R

enum class ClassResultTab(@param:StringRes val labelRes: Int) {
    SUMMARY(R.string.class_result_summary_tab),
    REPORT(R.string.class_result_report_tab)
}