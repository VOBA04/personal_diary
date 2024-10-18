package com.laba.mydiary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar

class EntryActivity : AppCompatActivity() {
    private var startX: Float = 0f
    private var endX: Float = 0f

    @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entry)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val title = findViewById<TextView>(R.id.title)
        val btn_back = findViewById<Button>(R.id.button_back)
        val btn_delete = findViewById<Button>(R.id.button_delete)
        val mainText = findViewById<EditText>(R.id.editText_text)
        val btn_add_im = findViewById<Button>(R.id.button_add_image)
        val imagesView = findViewById<RecyclerView>(R.id.images_list)
        val images = intent.getStringArrayListExtra("entryImages")
        val id = intent.getLongExtra("entryId", 0)
        val userId = intent.getLongExtra("userId", 0)

        title.text = intent.getStringExtra("entryTitle")
        mainText.setText(intent.getStringExtra("entryText"))
        //images

        btn_back.setOnClickListener {
            val db = DbEntry(DBHelper(this, null))
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            db.updateEntry(
                id,
                Entry(
                    id,
                    title.text.toString(),
                    formatter.format(time),
                    mainText.text.toString(),
                    images
                )
            )
            val intent = Intent(this, EntriesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        btn_delete.setOnClickListener {
            val db = DbEntry(DBHelper(this, null))
            db.deleteEntry(id)
            val intent = Intent(this, EntriesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

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
                        startX > endX -> swipeLeft()
                    }
                    true
                }

                else -> false
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun swipeRight() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.swipe_right)
        window.decorView.startAnimation(animation)

        val id = intent.getLongExtra("entryId", 0)
        val title = findViewById<TextView>(R.id.title)
        val mainText = findViewById<EditText>(R.id.editText_text)
        val images = intent.getStringArrayListExtra("entryImages")
        val userId = intent.getLongExtra("userId", 0)
        val db = DbEntry(DBHelper(this, null))
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        db.updateEntry(
            id,
            Entry(
                id,
                title.text.toString(),
                formatter.format(time),
                mainText.text.toString(),
                images
            )
        )
        val intent = Intent(this, EntriesActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
        finish()
    }

    private fun swipeLeft() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.swipe_left)
        window.decorView.startAnimation(animation)

        val id = intent.getLongExtra("entryId", 0)
        val userId = intent.getLongExtra("userId", 0)
        val db = DbEntry(DBHelper(this, null))
        db.deleteEntry(id)
        val intent = Intent(this, EntriesActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
        finish()
    }
}