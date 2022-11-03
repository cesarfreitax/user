package com.cesar.user

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.cesar.user.databinding.ActivityMenuHamburguerBinding
import java.io.File

class MenuHamburguerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMenuHamburguerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuHamburguerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val headerPerfilPhoto = header.findViewById<ImageView>(R.id.home_perfil_img)
        headerPerfilPhoto.setImageDrawable(Drawable.createFromPath(file.toString()))
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

        if (item.itemId == R.id.nav_slideshow) {
            Toast.makeText(applicationContext, "CLICOU NA SLIDESHOW", Toast.LENGTH_SHORT).show()
        }

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        return true
    }
}