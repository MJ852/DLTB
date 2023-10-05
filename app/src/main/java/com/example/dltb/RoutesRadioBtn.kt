package com.example.dltb

import android.content.Context
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup

class RoutesRadioBtn (private val context: Context) {
    private val radioGroup = RadioGroup(context)

    fun setOptions(options: List<String>) {
        for (option in options) {
            val radioButton = RadioButton(context)
            radioButton.text = option
            radioButton.layoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
            )
            radioButton.gravity = Gravity.END // Align to the right

            // Add the radio button to the radio group
            radioGroup.addView(radioButton)
        }
    }

    fun getView(): RadioGroup {
        return radioGroup
    }
}