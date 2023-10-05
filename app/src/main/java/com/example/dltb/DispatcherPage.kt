package com.example.dltb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DispatcherPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dispatcher_page)

        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)


        val dateAndTime = DateAndTime(dateTextView, timeTextView)
        dateAndTime.start()


        val allTicketsButton = findViewById<Button>(R.id.all_tickets_btn)
        allTicketsButton.setOnClickListener {
            val intent = Intent(this, TicketingMenu::class.java)
            startActivity(intent)
        }

        val inspectionButton = findViewById<Button>(R.id.inspection_btn)
        inspectionButton.setOnClickListener {
            showDialogPosition()
        }

        val currentTripBaggageButton = findViewById<Button>(R.id.current_trip_baggage_btn)
        currentTripBaggageButton.setOnClickListener {
            val intent = Intent(this, InspectionMenu::class.java)
            startActivity(intent)
        }
    }

    private fun showDialogPosition(){
        val dialogBox = DialogBoxChoosePosition.newInstance()
        dialogBox.show(supportFragmentManager, "popup")
    }
}