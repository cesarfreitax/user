package com.cesar.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class LoginActivity : AppCompatActivity() {
    lateinit var registerBtn: Button
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var showPassword: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        email = findViewById(R.id.login_email)

        registerBtn = findViewById(R.id.login_btn_register)
//        showPassword = findViewById(R.id.login_show_password)
//        password = findViewById(R.id.login_password)

        registerBtn.setOnClickListener {
            val intent = Intent(this, MenuHamburguerActivity::class.java)
            startActivity(intent)
        }

        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("nome", "Nome nao encontrado!")
        email.setText(name)

        val filename = "myfile"
        val fileContents = "Hello world!"
        openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }

//        showPassword.setOnClickListener {
//            password.transformationMethod = PasswordTransformationMethod.getInstance()
//        }

    }
}