package com.example.dltb

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class DialogBox_TicketingMenu : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_box_ticketing_menu, null)
        builder.setView(dialogView)

        val noButton = dialogView.findViewById<Button>(R.id.no_btn)
        noButton.setOnClickListener {
            dismiss()
        }

        val yesButton = dialogView.findViewById<Button>(R.id.yes_btn)
        yesButton.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        return builder.create()
    }

    companion object {
        fun newInstance(): DialogBox_TicketingMenu {
            return DialogBox_TicketingMenu()
        }
    }
}
