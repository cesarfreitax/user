package com.cesar.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    lateinit var registerBtn: Button
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var showPassword: ImageView
    lateinit var loginBtn: Button
    lateinit var checkbox: CheckBox



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val emailSharedPref = sharedPreferences.getString("email", "Email nao encontrado!")
        val passwordSharedPref = sharedPreferences.getString("password", "123")
        val rememberPassword = sharedPreferences.getString("rememberPassword", "false")

        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)
        loginBtn = findViewById(R.id.login_btn_submit)
        checkbox = findViewById(R.id.login_checkbox)

        if (rememberPassword == "true") {
            email.setText(emailSharedPref)
            password.setText(passwordSharedPref)
            checkbox.isChecked = true
        } else {
            checkbox.isChecked = false
        }

        registerBtn = findViewById(R.id.login_btn_register)
//        showPassword = findViewById(R.id.login_show_password)
//        password = findViewById(R.id.login_password)

        registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            if (email.text.toString() == emailSharedPref && password.text.toString() == passwordSharedPref) {
                val intent = Intent(this, MenuHamburguerActivity::class.java)
                Toast.makeText(applicationContext, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show()
                if (checkbox.isChecked) {
                    sharedPreferences.edit().putString("rememberPassword", "true").apply()
                }
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Email ou senha inválido.", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putString("rememberPassword", "false").apply()
            }

        }

//        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (buttonView.isChecked) {

//            } else {
//                email.setText("")
//                password.setText("")
//            }
//        }

//        val filename = "myfile"
//        val fileContents = "Hello world!"
//        openFileOutput(filename, Context.MODE_PRIVATE).use {
//            it.write(fileContents.toByteArray())
//        }

//        showPassword.setOnClickListener {
//            password.transformationMethod = PasswordTransformationMethod.getInstance()
//        }

    }
}