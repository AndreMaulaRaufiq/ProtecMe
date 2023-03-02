package com.hermes.protecme.about

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hermes.protecme.MainActivity.Companion.mainModel
import com.hermes.protecme.R
import com.hermes.protecme.databinding.ActivityEditProfileBinding
import com.hermes.protecme.sharepref.SavePref
import java.io.ByteArrayOutputStream

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var imgBytArray: ByteArray
    lateinit var sharePref: SharedPreferences

    lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        mainModel.user.observe(this) { user ->
            binding.apply {
                val value = if (user?.jenis_kelamin == "Laki-laki") 1 else 2
                etNama.setText(user?.nama)
                etNoHp.setText(user?.no_hp)
                etAddress.setText(user?.alamat)
                etDate.setText(user?.tgl_lahir)
                spinnerJenis.setSelection(value)
            }
            if (user?.url_img?.isEmpty() != true) {
                //gambar
                Glide.with(this)
                    .load(user?.url_img)
                    .into(binding.ivProfile)
            }
        }

        binding.ivProfile.setOnClickListener {
            ImagePicker.with(this)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        //firebase
        ref = FirebaseDatabase.getInstance().getReference("users")


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


        binding.btnUpdate.setOnClickListener {
            //text field
            val name = binding.etNama.text.toString()
            val noHp = binding.etNoHp.text.toString()
            val tglLahir = binding.etDate.text.toString()
            val jenisK = binding.spinnerJenis.selectedItem.toString()
            val alamat = binding.etAddress.text.toString()

            if (name.isEmpty()) {
                binding.etNama.error = "Masukan Nama"
            }
            if (noHp.isEmpty()) {
                binding.etNoHp.error = "Masukan No"
            }
            if (tglLahir.isEmpty()) {
                binding.etDate.error = "Masukan Password"
            }
            if (alamat.isEmpty()) {
                binding.etAddress.error = "Masukan Alamat"
            }

            mainModel.user.observe(this) { user ->
                val cUser = user
                cUser?.nama = name
                cUser?.no_hp = noHp
                cUser?.tgl_lahir = tglLahir
                cUser?.jenis_kelamin = jenisK
                cUser?.alamat = alamat
                ref.child(user?.id.toString()).setValue(cUser)
                showDialog()
            }
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                val inputStream =
                    fileUri.let { contentResolver?.openInputStream(it) }
                val imgBitmap = BitmapFactory.decodeStream(inputStream)
                val baos = ByteArrayOutputStream()
                imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val image = baos.toByteArray()
                imgBytArray = image
                //firebase
                sharePref = getSharedPreferences(SavePref.PREF_NAME, Context.MODE_PRIVATE)
                val uid = sharePref.getString(SavePref.UID, "123").toString()
                val refStore = FirebaseStorage.getInstance().reference.child("img/uid")
                refStore.putBytes(imgBytArray).addOnCompleteListener { upload ->
                    if (upload.isSuccessful) {
                        refStore.downloadUrl.addOnCompleteListener {
                            it.result.let { url ->
                                mainModel.user.observe(this) { user ->
                                    val cloneUser = user
                                    cloneUser?.url_img = url.toString()
                                    ref.child(uid).setValue(cloneUser)
                                }

                            }
                        }

                    }
                }

                binding.ivProfile.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.custom_dialog_submit, null)
        view.findViewById<TextView>(R.id.tvPertama).text = "Congratulation !"
        view.findViewById<TextView>(R.id.tvKedua).text = "Profil anda telah terupdate!!"
        builder.setView(view)
        builder.setCancelable(true)
        val dialog = builder.create()
        dialog.show()
        val close = view.findViewById<Button>(R.id.btn_close)
        close.setOnClickListener {
            dialog.dismiss()
        }
    }
}