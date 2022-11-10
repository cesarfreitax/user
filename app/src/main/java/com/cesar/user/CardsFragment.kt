package com.cesar.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cesar.user.databinding.FragmentCardsBinding

class CardsFragment : Fragment() {

    private var imagesList = mutableListOf<Int>()

    private val binding by lazy {
        FragmentCardsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postToList()

        binding.cardsRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.cardsRecyclerview.addItemDecoration(HorizontalItemDecoration())
        val adapter = ADCardAdapter(imagesList)
        binding.cardsRecyclerview.adapter = adapter
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