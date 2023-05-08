package com.hcq.android.kotlin.extensions.time

import android.text.format.DateUtils
import java.util.concurrent.TimeUnit

inline val nowInSeconds: Long get() = System.currentTimeMillis() / DateUtils.SECOND_IN_MILLIS

inline val Number.hoursInSeconds: Long get() = TimeUnit.HOURS.toSeconds(toLong())

inline val Number.hoursInMinutes: Long get() = TimeUnit.HOURS.toMinutes(toLong())

inline val Number.minutesInSeconds: Long get() = TimeUnit.MINUTES.toSeconds(toLong())

inline val Number.daysInSeconds: Long get() = TimeUnit.DAYS.toSeconds(toLong())

inline val Number.secondsToHours: Long get() = TimeUnit.SECONDS.toHours(toLong())

inline val Number.secondsToMinutes: Long get() = TimeUnit.SECONDS.toMinutes(toLong())