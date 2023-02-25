package com.hermes.protecme

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.github.dhaval2404.imagepicker.ImagePicker
import java.util.*

class ReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

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
        val date = findViewById<TextView>(R.id.tv_tanggal)
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
            dialog.show()

            val close = view.findViewById<Button>(R.id.btn_close)
            close.setOnClickListener {
                dialog.dismiss()
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
                image.setImageURI(fileUri)
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
            val image = findViewById<ImageView>(R.id.iv_bukti)
            image.setImageURI(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}