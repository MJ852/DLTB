package com.example.dltb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ArrivalMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.arrival_menu_print)

        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)


        val dateAndTime = DateAndTime(dateTextView, timeTextView)
        dateAndTime.start()

        val backButton = findViewById<Button>(R.id.back_btn)
        backButton.setOnClickListener {
            val intent = Intent(this, Expense::class.java)
            startActivity(intent)
        }

    }

}