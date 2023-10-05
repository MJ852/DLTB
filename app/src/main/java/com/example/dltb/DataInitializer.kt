package com.example.dltb

import android.content.ContentValues
import android.content.Context
import android.util.Log

class DataInitializer(private val dbHelper: DBHelper) {


    fun initializeData() {
        Log.d("DataInitializer", "Initializing data")

        val drivers = arrayOf(
            Pair("4B7851D9", "Juan Cruz"),
            Pair("91011123", "Maria Clara")
            // Add more driver data here...
        )

        val dispatchers = arrayOf(
            Pair("AB4D50D9", "Juan Cruz"),
            Pair("98765421", "Maria Clara")
            // Add more driver data here...
        )

        val conductors = arrayOf(
            Pair("CB1840D9", "Juan Cruz"),
            Pair("45612348", "Maria Clara")
            // Add more driver data here...
        )

        try {
            // Insert data into tables
            dbHelper.insertDrivers(drivers)
            dbHelper.insertDispatchers(dispatchers)
            dbHelper.insertConductors(conductors)
        }catch (e: Exception) {
            Log.d("DataInitializer", "Error Initializing data")
        }


    }

//    private fun insertConductors(conductors: Array<Pair<String, String>>) {
//        val db = dbHelper.writableDatabase
//        db.beginTransaction()
//        try {
//            for (conductor in conductors) {
//                val contentValues = ContentValues()
//                contentValues.put("ConductorID", conductor.first)
//                contentValues.put("Name", conductor.second)
//                db.insert("Conductor", null, contentValues)
//            }
//            db.setTransactionSuccessful()
//        } catch (e: Exception) {
//            // Handle the exception, e.g., log an error
//        } finally {
//            db.endTransaction()
//        }
//    }
//
//    private fun insertDispatchers(dispatchers: Array<Pair<String, String>>) {
//        val db = dbHelper.writableDatabase
//        db.beginTransaction()
//        try {
//            for (dispatcher in dispatchers) {
//                val contentValues = ContentValues()
//                contentValues.put("DispatcherID", dispatcher.first)
//                contentValues.put("Name", dispatcher.second)
//                db.insert("Dispatchers", null, contentValues)
//            }
//            db.setTransactionSuccessful()
//        } catch (e: Exception) {
//            // Handle the exception, e.g., log an error
//        } finally {
//            db.endTransaction()
//        }
//    }
}
