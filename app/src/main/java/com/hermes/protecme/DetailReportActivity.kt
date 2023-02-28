package com.hermes.protecme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DetailReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_report)
        supportActionBar?.hide()
    }
}