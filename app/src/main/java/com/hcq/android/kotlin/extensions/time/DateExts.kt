package com.hcq.android.kotlin.extensions.time

import android.text.format.DateUtils

fun nowInSeconds(): Long = System.currentTimeMillis() / 1000

inline val Number.hourInSeconds: Long get() = DateUtils.HOUR_IN_MILLIS / DateUtils.SECOND_IN_MILLIS

inline val Number.hourInMinutes: Long get() = DateUtils.HOUR_IN_MILLIS / DateUtils.MINUTE_IN_MILLIS

inline val Number.minuteInSeconds: Long get() = DateUtils.MINUTE_IN_MILLIS / DateUtils.SECOND_IN_MILLIS

inline val Number.dayInSeconds: Long get() = DateUtils.DAY_IN_MILLIS / DateUtils.SECOND_IN_MILLIS

inline val Number.secondsToHours: Long get() = this.toLong() / 1.hourInSeconds

inline val Number.secondsToMinutes: Long get() = this.toLong() / 1.minuteInSeconds