package com.example.dltb

import android.os.Handler
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateAndTime(private val dateTextView: TextView, private val timeTextView: TextView) {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val handler = Handler()
    private val updateIntervalMs = 1000L

    private val updateTask = object : Runnable {
        override fun run() {
            val currentTime = Calendar.getInstance().time
            val formattedDate = dateFormat.format(currentTime)
            val formattedTime = timeFormat.format(currentTime)

            dateTextView.text = formattedDate
            timeTextView.text = formattedTime

            handler.postDelayed(this, updateIntervalMs)
        }
    }

    fun start() {
        handler.post(updateTask)
    }

    fun stop() {
        handler.removeCallbacks(updateTask)
    }
}
