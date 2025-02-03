package com.daniebeler.pfpixelix.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

object TimeAgo {

    fun convertTimeToText(dataDate: String): String {
        var convTime: String = ""
        val suffix = "ago"
        try {
            val pasTime: Instant = Instant.parse(dataDate)
            val nowTime: Instant = Clock.System.now()
            val dateDiff = nowTime - pasTime

            val second: Long = dateDiff.inWholeSeconds
            val minute: Long = dateDiff.inWholeMinutes
            val hour: Long = dateDiff.inWholeHours
            val day: Long = dateDiff.inWholeDays
            if (second < 60) {
                convTime = "$second seconds $suffix"
            } else if (minute < 60) {
                convTime = "$minute minutes $suffix"
            } else if (hour < 24) {
                convTime = "$hour hours $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " months " + suffix
                } else {
                    (day / 7).toString() + " week " + suffix
                }
            } else if (day < 7) {
                convTime = "$day days $suffix"
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return convTime
    }
}