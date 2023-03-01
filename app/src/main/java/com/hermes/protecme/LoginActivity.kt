package com.hermes.protecme

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.hermes.protecme.databinding.ActivityLoginBinding
import com.hermes.protecme.model.viewmodel.MainViewModel
import com.hermes.protecme.sharepref.SavePref

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    lateinit var sharePref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var mainModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        //model
        mainModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        //firebase
        auth = FirebaseAuth.getInstance()
        binding.tvDaftar.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.apply {
            etEmail.setText("andi@gmail.com")
            etPwd.setText("12345")
        }

        binding.btnLogin.setOnClickListener{
            binding.pgBar.visibility = View.VISIBLE
            val email = binding.etEmail.text.toString()
            val pwd = binding.etPwd.text.toString()

            if (email.isEmpty()) {
                binding.etEmail.error = "Masukan Email"
            }
            if (pwd.isEmpty()) {
                binding.etPwd.error = "Masukan Password"
            }

            auth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener {
                if (it.isSuccessful){
                    //shareprfe
                    sharePref =getSharedPreferences(SavePref.PREF_NAME, Context.MODE_PRIVATE)
                    editor = sharePref.edit()

                    Toast.makeText(applicationContext,"Login Succes", Toast.LENGTH_LONG).show()
                    val idUser = auth.currentUser!!.uid
                    editor.putString(SavePref.UID, idUser).apply()
                    mainModel.getUser(idUser)
                    binding.pgBar.visibility = View.GONE
                    toHome()

                }else{
                    binding.pgBar.visibility = View.GONE
                }
            }
                .addOnFailureListener {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                    binding.pgBar.visibility = View.GONE
                }

        }

    }
    private fun toHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}