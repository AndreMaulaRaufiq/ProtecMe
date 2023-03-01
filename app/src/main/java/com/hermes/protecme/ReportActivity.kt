package com.hermes.protecme

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.hermes.protecme.databinding.ActivityReportBinding
import com.hermes.protecme.model.Pelaporan
import com.hermes.protecme.sharepref.SavePref
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding
    private lateinit var timeStamp:String
    private lateinit var imgBytArray:ByteArray
    lateinit var ref: DatabaseReference
    lateinit var imgUrl:Uri
    lateinit var sharePref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //sharepref
        sharePref = getSharedPreferences(SavePref.PREF_NAME,Context.MODE_PRIVATE)

        //firebase
        ref = FirebaseDatabase.getInstance().getReference("pelaporan")

        //SPINNER
        val items = resources.getStringArray(R.array.report_array)
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
        val date = findViewById<TextView>(R.id.et_tanggal)
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
                        date.text =
                            (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
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

        //Bukti
        val image = findViewById<ImageView>(R.id.iv_bukti)
        image.setOnClickListener{
            ImagePicker.with(this)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }


        //Submit
        val submit = findViewById<Button>(R.id.btn_submit)
        submit.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.custom_dialog_submit, null)
            builder.setView(view)
            builder.setCancelable(true)
            val dialog = builder.create()

            insertPelaporan()
            dialog.show()

            val close = view.findViewById<Button>(R.id.btn_close)
            close.setOnClickListener {
                dialog.dismiss()
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "SuspiciousIndentation")
    private fun insertPelaporan(){
        timeStamp = System.currentTimeMillis().toString()
        //field text
        val jenisPelaporan = binding.spinnerJenis.selectedItem.toString()
        val judulPelecehan = binding.etJudulPelecehan.text.toString()
        val pihakBersangkutan = binding.etBersangkutan.text.toString()
        val tanggal = binding.etTanggal.text.toString()
        val kronologi = binding.etKronologi.text.toString()
        val tempat = binding.etLokasi.text.toString()

        //push firebase
        val idPel:String? = ref.push().key

        //firebase
        val refStore = FirebaseStorage.getInstance().reference.child("img/123$timeStamp")
        refStore.putBytes(imgBytArray).addOnCompleteListener { upload ->
            if (upload.isSuccessful){
                refStore.downloadUrl.addOnCompleteListener {
                    it.result?.let { url ->
                        //date
                        val sdf = SimpleDateFormat("HH:mm")
                        val jam = sdf.format(Date())
                        imgUrl = url
                        val idUser = sharePref.getString(SavePref.UID,"123").toString()
                        val pelaporan = Pelaporan(idPel,
                            idUser,
                            jenisPelaporan,
                            judulPelecehan,
                            pihakBersangkutan,
                            tanggal,
                            jam,
                            kronologi,
                            tempat,
                            url.toString(),
                            "Terkirim")
                            if (idPel != null) {
                                ref.child(idPel).setValue(pelaporan)
                            }
                    }
                }
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

                var mProfileUri = fileUri
                val image = findViewById<ImageView>(R.id.iv_bukti)
//                image.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            val imageView = findViewById<ImageView>(R.id.iv_bukti)
            val inputStream =
                uri.let { contentResolver?.openInputStream(it) }
            val imgBitmap = BitmapFactory.decodeStream(inputStream)
            val baos = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val image = baos.toByteArray()
            imgBytArray = image
            imageView.setImageURI(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}