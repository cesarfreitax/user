package com.cesar.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ADCardAdapter(private var images: List<Int>) : RecyclerView.Adapter<ADCardAdapter.ADCardHolder>() {


    inner class ADCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImg = itemView.findViewById<ImageView>(R.id.ad_card_item_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ADCardHolder {
        return ADCardHolder(LayoutInflater.from(parent.context).inflate(R.layout.ad_card_item, parent, false))
    }

    override fun onBindViewHolder(holder: ADCardHolder, position: Int) {
        holder.itemImg.setImageResource(images[position])

            holder.itemImg.setOnClickListener {
                Toast.makeText(holder.itemImg.context, "Clicou no item #${position + 1}!", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}