package com.hermes.protecme

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hermes.protecme.databinding.ActivityDetailReportBinding
import com.hermes.protecme.model.Pelaporan

class DetailReportActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val data = intent.extras
        val pelaporan = data?.getParcelable<Pelaporan>("Data")

        binding.apply {
            titlePelaporan.text = pelaporan?.judulPelecehan
            tvJenislapor.text = pelaporan?.jenisPelecehan
            tvJudulPermasalhan.text = pelaporan?.judulPelecehan
            tvPihakBersangkutan.text = pelaporan?.pihakBersangkutan
            tvTglKejadian.text = pelaporan?.tglKejadian
            tvTglKejadian.text = pelaporan?.kronologi
        }
        Glide.with(this)
            .load(pelaporan?.fileBukti)
            .into(binding.ivBukti)



    }
}