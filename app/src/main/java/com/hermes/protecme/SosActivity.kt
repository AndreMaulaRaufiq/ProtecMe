package com.hermes.protecme

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import com.hermes.protecme.databinding.ActivitySosBinding
import com.hermes.protecme.model.Sos
import com.hermes.protecme.sharepref.SavePref

class SosActivity : AppCompatActivity() {
    private var handlerAnimation = Handler(Looper.getMainLooper())
    private lateinit var binding:ActivitySosBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private  val permissionId = 101
    lateinit var mainHandler:Handler
    private var isLocationStart = true
    lateinit var sharePref: SharedPreferences
    lateinit var idUser:String
    lateinit var ref:DatabaseReference
    lateinit var refIdUser:Query


    private val updateLokasi = object : Runnable {
        override fun run() {
            getLokasi()
            mainHandler.postDelayed(this, 15000)
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //animasi pulse
        startPulse()



        //sharepref
        sharePref = getSharedPreferences(SavePref.PREF_NAME,Context.MODE_PRIVATE)
        idUser = sharePref.getString(SavePref.UID,"123").toString()



        //firebase
        ref = FirebaseDatabase.getInstance().getReference("sos")
        refIdUser = ref.orderByChild("id_user").equalTo(idUser)

        //evetn every 15s get lokasi
        mainHandler = Handler(Looper.getMainLooper())

        //stop get lokasi
        binding.button.setOnClickListener {
            stopGetLokasi()
            Toast.makeText(this,"Location Stoped",Toast.LENGTH_SHORT).show()
        }
        //start get lokasi again
        binding.imgAnimation1.setOnClickListener {
            if(isLocationStart){
                Toast.makeText(this,"Location Has Started",Toast.LENGTH_SHORT).show()
            }else{
                mainHandler.post(updateLokasi)
                startPulse()
            }
        }

    }

    private fun stopGetLokasi() {
        mainHandler.removeCallbacks(updateLokasi)
        updateStatusFalse()
        stopPulse()
        isLocationStart = false
    }

    private fun updateStatusFalse(){
        refIdUser.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(value:DataSnapshot in snapshot.children){
                        val sos:Sos? = value.getValue(Sos::class.java)
                        sos?.status = false
                        val id = sos?.id
                        if (id != null) {
                            ref.child(id).setValue(sos)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getLokasi() {

        //timestamp
        val currentTimestamp = System.currentTimeMillis()
        Log.e("TIMESTAMP",currentTimestamp.toString())
        isLocationStart = true
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionId)

            return
        }
        //inisiasi lokasi
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location:Location? ->
            if (location != null) {

                refIdUser.addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for(value:DataSnapshot in snapshot.children){
                                val sos:Sos? = value.getValue(Sos::class.java)
                                sos?.latitude = location.latitude.toString()
                                sos?.longitude = location.longitude.toString()
                                sos?.status = true
                                sos?.timeStamp = currentTimestamp
                                val id = sos?.id
                                if (id != null) {
                                    ref.child(id).setValue(sos)
                                    Log.e("ANDI GANTENG","Data Updated:  ")
                                }
                            }
                        }else{
                            val idSos:String? = ref.push().key
                            val sos = Sos(idSos,idUser,location.latitude.toString(),location.longitude.toString(),currentTimestamp,true)
                            if (idSos != null) {
                                ref.child(idSos).setValue(sos)
                                Log.e("ANDI GANTENG","Data created: "+location.latitude.toString())
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


            }else{
                Log.e("ANDI GANTENG","LOKASI NULL ")
            }
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //permission granted

            }else {
                // permission denied
                Toast.makeText(this, "You need to grant permission to access location",
                    Toast.LENGTH_SHORT).show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateLokasi)
        updateStatusFalse()

    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateLokasi)
    }


    private fun startPulse() {
        runnable.run()
    }
    private fun stopPulse() {
        handlerAnimation.removeCallbacks(runnable)
    }
    private var runnable = object : Runnable {
        override fun run() {
            binding.imgAnimation1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000)
                .withEndAction {
                    binding.apply {
                        imgAnimation1.scaleX = 1f
                        imgAnimation1.scaleY = 1f
                        imgAnimation1.alpha = 1f
                    }
                }
            binding.imgAnimation2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700)
                .withEndAction {
                    binding.apply {
                        imgAnimation2.scaleX = 1f
                        imgAnimation2.scaleY = 1f
                        imgAnimation2.alpha = 1f
                    }
                }
            handlerAnimation.postDelayed(this, 1500)
        }
    }


}