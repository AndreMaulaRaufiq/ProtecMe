package com.hermes.protecme.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hermes.protecme.R

class AdapterAbout(
    private val listData:List<ItemAbout>):RecyclerView.Adapter<AdapterAbout.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val view=layoutInflater.inflate(R.layout.item_about,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitleAbout)
        fun bind(data:ItemAbout){
            itemView.apply {
                tvTitle.text = data.titile
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(data.icon,0,0,0)

            }
        }
    }

    interface ItemAdapterCallBack{
        fun onClick(v: View,data: ItemAbout)
    }

}