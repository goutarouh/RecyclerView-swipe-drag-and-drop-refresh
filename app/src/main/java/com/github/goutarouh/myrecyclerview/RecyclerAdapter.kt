package com.github.goutarouh.myrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(countryList: MutableList<String>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var countries = countryList

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        lateinit var itemTitle: TextView
        lateinit var itemImage: ImageView

        init {
            itemImage = itemView.findViewById(R.id.itemImage)
            itemTitle = itemView.findViewById(R.id.itemTitle)

            itemView.setOnClickListener {
                val position = adapterPosition
                Toast.makeText(itemView.context, "You clicked on ${countries[position]}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return countries.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = countries[position]
    }
}