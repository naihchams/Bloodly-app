/*package com.example.bloodly.homepage.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.bloodly.R
import com.example.bloodly.appcomponents.base.BaseActivity
import com.example.bloodly.databinding.ActivityMainScreenBinding
import com.example.bloodly.homepage.`data`.viewmodel.HomepageVM
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomepageActivity : BaseActivity<ActivityMainScreenBinding>(R.layout.activity_main_screen) {
  private val viewModel: HomepageVM by viewModels()
  private lateinit var fusedLocationClient: FusedLocationProviderClient

  override fun onInitialized() {
    viewModel.navArguments = intent.extras?.getBundle("bundle")
    binding.homepageVM = viewModel

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    getLocation()
  }

  override fun setUpClicks() {
    // Add your click listeners here
  }

  private fun getLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    } else {
      fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
          val geocoder = Geocoder(this)
          val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
          if (addresses != null) {
            if (addresses.isNotEmpty()) {
                  updateCityInViewModel(addresses.first().locality)
            }
          }
        }
      }
    }
  }

  private fun updateCityInViewModel(cityName: String?) {
    cityName?.let {
      viewModel.homepageModel.value?.txtToronto = it
      viewModel.homepageModel.postValue(viewModel.homepageModel.value)
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      getLocation()
    }
  }
}*/