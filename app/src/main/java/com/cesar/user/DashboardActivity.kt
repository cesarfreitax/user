package com.cesar.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.cesar.user.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        replaceFragment(CardsFragment())

        binding.dashboardBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.cards -> replaceFragment(CardsFragment())
                R.id.advantages -> replaceFragment(AdvantagesFragment())
                R.id.more -> replaceFragment(MoreFragment())

                else -> { }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.dashboard_container, fragment)
        fragmentTransaction.commit()
    }
}