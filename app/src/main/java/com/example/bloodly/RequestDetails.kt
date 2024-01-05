package com.example.bloodly

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.bloodly.models.Request
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class RequestDetails : AppCompatActivity(), PermissionListener {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_details)
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var requestModel = intent.extras?.getSerializable("requestModel") as? Request
        if (requestModel == null) {
            finish()
            return
        }


        val txt_name: TextView = findViewById(R.id.txt_name)
        val txt_BloodGrp: TextView = findViewById(R.id.txt_BloodGrp)
        val txt_address: TextView = findViewById(R.id.txt_address)
        val txt_hospital: TextView = findViewById(R.id.txt_hospital)
        val txt_location: TextView = findViewById(R.id.txt_location)
        val txt_neededBloodGrp: TextView = findViewById(R.id.txt_neededBloodGrp)
        val txt_neededUnit: TextView = findViewById(R.id.txt_neededUnit)
        val txt_patiantName: TextView = findViewById(R.id.txt_patiantName)
        val txt_phone: TextView = findViewById(R.id.txt_phone)
        val btn_call: Button = findViewById(R.id.btn_call)


        txt_name.text="Name : "+requestModel.userName
        txt_BloodGrp.text="Blood Group : "+requestModel.userBloodGrp
        txt_address.text="Address : "+requestModel.userLocation
        txt_hospital.text="Request From : "+requestModel.hospital
        txt_location.text="Location Of Hospital : "+requestModel.userLocation
        txt_neededBloodGrp.text="Needed BloodGroup : "+requestModel.userBloodGrp
        txt_neededUnit.text="Needed No of Unit : "+requestModel.unitNeeded
        txt_patiantName.text="Patient Name : "+requestModel.userName
        txt_phone.text="Phone No : "+requestModel.userPhone

        btn_call.setOnClickListener {
            callPaisent(requestModel.userPhone.toString())
        }

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CALL_PHONE)
            .withListener(this)
            .check()

    }

    fun callPaisent(number:String){
        val intent = Intent(Intent.ACTION_CALL);
        intent.data = Uri.parse("tel:$number")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        token?.continuePermissionRequest()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
    }

}