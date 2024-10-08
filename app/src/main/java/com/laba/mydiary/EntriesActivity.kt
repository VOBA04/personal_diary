package com.laba.mydiary

import android.annotation.SuppressLint
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
        val entries = arrayListOf<Entry>()
        val addButton = findViewById<FloatingActionButton>(R.id.button_add_entry)

        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd.mm.yyyy")
        entries.add(Entry("Test1", formatter.format(time), "", arrayListOf()))

        addButton.setOnClickListener {
            entries.add(Entry("Test entry", formatter.format(time), "", arrayListOf()))
            entriesList.adapter = EntriesAdapter(entries, this)
        }

        entriesList.layoutManager = LinearLayoutManager(this)
        entriesList.adapter = EntriesAdapter(entries, this)
    }
}