package com.hermes.protecme

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hermes.protecme.databinding.ActivitySignupBinding
import com.hermes.protecme.model.Users
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //firebase
        auth = FirebaseAuth.getInstance()

        //SPINNER
        val items = resources.getStringArray(R.array.jenisKelamin)
        val spinnerAdapter =
            object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }
                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view: TextView =
                        super.getDropDownView(position, convertView, parent) as TextView
                    if (position == 0) {
                        view.setTextColor(Color.GRAY)
                    } else {
                        view.setTextColor(Color.BLACK)
                    }
                    return view
                }
            }

        val spinner_jenis = findViewById<Spinner>(R.id.spinner_jenis)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_jenis.adapter = spinnerAdapter

        spinner_jenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == items[0]) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }
        }

        //Tanggal
        val date = findViewById<EditText>(R.id.etDate)
        date.setOnClickListener{
            val tanggal = Calendar.getInstance()
            val year = tanggal.get(Calendar.YEAR)
            val month = tanggal.get(Calendar.MONTH)
            val day = tanggal.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our text view.
                    date.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }

        binding.apply {
          etNama.setText("Andi Setyo P")
            etNoHp.setText("08123456789")
            etEmail.setText("andi@gmail.com")
            etPwd.setText("123456")
            etCpawd.setText("12345")
         etKabupaten.setText("Surakarta")
            etAddress.setText("Pasar Kliwon Rt 1/7")
        }



        binding.buttonSignUp.setOnClickListener {
            //text field
            val name = binding.etNama.text.toString()
            val noHp = binding.etNoHp.text.toString()
            val email = binding.etEmail.text.toString()
            val pwd = binding.etPwd.text.toString()
            val cPwd = binding.etCpawd.text.toString()
            val tglLahir = binding.etDate.text.toString()
            val jenisK = binding.spinnerJenis.selectedItem.toString()
            val kab = binding.etKabupaten.text.toString()
            val alamat = binding.etAddress.text.toString()

            if (name.isEmpty()){
                binding.etNama.error = "Masukan Nama"
            }
            if (noHp.isEmpty()){
                binding.etNoHp.error = "Masukan No"
            }
            if (email.isEmpty()){
                binding.etEmail.error = "Masukan Email"
            }
            if (pwd.isEmpty()){
                binding.etPwd.error = "Masukan Password"
            }
            if (cPwd.isEmpty()){
                binding.etCpawd.error = "Masukan Password"
            }
            if (tglLahir.isEmpty()){
                binding.etDate.error = "Masukan Password"
            }
            if (kab.isEmpty()){
                binding.etKabupaten.error = "Masukan Kabupaten"
            }
            if (alamat.isEmpty()){
                binding.etAddress.error = "Masukan Alamat"
            }

            if (pwd!=cPwd){
                binding.etPwd.error = "Password Berbeda"
                binding.etCpawd.error = "Password Berbeda"
            }


            auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener {
                if (it.isSuccessful){
                    database = FirebaseDatabase.getInstance().reference.child("users").child(auth.currentUser!!.uid)
                    val user = Users(auth.currentUser!!.uid,
                            name,email,noHp,jenisK,tglLahir,alamat,""
                        )
                    database.setValue(user).addOnCompleteListener {
                        if (it.isSuccessful){
                            toLogin()
                        }
                    }
                }
            }
                .addOnFailureListener {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun toLogin() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.custom_dialog_submit, null)
        val tvPertama = view.findViewById<TextView>(R.id.tvPertama)
        val tvKedua = view.findViewById<TextView>(R.id.tvKedua)
        tvPertama.text = "Congratulation !"
        tvKedua.text = " You have successfully sign up"
        builder.setView(view)
        builder.setCancelable(true)
        val dialog = builder.create()
        dialog.show()

        val close = view.findViewById<Button>(R.id.btn_close)
        close.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this,LoginActivity::class.java))
        }

    }
}