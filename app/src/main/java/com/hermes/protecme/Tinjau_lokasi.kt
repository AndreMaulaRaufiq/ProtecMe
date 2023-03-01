package com.hermes.protecme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hermes.protecme.adapter.AdapterWilayah
import com.hermes.protecme.databinding.TinjauLokasiBinding
import com.hermes.protecme.model.Wilayah

class Tinjau_lokasi : AppCompatActivity() {
    private lateinit var binding:TinjauLokasiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = TinjauLokasiBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        supportActionBar?.hide()
        val listWilayah = listOf<Wilayah>(
            Wilayah("Pasar Kliwon","9"),
            Wilayah("Jebres","12"),
            Wilayah("Banjar Sari","13"),
            Wilayah("Laweyan","12"),
            Wilayah("Serengan","8"),
        )

        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        val adapterWilayah = AdapterWilayah(listWilayah)

        binding.rcListWilayah.layoutManager = layoutManager
        binding.rcListWilayah.adapter = adapterWilayah


    }
}