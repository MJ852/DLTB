package com.example.dltb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DBHelper(context: Context) : SQLiteOpenHelper(context, "DLTBDatabase", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Employees  (UID TEXT PRIMARY KEY, Name TEXT, EmployeeType TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Employees")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "DLTBDatabase"
        private const val DATABASE_VERSION = 3
    }

    fun insertEmployees(employees: List<Employee>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (employee in employees) {
                val contentValues = ContentValues()
                contentValues.put("UID", employee.UID)
                contentValues.put("Name", employee.Name)
                contentValues.put("EmployeeType", employee.EmployeeType)
                db.insert("Employees", null, contentValues)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DBHelper", "Error inserting drivers: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getEmployeeData(): List<Employee> {
        val employees = mutableListOf<Employee>()
        val database = this.readableDatabase

        val query = "SELECT * FROM Employees"
        val cursor = database.rawQuery(query, null)

        try {
            cursor.use {
                while (it.moveToNext()) {
                    val uid = it.getString(it.getColumnIndex("UID"))
                    val name = it.getString(it.getColumnIndex("Name"))
                    val employeeType = it.getString(it.getColumnIndex("EmployeeType"))
                    employees.add(Employee(uid, name, employeeType))
                }
            }
        } catch (e: Exception) {
            Log.e("DatabaseError", "Error retrieving employee data: ${e.message}", e)
        } finally {
            cursor.close()
            database.close()
        }
        return employees
    }

    // Check if the driverID exists
    fun employeeExists(UID: String): Boolean {
        val db = readableDatabase
        val query = "SELECT UID FROM Employees WHERE UID = ?"
            val cursor = db.rawQuery(query, arrayOf(UID))
            try {
                return cursor.moveToFirst()
            } finally {
                cursor.close()
                db.close()
        }
    }
}
