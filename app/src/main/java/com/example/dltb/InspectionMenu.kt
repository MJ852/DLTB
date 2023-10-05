package com.example.dltb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class InspectionMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspection_menu)


        val inspectionSummary = findViewById<Button>(R.id.inspection_summary_btn)
        inspectionSummary.setOnClickListener {
            val intent = Intent(this, InspectionSummary::class.java)
            startActivity(intent)
        }
    }
}