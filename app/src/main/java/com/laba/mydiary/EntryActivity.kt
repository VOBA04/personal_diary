package com.laba.mydiary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar

class EntryActivity : AppCompatActivity() {
    private var startX: Float = 0f
    private var endX: Float = 0f
    private lateinit var imagesAdapter: ImagesAdapter

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
        requestedOrientation = if (resources.configuration.smallestScreenWidthDp >= 600)
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        else
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val title = findViewById<EditText>(R.id.title)
        val btnBack = findViewById<Button>(R.id.button_back)
        val btnDelete = findViewById<Button>(R.id.button_delete)
        val mainText = findViewById<EditText>(R.id.editText_text)
        val btnAddIm = findViewById<Button>(R.id.button_add_image)
        val imagesView = findViewById<RecyclerView>(R.id.images_list)
        var images = intent.getStringArrayListExtra("entryImages")
        if (images == null)
            images = arrayListOf()
        val id = intent.getLongExtra("entryId", 0)
        val userId = intent.getLongExtra("userId", 0)

        val db = DbEntry(DBHelper(this, null))
        title.setText(intent.getStringExtra("entryTitle"))
        mainText.setText(intent.getStringExtra("entryText"))
        imagesView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imagesAdapter = ImagesAdapter(images, this)
        imagesAdapter.onClickListener(object : ImagesAdapter.OnItemClickListener {
            override fun onLongItemClick(position: Int, view: View) {
                val time = Calendar.getInstance().time
                val formatter = SimpleDateFormat("dd.MM.yyyy")
                images.removeAt(position)
                imagesView.adapter = imagesAdapter
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
            }
        })
        imagesView.adapter = imagesAdapter

        btnBack.setOnClickListener {
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

        btnDelete.setOnClickListener {
            db.deleteEntry(id)
            val intent = Intent(this, EntriesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }
        val getImageIntent =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                uri
            }
        btnAddIm.setOnClickListener {
            getImageIntent.launch(arrayOf("image/*"))
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
        val title = findViewById<EditText>(R.id.title)
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

    @SuppressLint("SimpleDateFormat")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val dataUri = data?.data
            if (dataUri != null) {
                var images = intent.getStringArrayListExtra("entryImages")
                if (images == null)
                    images = arrayListOf()
                val imagesView = findViewById<RecyclerView>(R.id.images_list)
//                contentResolver.takePersistableUriPermission(
//                    dataUri,
//                    Intent.FLAG_GRANT_READ_URI_PERMISSION
//                )
                images.add(dataUri.toString())
                imagesView.adapter = imagesAdapter
                val id = intent.getLongExtra("entryId", 0)
                val title = findViewById<EditText>(R.id.title)
                val mainText = findViewById<EditText>(R.id.editText_text)
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
            }
        }
    }
}