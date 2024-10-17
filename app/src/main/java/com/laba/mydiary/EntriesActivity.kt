package com.laba.mydiary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar

class EntriesActivity : AppCompatActivity() {
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entries)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val entriesList = findViewById<RecyclerView>(R.id.entries_list)
        val addButton = findViewById<FloatingActionButton>(R.id.button_add_entry)
        val userId = intent.getLongExtra("userId", -1L)

        val db = DbEntry(DBHelper(this, null))
        val entries = db.getEntries(userId)

        addButton.setOnClickListener {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            val id = db.addEntry(Entry(0,"Новая заметка", formatter.format(time), "", arrayListOf()), userId)
            entries.add(Entry(id,"Новая заметка", formatter.format(time), "", arrayListOf()))
            entriesList.adapter = EntriesAdapter(entries, this)

            val intent = Intent(this, EntryActivity::class.java)
            intent.putExtra("entryId", id)
            intent.putExtra("entryTitle", entries.last().title)
            intent.putExtra("entryText", entries.last().text)
            intent.putStringArrayListExtra("entryImages", entries.last().images)
            startActivityForResult(intent, 1)
        }

        entriesList.layoutManager = LinearLayoutManager(this)
        entriesList.adapter = EntriesAdapter(entries, this)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onActivityResult(requestCode, resultCode, data)",
        "androidx.appcompat.app.AppCompatActivity"
    )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val entriesList = findViewById<RecyclerView>(R.id.entries_list)
        val db = DbEntry(DBHelper(this, null))
        val userId = intent.getLongExtra("userId", -1L)
        val entries = db.getEntries(userId)
        entriesList.adapter = EntriesAdapter(entries, this)
    }
}