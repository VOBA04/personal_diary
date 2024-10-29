package com.laba.mydiary

import android.content.ContentValues

class DbUser(val db: DBHelper) {
    fun addUser(user: User): Long {
        val values = ContentValues()
        values.put("login", user.login)
        values.put("pass", user.password)
        val result = db.writableDatabase.insert("Users", null, values)
        db.close()
        return result
    }

    fun getUser(login: String, password: String): Long {
        val result = db.readableDatabase.rawQuery(
            "SELECT * FROM Users WHERE login = '$login' AND pass = '$password'",
            null
        )
        if (!result.moveToFirst()) {
            result.close()
            return -1L
        }
        val userId = result.getLong(0)
        result.close()
        return userId
    }

    fun getAllUsers(): ArrayList<User> {
        val result = db.readableDatabase.rawQuery("SELECT * FROM Users", null)
        val users = ArrayList<User>()
        if (result.moveToFirst()) {
            for (i in 1..result.count) {
                users.add(User(result.getString(1), result.getString(2)))
            }
        }
        result.close()
        return users
    }
}