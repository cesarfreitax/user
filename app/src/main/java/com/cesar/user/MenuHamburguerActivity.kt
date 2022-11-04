package com.cesar.user

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.cesar.user.databinding.ActivityMenuHamburguerBinding
import java.io.File

class MenuHamburguerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMenuHamburguerBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nameSharedPref: String
    private lateinit var emailSharedPref: String
    private lateinit var passwordSharedPref: String
    private lateinit var genderSharedPref: String
    private lateinit var maritalStateSharedPref: String
    private lateinit var maritalStateIndiceSharedPref: String
    private lateinit var phoneSharedPref: String
    private lateinit var cpfSharedPref: String
    private lateinit var birthSharedPref: String
    private lateinit var availableHourSharedPref: String
    private lateinit var listSharedPref: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuHamburguerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        nameSharedPref = sharedPreferences.getString("name", "Nome nao encontrado!").toString()
        emailSharedPref = sharedPreferences.getString("email", "Email nao encontrado!").toString()
        passwordSharedPref = sharedPreferences.getString("password", "Senha nao encontrada!").toString()
        genderSharedPref = sharedPreferences.getString("gender", "Sexo nao encontrado!").toString()
        maritalStateSharedPref = sharedPreferences.getString("maritalState", "Estado civil nao encontrado!").toString()
        maritalStateIndiceSharedPref = sharedPreferences.getString("maritalStateIndice", "Indice do Estado civil nao encontrado!").toString()
        phoneSharedPref = sharedPreferences.getString("phone", "Telefone nao encontrado!").toString()
        cpfSharedPref = sharedPreferences.getString("cpf", "CPF nao encontrado!").toString()
        birthSharedPref = sharedPreferences.getString("birth", "Data de nascimento nao encontrada!").toString()
        availableHourSharedPref = sharedPreferences.getString("availableHour", "Hora disponível nao encontrada!").toString()
        listSharedPref = listOf(nameSharedPref, emailSharedPref, passwordSharedPref, genderSharedPref, maritalStateSharedPref, phoneSharedPref, cpfSharedPref, birthSharedPref, availableHourSharedPref)

        setSupportActionBar(binding.appBarMenuHamburguer.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.appBarMenuHamburguer.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        val file = File(filesDir, "foto.jpg")



        val header: LinearLayout = navView.getHeaderView(0) as LinearLayout
        val headerPerfilPhoto = header.findViewById<ImageView>(R.id.home_img)
        headerPerfilPhoto.setImageDrawable(Drawable.createFromPath(file.toString()))
        val headerName = header.findViewById<TextView>(R.id.home_name)
        headerName.text = nameSharedPref
        val headerEmail = header.findViewById<TextView>(R.id.home_email)
        headerEmail.text = "${emailSharedPref}@gmail.com"
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (item.itemId == R.id.nav_home) {
            Toast.makeText(applicationContext, "CLICOU NA HOME", Toast.LENGTH_SHORT).show()

        }

        if (item.itemId == R.id.update) {
            val intent = Intent(this, RegisterActivity::class.java)
            if (listSharedPref.isNotEmpty()) {
                intent.putExtra("email", emailSharedPref)
                intent.putExtra("password", passwordSharedPref)
                intent.putExtra("birth", birthSharedPref)
                intent.putExtra("gender", genderSharedPref)
                intent.putExtra("maritalState", maritalStateSharedPref)
                intent.putExtra("maritalStateIndice", maritalStateIndiceSharedPref)
                intent.putExtra("cpf", cpfSharedPref)
                intent.putExtra("phone", phoneSharedPref)
                intent.putExtra("name", nameSharedPref)
                intent.putExtra("availableHour", availableHourSharedPref)
            }

            startActivity(intent)
            finish()
        }

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        return true
    }
}