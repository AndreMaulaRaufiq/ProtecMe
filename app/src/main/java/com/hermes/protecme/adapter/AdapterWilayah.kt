package com.hermes.protecme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hermes.protecme.R
import com.hermes.protecme.model.Pelaporan
import com.hermes.protecme.model.Wilayah

class AdapterWilayah(val listHistory:List<Wilayah>):RecyclerView.Adapter<AdapterWilayah.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val view=layoutInflater.inflate(R.layout.item_wilayah,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }



    override fun getItemCount(): Int {
        return listHistory.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvTitleWilayah)
        val kasus = itemView.findViewById<TextView>(R.id.tvKasus)

        fun bind(article:Wilayah){
            title.text = article.title
            kasus.text = article.kasus
        }
    }


}
