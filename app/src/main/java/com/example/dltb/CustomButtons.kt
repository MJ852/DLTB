package com.example.dltb

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.widget.Button
import android.widget.Toast
import kotlin.coroutines.CoroutineContext


class CustomButtons {

    private var selectedButtonDDC: Button? = null
    private var selectedTripButton: Button? = null
    private var positionButton: Button? = null
    private val customBlueColor: Int = Color.parseColor("#00558D")

    fun handleButtonClick(button: Button): Int {
//        if (button == selectedButtonDDC) {
//            // Deselect the button and reset its colors to default
//            button.setBackgroundResource(R.drawable.gray_btn)
//            button.setTextColor(customBlueColor)
//            selectedButtonDDC = null
//            // Return the color resource for the default button background
//            return R.color.gray
//        } else {
//            // Deselect the previously selected button
//            selectedButtonDDC?.apply {
//                setBackgroundResource(R.drawable.gray_btn)
//                setTextColor(customBlueColor)
//            }

            // Select the new button
            button.setBackgroundResource(R.drawable.green_btn)
            button.setTextColor(Color.WHITE)
            selectedButtonDDC = button
            // Return the color resource for the selected button background
            return R.color.green
        }

        fun handleTripButtonClick(button: Button) {

            if (button == selectedTripButton) {
                // Deselect the currently selected button
                button.setBackgroundResource(R.drawable.gray_btn)
                button.setTextColor(customBlueColor)
                selectedTripButton = null


            } else {
                // Select the new button and deselect the previously selected one
                selectedTripButton?.apply {
                    setBackgroundResource(R.drawable.gray_btn)
                    setTextColor(customBlueColor)

                }

                button.setBackgroundResource(R.drawable.dark_blue_button)
                button.setTextColor(Color.WHITE)
                selectedTripButton = button
            }
        }

        fun handleChoosePositionButtonClick(button: Button) {

            if (button == positionButton) {
                // Deselect the currently selected button
                button.setBackgroundResource(R.drawable.gray_btn)
                button.setTextColor(customBlueColor)
                positionButton = null


            } else {
                // Select the new button and deselect the previously selected one
                positionButton?.apply {
                    setBackgroundResource(R.drawable.gray_btn)
                    setTextColor(Color.WHITE)
                }

                button.setBackgroundResource(R.drawable.gray_btn)
                button.setTextColor(Color.WHITE)
                positionButton = button
            }
        }
    }

