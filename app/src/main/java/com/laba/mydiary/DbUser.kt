package com.laba.mydiary

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbUser(val db: DBHelper) {
    fun addUser(user: User) : Long{
        val values = ContentValues()
        values.put("login", user.login)
        values.put("pass", user.password)
        val result = db.writableDatabase.insert("Users", null, values)
        db.close()
        return result
    }

    fun getUser(login: String, password: String) : Long {
        val result = db.readableDatabase.rawQuery("SELECT * FROM Users WHERE login = '$login' AND pass = '$password'", null)
        if (!result.moveToFirst()) {
            result.close()
            return -1L
        }
        val userId = result.getLong(0)
        result.close()
        return userId
    }
}