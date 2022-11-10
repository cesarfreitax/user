package com.cesar.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cesar.user.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private var imagesList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setFullScreen()
        postToList()
        setAdapter()
        setADRecyclerViewAnimation()


    }

    private fun setFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun setADRecyclerViewAnimation() {
        binding.home.cardsRecyclerview.addItemDecoration(HorizontalItemDecoration())
        PageItemSnapHelper().attachToRecyclerView(binding.home.cardsRecyclerview)
    }

    private fun setAdapter() {
        binding.home.cardsRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ADCardAdapter(imagesList)
        binding.home.cardsRecyclerview.adapter = adapter
    }

    private fun addtoList(image: Int) {
        imagesList.add(image)
    }

    private fun postToList() {
        for (i in 1..5) {
            addtoList(R.drawable.anuncio_1)
        }
    }

}