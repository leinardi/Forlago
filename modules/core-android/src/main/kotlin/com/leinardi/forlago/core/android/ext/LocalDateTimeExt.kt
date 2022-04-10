package com.leinardi.forlago.core.android.ext

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDateTime.toLocalizedDate(dateStyle: FormatStyle = FormatStyle.LONG): String = format(DateTimeFormatter.ofLocalizedDate(dateStyle))
