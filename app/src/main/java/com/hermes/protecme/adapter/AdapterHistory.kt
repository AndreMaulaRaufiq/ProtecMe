package com.hermes.protecme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hermes.protecme.R
import com.hermes.protecme.model.Pelaporan

class AdapterHistory(val listHistory:ArrayList<Pelaporan>, val listener:AdapterHistory.OnItemClick):RecyclerView.Adapter<AdapterHistory.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val view=layoutInflater.inflate(R.layout.item_history,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listHistory[position],listener)
    }



    override fun getItemCount(): Int {
        return listHistory.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)

        fun bind(article:Pelaporan,listener: OnItemClick){
            title.text = article.jenisPelecehan
            itemView.setOnClickListener {
                listener.klik()
            }
        }
    }

    interface OnItemClick{
        fun klik()
    }
}
