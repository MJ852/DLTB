package com.example.dltb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar

class LoadingScreen : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private val delay_Splash_Screen: Long = 3000 //delay duration for 3 seconds
    private val total_Progress: Long = 3000

    private val splashRunnable = Runnable {
        val intent = Intent(this@LoadingScreen, MainActivity::class.java) //To go to Main Activity
        startActivity(intent)
        finish()
    }
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screen)

        fetchData()

        progressBar = findViewById(R.id.progress_Bar)
        progressBar.max = 100

        var currentProgress = 0
        val progressIncrement = 100
        val progressUpdateInterval = total_Progress / progressIncrement

        handler.postDelayed(object : Runnable {
            override fun run() {
                progressBar.progress = currentProgress

                if (currentProgress < 100) {
                    currentProgress++
                    handler.postDelayed(this, progressUpdateInterval)
                } else {
                    finish()
                }
            }
        }, progressUpdateInterval)

        handler.postDelayed(splashRunnable, delay_Splash_Screen)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // this removes any pending splashrunnable once activity is destroyed,
    }

    private fun fetchData() {
        // Implement data fetching
    }
}
