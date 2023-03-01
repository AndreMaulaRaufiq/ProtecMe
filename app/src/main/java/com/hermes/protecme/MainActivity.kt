package com.hermes.protecme

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.hermes.protecme.about.ProfileActivity
import com.hermes.protecme.adapter.AdapterArticle
import com.hermes.protecme.databinding.ActivityMainBinding
import com.hermes.protecme.model.Article
import com.hermes.protecme.model.viewmodel.MainViewModel
import com.hermes.protecme.sharepref.SavePref

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    companion object{
        lateinit var auth: FirebaseAuth
        lateinit var sharePref: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        lateinit var mainModel: MainViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //firebaseAuth
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser==null){
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }


        //model
        mainModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        //sharepref
        sharePref = getSharedPreferences(SavePref.PREF_NAME, Context.MODE_PRIVATE)
        editor = sharePref.edit()

        val idUser = sharePref.getString(SavePref.UID,"123").toString()
        mainModel.getUser(idUser)



        binding.icProfile.setOnClickListener {
            startActivity(Intent(this,ProfileActivity::class.java))
        }
        binding.ivIconTinjau.setOnClickListener {
            startActivity(Intent(this,Tinjau_lokasi::class.java))
        }
        binding.ivIconReport.setOnClickListener {
            startActivity(Intent(this,ReportActivity::class.java))
        }
        binding.ivIconHistory.setOnClickListener {
            startActivity(Intent(this,History::class.java))
        }
        binding.fab.setOnClickListener {
            startActivity(Intent(this,SosActivity::class.java))
        }

        val listArticle = listOf<Article>(
            Article("Pelecehan di UNS","berada di uns oleh remaja berinisial S","","",""),
            Article("Pelecehan di Solo","berada di uns oleh remaja berinisial S","","",""),
            Article("Pelecehan di Laweyan","berada di uns oleh remaja berinisial S","","",""),
        )

        val adapterArticle = AdapterArticle(listArticle)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.rcListArticle.layoutManager = layoutManager
        binding.rcListArticle.adapter = adapterArticle


    }



}