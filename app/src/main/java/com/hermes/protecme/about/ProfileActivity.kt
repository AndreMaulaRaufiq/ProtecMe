package com.hermes.protecme.about

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hermes.protecme.LoginActivity
import com.hermes.protecme.MainActivity.Companion.auth
import com.hermes.protecme.MainActivity.Companion.editor
import com.hermes.protecme.MainActivity.Companion.mainModel
import com.hermes.protecme.R
import com.hermes.protecme.databinding.ActivityProfileBinding
import com.hermes.protecme.model.viewmodel.MainViewModel

class ProfileActivity : AppCompatActivity(),AdapterAbout.ItemAdapterCallBack {
    private lateinit var binding:ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.btnLogout.setOnClickListener {
            auth.signOut()
            editor.clear()?.apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //list
        val listAbout = listOf<ItemAbout>(
            ItemAbout("Profile", R.drawable.ic_baseline_person_24_black,Intent(this,EditProfileActivity::class.java)),
            ItemAbout("Riwayat Patroli", R.drawable.ic_baseline_remove_red_eye_24,Intent(this,EditProfileActivity::class.java)),
            ItemAbout("Bantuan", R.drawable.ic_baseline_help_outline_24,Intent(this,EditProfileActivity::class.java)),
        )

        setData(listAbout)

        mainModel.user.observe(this){user->
            //nama
            binding.tvNameProfile.text = user?.nama

            if (user?.url_img?.isEmpty() != true){
                //gambar
                Glide.with(this)
                    .load(user?.url_img)
                    .into(binding.ivProfile)
            }
        }


    }
    fun setData(abt: List<ItemAbout>) {
        val adapterAbout = AdapterAbout(abt,this)
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcListAbout.layoutManager = layoutManager
        binding.rcListAbout.adapter = adapterAbout
    }

    override fun onClick(v: View, data: ItemAbout) {
        startActivity(data.intent)
    }
}