package com.laba.mydiary

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbUser(val context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, "app", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY, login TEXT UNIQUE, pass TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS Users")
        onCreate(db)
    }

    fun addUser(user: User) : Long{
        val values = ContentValues()
        values.put("login", user.login)
        values.put("pass", user.password)
        val db = this.writableDatabase
        val result = db.insert("Users", null, values)
        db.close()
        return result
    }

    fun getUser(login: String, password: String) : Boolean {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM Users WHETE login = '$login' AND pass = '$password'", null)
        db.close()
        return result.moveToFirst()
    }
}