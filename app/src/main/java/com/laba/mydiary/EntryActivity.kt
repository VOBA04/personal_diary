package com.laba.mydiary

import android.os.Bundle
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

        title.text = intent.getStringExtra("entryTitle")
        mainText.setText(intent.getStringExtra("entryText"))
        //images

        btn_back.setOnClickListener {
            val db = DbEntry(DBHelper(this, null))
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            db.updateEntry(id, Entry(id, title.text.toString(), formatter.format(time), mainText.text.toString(), images))
            finish()
        }
    }
}