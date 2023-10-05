package com.example.dltb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TicketStops : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticket_stops)

        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)


        val dateAndTime = DateAndTime(dateTextView, timeTextView)
        dateAndTime.start()

        val backButton = findViewById<Button>(R.id.back_btn)
        backButton.setOnClickListener {
            val intent = Intent(this, TicketingMenu::class.java)
            startActivity(intent)
        }
    }
}