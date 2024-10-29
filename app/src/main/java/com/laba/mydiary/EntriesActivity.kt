package com.laba.mydiary

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.AnimationUtils
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
    private var startX: Float = 0f
    private var endX: Float = 0f

    @SuppressLint("SimpleDateFormat", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entries)
        requestedOrientation = if (resources.configuration.smallestScreenWidthDp >= 600)
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        else
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
            val id = db.addEntry(
                Entry(0, "Новая заметка", formatter.format(time), "", arrayListOf()),
                userId
            )
            entries.add(Entry(id, "Новая заметка", formatter.format(time), "", arrayListOf()))
            entriesList.adapter = EntriesAdapter(entries, userId, this)

            val intent = Intent(this, EntryActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("entryId", id)
            intent.putExtra("entryTitle", entries.last().title)
            intent.putExtra("entryText", entries.last().text)
            intent.putStringArrayListExtra("entryImages", entries.last().images)
            startActivity(intent)
            finish()
        }

        entriesList.layoutManager = LinearLayoutManager(this)
        entriesList.adapter = EntriesAdapter(entries, userId, this)

        window.decorView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    true
                }

                MotionEvent.ACTION_UP -> {
                    endX = event.x
                    when {
                        endX > startX -> swipeRight()
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun swipeRight() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.swipe_right)
        window.decorView.startAnimation(animation)

        intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}