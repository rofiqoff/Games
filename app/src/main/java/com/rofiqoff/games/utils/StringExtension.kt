package com.rofiqoff.games.utils

import com.rofiqoff.games.utils.Constants.DEFAULT_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Locale

infix fun String.convertAs(formatResult: String): String {
    return kotlin.runCatching {
        SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault()).parse(this)?.let { date ->
            SimpleDateFormat(formatResult, Locale.getDefault()).format(date)
        }.orEmpty()
    }.getOrDefault("")
}
