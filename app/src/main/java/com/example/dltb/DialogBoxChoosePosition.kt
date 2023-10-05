package com.example.dltb

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class DialogBoxChoosePosition : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dialogView = inflater.inflate(R.layout.dialog_box_choose_position, container, false)

        val doneButton = dialogView.findViewById<Button>(R.id.done_btn)
        doneButton.setOnClickListener {
            val intent = Intent(requireActivity(), InspectionMenu::class.java)
            startActivity(intent)
            dismiss()
        }

        return dialogView
    }

    companion object {
        fun newInstance(): DialogBoxChoosePosition {
            return DialogBoxChoosePosition()
        }
    }
}
