package com.hermes.protecme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val intent = Intent (this, DetailReportActivity ::class.java)
        val bst = findViewById<ImageView>(R.id.pelecehan)

        supportActionBar?.hide()

        bst.setOnClickListener {
            startActivity(intent)
        }
    }
}