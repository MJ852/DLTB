package com.example.dltb

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Ticketing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticketing)

        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val timeTextView = findViewById<TextView>(R.id.timeTextView)


        val dateAndTime = DateAndTime(dateTextView, timeTextView)
        dateAndTime.start()


        val menuButton = findViewById<Button>(R.id.menu_btn)
        menuButton.setOnClickListener {
            showDialogBoxTicketingMenu()
        }


        val passengerTypeButton = findViewById<Button>(R.id.passenger_type_btn)
        passengerTypeButton.setOnClickListener{
            showDialogBoxPassengerType()
        }

        val baggageButton = findViewById<Button>(R.id.baggage_btn)
        baggageButton.setOnClickListener{
            showDialogBoxBaggage()
        }

        val cardButton = findViewById<Button>(R.id.card_btn)
        cardButton.setOnClickListener{
            showDialogTypeOfCard()
        }

    }

//    private fun showPassengerTypeDialogBox() {
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.dialog_box_passenger_type)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.GRAY))
//    }

    private fun showDialogBoxTicketingMenu(){

        val ticketingMenu = DialogBox_TicketingMenu.newInstance()
        ticketingMenu.show(supportFragmentManager, "popup")
    }

    private fun showDialogBoxPassengerType() {
        val dialogBoxPassengerType = DialogBoxPassengerType()
        dialogBoxPassengerType.show(supportFragmentManager, "popup")
    }

    private fun showDialogBoxBaggage() {
        val dialogBoxBaggage = DialogBoxBaggage()
        dialogBoxBaggage.show(supportFragmentManager, "popup")
    }

    private fun showDialogTypeOfCard() {
        val dialogBoxCard = DialogBoxTypeOfCard()
        dialogBoxCard.show(supportFragmentManager, "popup")
    }
}


