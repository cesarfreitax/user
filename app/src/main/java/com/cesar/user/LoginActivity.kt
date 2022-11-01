package com.cesar.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class LoginActivity : AppCompatActivity() {
    lateinit var registerBtn: Button
    lateinit var password: EditText
    lateinit var showPassword: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        registerBtn = findViewById(R.id.login_btn_register)
//        showPassword = findViewById(R.id.login_show_password)
//        password = findViewById(R.id.login_password)

        registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

//        showPassword.setOnClickListener {
//            password.transformationMethod = PasswordTransformationMethod.getInstance()
//        }

    }
}