package com.hermes.protecme

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hermes.protecme.databinding.ActivitySosBinding

class SosActivity : AppCompatActivity() {
    private var handlerAnimation = Handler(Looper.getMainLooper())
    private lateinit var binding:ActivitySosBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private  val permissionId = 101
    lateinit var mainHandler:Handler
    lateinit var runner:Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySosBinding.inflate(layoutInflater)
        setContentView(binding.root)

         mainHandler = Handler(Looper.getMainLooper())

        runner = object : Runnable {
            override fun run() {
                getLokasi()
                mainHandler.postDelayed(this, 15000)
            }
        }

        mainHandler.post(runner)

        startPulse()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


    }

    private fun getLokasi() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionId);
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location:Location? ->
            if (location != null) {
                Log.e("ANDI GANTENG","LATITUDE: "+location.latitude.toString())
            }else{
                Log.e("ANDI GANTENG","LOKASI NULL ")
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //permission granted
//                getLocation()
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
        mainHandler.removeCallbacks(runner)
    }


    private fun startPulse() {
        runnable.run()
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