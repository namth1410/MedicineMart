package com.example.medicinemart.activities

import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.list_address
import com.example.medicinemart.common.Info.position
import com.example.medicinemart.databinding.MapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

private lateinit var binding_map: MapBinding
private lateinit var mapView: MapView
private lateinit var googleMap: GoogleMap
private lateinit var location: LatLng

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_map = MapBinding.inflate(layoutInflater)
        setContentView(binding_map.root)

        val message = intent.getStringExtra("code")
        if (message == "themdiachi") {
            location = Info.location_default
        } else if (message == "chitietdiachi") {
            location = LatLng(
                list_address.get(position).td_x.toDouble(),
                list_address.get(position).td_y.toDouble()
            )
        }


        println(position)
        mapView = binding_map.mapView
        mapView.onCreate(savedInstanceState)
//        centerMarker = ImageView(this)
//        centerMarker.setImageResource(R.drawable.baseline_location)
//        val layoutParams = FrameLayout.LayoutParams(
//            resources.getDimensionPixelSize(R.dimen.center_marker_size),
//            resources.getDimensionPixelSize(R.dimen.center_marker_size)
//        )
//        centerMarker.layoutParams = layoutParams

//        mapView.addView(centerMarker)

        mapView.getMapAsync(this)

        binding_map.btnXacnhan.setOnClickListener() {
            val centerLatLng = googleMap.cameraPosition.target
            val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
            val addresses =
                geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1)

            Info.td_x = centerLatLng.latitude.toBigDecimal()
            Info.td_y = centerLatLng.longitude.toBigDecimal()

            println(Info.td_x)
            println(Info.td_y)
//            val address = addresses?.get(0)?.getAddressLine(0)
//            intent.putExtra("td_x", centerLatLng.latitude.toBigDecimal())
//            intent.putExtra("td_y", centerLatLng.longitude.toBigDecimal())
//            println(address)
            onBackPressed()
        }

        binding_map.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true

        googleMap.clear()

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 19f))

        var markerOptions = MarkerOptions().position(location).title("Vị trí của bạn")
        googleMap.addMarker(markerOptions)


        // Xử lý cái dấu đỏ khi mà map đứng yên
        googleMap.setOnCameraIdleListener {
            val centerLatLng = googleMap.cameraPosition.target
            val centerPoint = googleMap.projection.toScreenLocation(centerLatLng)

            markerOptions = MarkerOptions().position(centerLatLng).title("Vị trí của bạn")
            googleMap.clear()
            googleMap.addMarker(markerOptions)

//            centerMarker.translationX = centerPoint.x - centerMarker.width / 2f
//            centerMarker.translationY = centerPoint.y - centerMarker.height / 2f
        }

        // Xử lý cái dấu đỏ khi người dùng di chuyển map
        googleMap.setOnCameraMoveListener {
            val centerLatLng = googleMap.cameraPosition.target
            val centerPoint = googleMap.projection.toScreenLocation(centerLatLng)

            markerOptions = MarkerOptions().position(centerLatLng).title("Vị trí của bạn")
            googleMap.clear()
            googleMap.addMarker(markerOptions)

//            centerMarker.translationX = centerPoint.x - centerMarker.width / 2f
//            centerMarker.translationY = centerPoint.y - centerMarker.height / 2f
        }

//        googleMap.setOnMapClickListener { latLng ->
//            println("Clicked on: ${latLng.latitude}, ${latLng.longitude}")
//            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18f)
//            googleMap.animateCamera(cameraUpdate)
//
//            markerOptions.position(latLng)
//            googleMap.clear() // Xóa marker cũ
//            googleMap.addMarker(markerOptions) // Thêm marker mới
//        }


    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}