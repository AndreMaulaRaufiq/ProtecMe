package com.hermes.protecme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_report)
        supportActionBar?.hide()
    }
}