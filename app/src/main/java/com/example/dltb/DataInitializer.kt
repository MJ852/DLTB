package com.example.dltb

import android.content.ContentValues
import android.content.Context
import android.util.Log

class DataInitializer(private val dbHelper: DBHelper) {
    fun initializeData() {

        val employeeData = listOf(
            Employee("4B7851D9", "Driver Name 01", "driver"),
            Employee("4BAF43D9", "Driver Name 02", "driver"),
            Employee("0BE141D9", "Driver Name 03", "driver"),
            Employee("CB1840D9", "Conductor Name 01", "conductor"),
            Employee("9B9D53D9", "Conductor Name 02", "conductor"),
            Employee("6B284CD9", "Conductor Name 03", "conductor"),
            Employee("6BBB4ED9", "Dispatcher Name 01", "dispatcher"),
            Employee("AB4D50D9", "Dispatcher Name 02", "dispatcher"),
            Employee("DC39E156", "Dispatcher Name 03", "dispatcher"),
            Employee("A1994629", "George M. Garcia", "driver"),
            Employee("B5866EB3", "Ardie L. San Pedro", "conductor")
        // Add more employee data here...
        )

        try {
            // Insert data into tables
            dbHelper.insertEmployees(employeeData)
        } catch (e: Exception) {
            Log.d("DataInitializer", "Error Initializing data")
        }
    }
}

