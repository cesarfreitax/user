package com.cesar.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cesar.user.databinding.LoginBinding

@SuppressLint("ClickableViewAccessibility")
class LoginActivity : AppCompatActivity() {

    private val binding by lazy { LoginBinding.inflate(layoutInflater) }
    private val sharedPreferences: SharedPreferences by lazy { getSharedPreferences("user", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        delaySplashScreen()
        installSplashScreen()
        setContentView(binding.root)

        checkRememberLogin()
        setupButton()
        setShowPwdBtn()

    }

    private fun delaySplashScreen() {
        Thread.sleep(5000)
    }

    private fun setShowPwdBtn() {
        binding.loginBtnShowPwd.setOnTouchListener { _, event ->
            return@setOnTouchListener showOrHidePassword(event)
        }
    }

    private fun showOrHidePassword(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> binding.loginPassword.inputType =
                InputType.TYPE_CLASS_TEXT
            MotionEvent.ACTION_UP -> {
                binding.loginPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
        }
        return true
    }

    private fun setupButton() {
        binding.loginBtnRegister.setOnClickListener {
            navigationToRegister()
        }

        binding.loginBtnSubmit.setOnClickListener {
            login()
        }
    }

    private fun navigationToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun checkRememberLogin() {
        val rememberPassword = sharedPreferences.getBoolean("rememberPassword", false)
        val emailLogin = sharedPreferences.getString("loginEmail", "")
        val emailPassword = sharedPreferences.getString("loginPassword", "")

        binding.loginEmail.setText(emailLogin)
        binding.loginPassword.setText(emailPassword)
        binding.loginCheckbox.isChecked = rememberPassword
    }

    private fun login() {
        if (isLoginValid()) {
            rememberLogin()
            nagivationToHome()
        }
    }

    private fun isLoginValid() : Boolean {
        val emailSharedPref = sharedPreferences.getString("email", "")
        val passwordSharedPref = sharedPreferences.getString("password", "")

        if (binding.loginEmail.text.toString() != emailSharedPref) {
            return modalError("Email inválido.")
        }

        if (binding.loginPassword.text.toString() != passwordSharedPref) {
            return modalError("Senha inválida.")
        }

        return true
    }

    private fun modalError(message: String) : Boolean {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Falha no login")
        builder.setMessage(message)
        builder.setPositiveButton("Tentar novamente") { _, _ -> }
        val dialog = builder.create()
        dialog.show()
        return false
    }

    private fun nagivationToHome() {
        val intent = Intent(this, DashboardActivity::class.java)
        Toast.makeText(applicationContext, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    private fun rememberLogin() {
        val loginEmail = if (binding.loginCheckbox.isChecked) binding.loginEmail.text.toString() else ""
        val loginPassword = if (binding.loginCheckbox.isChecked) binding.loginPassword.text.toString() else ""
        val rememberPassword = binding.loginCheckbox.isChecked

        sharedPreferences.edit().putString("loginEmail", loginEmail).apply()
        sharedPreferences.edit().putString("loginPassword", loginPassword).apply()
        sharedPreferences.edit().putBoolean("rememberPassword", rememberPassword).apply()
    }
}