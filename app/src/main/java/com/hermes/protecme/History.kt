package com.hermes.protecme

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hermes.protecme.adapter.AdapterHistory
import com.hermes.protecme.databinding.ActivityHistoryBinding
import com.hermes.protecme.model.Pelaporan

class History : AppCompatActivity() {
    private lateinit var binding:ActivityHistoryBinding
    private lateinit var arrayHistory:ArrayList<Pelaporan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        arrayHistory = arrayListOf()

        val ref = FirebaseDatabase.getInstance().getReference("pelaporan").orderByChild("id_user").equalTo("123")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (value in snapshot.children){
                        val pelaporan:Pelaporan? = value.getValue(Pelaporan::class.java)
                        if (pelaporan!=null){
                            arrayHistory.add(pelaporan)
                        }
                    }
                }
                setData(arrayHistory)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })






    }

    private fun setData(data:ArrayList<Pelaporan>) {

        if (data.isEmpty()){
            binding.tvLaporanKosong.visibility = View.VISIBLE
        }else{
            val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
            val adapterHistory = AdapterHistory(data,object :AdapterHistory.OnItemClick{
                override fun klik() {
                    startActivity(Intent(this@History,Pelecehan_UNS::class.java))
                }

            })
            binding.rcListHistory.layoutManager = layoutManager
            binding.rcListHistory.adapter = adapterHistory
        }


    }
}