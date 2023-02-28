package com.hermes.protecme

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hermes.protecme.adapter.AdapterArticle
import com.hermes.protecme.databinding.ActivityMainBinding
import com.hermes.protecme.model.Article

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnTlp.setOnClickListener {
            startActivity(Intent(this,SosActivity::class.java))
        }

        val listArticle = listOf<Article>(
            Article("Pelecehan di UNS","berada di uns oleh remaja berinisial S","",""),
            Article("Pelecehan di Solo","berada di uns oleh remaja berinisial S","",""),
            Article("Pelecehan di Laweyan","berada di uns oleh remaja berinisial S","",""),
        )

        val adapterArticle = AdapterArticle(listArticle)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.rcListArticle.layoutManager = layoutManager
        binding.rcListArticle.adapter = adapterArticle


    }



}