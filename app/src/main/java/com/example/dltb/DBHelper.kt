package com.example.dltb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DBHelper(context: Context) : SQLiteOpenHelper(context, "DLTBDatabase", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Drivers (DriverID TEXT PRIMARY KEY, Name TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS Dispatchers (DispatcherID TEXT PRIMARY KEY, Name TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS Conductors (ConductorID TEXT PRIMARY KEY, Name TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Drivers")
        db.execSQL("DROP TABLE IF EXISTS Dispatchers")
        db.execSQL("DROP TABLE IF EXISTS Conductors")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "DLTBDatabase"
        private const val DATABASE_VERSION = 3
    }

    fun insertDrivers(drivers: Array<Pair<String, String>>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (driver in drivers) {
                val contentValues = ContentValues()
                contentValues.put("DriverID", driver.first)
                contentValues.put("Name", driver.second)
                db.insert("Drivers", null, contentValues)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DBHelper", "Error inserting drivers: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getDriverData(): List<Pair<String, String>> {
        val drivers = mutableListOf<Pair<String, String>>()
        val database = this.readableDatabase

        val query = "SELECT DriverID, Name FROM Drivers"
        val cursor = database.rawQuery(query, null)

        cursor.use {
            while (it.moveToNext()) {
                val driverID = it.getString(it.getColumnIndex("DriverID"))
                val name = it.getString(it.getColumnIndex("Name"))
                drivers.add(Pair(driverID, name))
            }
        }
        return drivers
    }

    // Check if the driverID exists
    fun driverExists(driverID: String): Boolean {
        val db = readableDatabase
        val query = "SELECT DriverID FROM Drivers WHERE DriverID = ?"
            val cursor = db.rawQuery(query, arrayOf(driverID))
            try {
                return cursor.moveToFirst()
            } finally {
                cursor.close()
                db.close()
        }
    }

    fun insertDispatchers(dispatchers: Array<Pair<String, String>>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (dispatcher in dispatchers) {
                val contentValues = ContentValues()
                contentValues.put("DispatcherID", dispatcher.first)
                contentValues.put("Name", dispatcher.second)
                db.insert("Dispatchers", null, contentValues)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DBHelper", "Error inserting Dispatchers: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getDispatcherData(): List<Pair<String, String>> {
        val dispatchers = mutableListOf<Pair<String, String>>()
        val database = this.readableDatabase

        val query = "SELECT DispatcherID, Name FROM Dispatchers"
        val cursor = database.rawQuery(query, null)

        cursor.use {
            while (it.moveToNext()) {
                val dispatcherID = it.getString(it.getColumnIndex("DispatcherID"))
                val name = it.getString(it.getColumnIndex("Name"))
                dispatchers.add(Pair(dispatcherID, name))
            }
        }
        return dispatchers
    }

    fun dispatcherExists(dispatcherID: String): Boolean {
        val db = readableDatabase
        val query = "SELECT DispatcherID FROM Dispatchers WHERE DispatcherID = ?"
        val cursor = db.rawQuery(query, arrayOf(dispatcherID))
        try {
            return cursor.moveToFirst()
        } finally {
            cursor.close()
            db.close()
        }
    }

    fun insertConductors(conductors: Array<Pair<String, String>>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (conductor in conductors) {
                val contentValues = ContentValues()
                contentValues.put("ConductorID", conductor.first)
                contentValues.put("Name", conductor.second)
                db.insert("Conductors", null, contentValues)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DBHelper", "Error inserting Conductors: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
        }

    }

    fun getConductorData(): List<Pair<String, String>> {
        val conductors = mutableListOf<Pair<String, String>>()
        val database = this.readableDatabase

        val query = "SELECT ConductorID, Name FROM Conductors"
        val cursor = database.rawQuery(query, null)

        cursor.use {
            while (it.moveToNext()) {
                val conductorID = it.getString(it.getColumnIndex("ConductorID"))
                val name = it.getString(it.getColumnIndex("Name"))
                conductors.add(Pair(conductorID, name))
            }
        }
        return conductors
    }

    fun conductorExists(conductorID: String): Boolean {
        val db = readableDatabase
        val query = "SELECT ConductorID FROM Conductors WHERE ConductorID = ?"
        val cursor = db.rawQuery(query, arrayOf(conductorID))
        try {
            return cursor.moveToFirst()
        } finally {
            cursor.close()
            db.close()
        }
    }
}
