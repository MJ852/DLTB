package com.example.dltb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Expense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense)


        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)


        val dateAndTime = DateAndTime(dateTextView, timeTextView)
        dateAndTime.start()

        val printButton = findViewById<Button>(R.id.print_btn)
        printButton.setOnClickListener {
            val intent = Intent(this, ArrivalMenu::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<Button>(R.id.back_btn)
        backButton.setOnClickListener {
            val intent = Intent(this, TicketingMenu::class.java)
            startActivity(intent)
        }

        val arrivalButton = findViewById<Button>(R.id.select_btn)
        arrivalButton.setOnClickListener {
            val intent = Intent(this, Arrival::class.java)
            startActivity(intent)
        }
    }
}