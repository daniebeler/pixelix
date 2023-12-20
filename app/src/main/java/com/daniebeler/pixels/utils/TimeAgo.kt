package com.daniebeler.pixels.utils

import android.net.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit

class TimeAgo {

    fun covertTimeToText(dataDate: String?): String? {
        var convTime: String? = null
        val suffix = "ago"
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val pasTime: Date = dateFormat.parse(dataDate)
            val nowTime: Date = dateFormat.parse(Instant.now().toString())
            val dateDiff: Long = nowTime.time - pasTime.time

            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
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
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return convTime
    }
}