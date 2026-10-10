package com.picke.presentation.ui.classroom.myclass.model

import androidx.annotation.StringRes
import com.picke.presentation.R

enum class MyClassFilter(@param:StringRes val labelRes: Int) {
    ALL(R.string.my_class_filter_all),
    IN_PROGRESS(R.string.my_class_status_in_progress),
    ENDED(R.string.my_class_status_ended)
}