package com.hermes.protecme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hermes.protecme.R
import com.hermes.protecme.model.Article

class AdapterArticle(val listArticle:List<Article>):RecyclerView.Adapter<AdapterArticle.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val view=layoutInflater.inflate(R.layout.item_article,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listArticle[position])
    }



    override fun getItemCount(): Int {
        return listArticle.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val desc = itemView.findViewById<TextView>(R.id.tv_desc)
        fun bind(article:Article){
            title.text = article.title
            desc.text = article.desc
        }
    }
}