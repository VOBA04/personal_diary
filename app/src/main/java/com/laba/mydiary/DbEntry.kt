package com.laba.mydiary

import android.content.ContentValues

class DbEntry(val db: DBHelper) {
    fun addEntry(entry: Entry, userId: Long) : Long{
        val values = ContentValues()
        values.put("user_id", userId)
        values.put("title", entry.title)
        values.put("text", entry.text)
        var images =""
        for (i in 0..<(entry.images?.size ?: 0))
            images=images+ entry.images!![i] +" "
        images.trim()
        values.put("images", images)
        val result = db.writableDatabase.insert("Entries", null, values)
        db.close()
        return result
    }

    fun deleteEntry(id: Long) : Int{
        val result = db.writableDatabase.delete("Entries", "id = $id", null)
        db.close()
        return result
    }

    fun updateEntry(id: Long, entry: Entry) : Int{
        val values = ContentValues()
        values.put("title", entry.title)
        values.put("text", entry.text)
        values.put("date", entry.date)
        var images =""
        for (i in 0..<(entry.images?.size ?: 0))
            images=images+ entry.images!![i] +" "
        images.trim()
        values.put("images", images)
        val result = db.writableDatabase.update("Entries", values, "id = $id", null)
        db.close()
        return result
    }

    fun getEntries(userId: Long) : ArrayList<Entry>{
        val entries = ArrayList<Entry>()
        val cursor = db.readableDatabase.rawQuery("SELECT * FROM Entries WHERE user_id = '$userId'", null)
        if (cursor.moveToFirst()){
            for (i in 1..cursor.count){
                val id = cursor.getLong(0)
                val title = cursor.getString(2)
                val date = cursor.getString(3)
                val text = cursor.getString(4)
                val imagesStr = cursor.getString(5)
                val images = imagesStr.split(" ")
                val entry = Entry(id, title, date, text, ArrayList(images))
                entries.add(entry)
                cursor.moveToNext()
            }
        }
        cursor.close()
        return entries
    }
}