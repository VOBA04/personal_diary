package com.laba.mydiary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userLogin = findViewById<EditText>(R.id.editText_login)
        val userPass = findViewById<EditText>(R.id.editText_pass)
        val regButton =  findViewById<Button>(R.id.button_reg)
        val signInButton = findViewById<Button>(R.id.button_to_sign_in)

        regButton.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPass.text.toString().trim()

            if(login == "" || password == "")
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_LONG).show()
            else {
                val user = User(login, password)
                val db = DbUser(DBHelper(this, null))
                if(db.addUser(user) == -1L)
                    Toast.makeText(this, "Такой пользователь уже существует", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this, "Вы успешно зарегистрированы!", Toast.LENGTH_LONG).show()
                userPass.text.clear()
                userLogin.text.clear()
            }
        }

        signInButton.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity((intent))
        }
    }
}