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

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toRegButton = findViewById<Button>(R.id.button_to_reg)
        val userLogin = findViewById<EditText>(R.id.editText_login_auth)
        val userPass = findViewById<EditText>(R.id.editText_pass_auth)
        val authButton = findViewById<Button>(R.id.button_auth)

        authButton.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPass.text.toString().trim()

            if(login == "" || password == "")
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_LONG).show()
            else{
                val db = DbUser(this, null)
                val isAuth = db.getUser(login, password)
                if (isAuth){
                    userLogin.text.clear()
                    val intent = Intent(this, EntriesActivity::class.java)
                    startActivity(intent)
                }
                else
                    Toast.makeText(this, "Такого пользователя нет", Toast.LENGTH_LONG).show()
                userPass.text.clear()
            }
        }

        toRegButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}