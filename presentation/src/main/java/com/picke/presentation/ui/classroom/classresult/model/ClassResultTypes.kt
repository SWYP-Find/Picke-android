package com.picke.presentation.ui.classroom.classresult.model

import androidx.annotation.StringRes
import com.picke.presentation.R

enum class ClassResultTab(@param:StringRes val labelRes: Int) {
    SUMMARY(R.string.class_result_summary_tab),
    REPORT(R.string.class_result_report_tab)
}

enum class ClassReportState {
    BEFORE_CLOSE,
    PENDING,
    GENERATING,
    SUCCEEDED,
    LIMITED,
    FAILED;

    companion object {
        fun from(value: String): ClassReportState = entries.find { it.name == value } ?: PENDING
    }
}