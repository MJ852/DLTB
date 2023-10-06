package com.example.dltb

import android.content.ContentValues
import android.content.Context
import android.util.Log

class DataInitializer(private val dbHelper: DBHelper) {


    fun initializeData() {
        Log.d("DataInitializer", "Initializing data")

        val drivers = arrayOf(
            Pair("4B7851D9", "Driver Name 01"),
            Pair("91011123", "Driver Name 02")
            // Add more driver data here...
        )

        val dispatchers = arrayOf(
            Pair("AB4D50D9", "Dispatcher Name 01"),
            Pair("98765421", "Dispatcher Name 01")
            // Add more dispatcher data here...
        )

        val conductors = arrayOf(
            Pair("CB1840D9", "Conductor Name 01"),
            Pair("45612348", "Conductor Name 02")
            // Add more conductor data here...
        )

        try {
            // Insert data into tables
            dbHelper.insertDrivers(drivers)
            dbHelper.insertDispatchers(dispatchers)
            dbHelper.insertConductors(conductors)
        } catch (e: Exception) {
            Log.d("DataInitializer", "Error Initializing data")
        }
    }
}
