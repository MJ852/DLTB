package com.example.dltb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TicketingMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticketing_menu)


        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)


        val dateAndTime = DateAndTime(dateTextView, timeTextView)
        dateAndTime.start()

        val ticketingButton = findViewById<Button>(R.id.ticketing_btn)
        ticketingButton.setOnClickListener {
            val intent = Intent(this, Ticketing::class.java)
            startActivity(intent)
        }

        val ticketListingButton = findViewById<Button>(R.id.ticketing_listing_btn)
        ticketListingButton.setOnClickListener {
            val intent = Intent(this, TicketListing::class.java)
            startActivity(intent)
        }
        val stopsButton = findViewById<Button>(R.id.stops_btn)
        stopsButton.setOnClickListener {
            val intent = Intent(this, TicketStops::class.java)
            startActivity(intent)
        }

        val expensesButton = findViewById<Button>(R.id.expenses_btn)
       expensesButton.setOnClickListener {
            val intent = Intent(this, Expense::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<Button>(R.id.back_btn)
        backButton.setOnClickListener {
            val intent = Intent(this, DispatcherPage::class.java)
            startActivity(intent)
        }


    }

    companion object {
        fun onBackPressed(): DialogBox_TicketingMenu {
            return DialogBox_TicketingMenu()
        }
    }
}