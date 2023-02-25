package com.hermes.protecme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val intent = Intent (this, Pelecehan_BST ::class.java)
        val bst = findViewById<ImageView>(R.id.pelecehan)
        bst.setOnClickListener {
            startActivity(intent)
        }
    }
}