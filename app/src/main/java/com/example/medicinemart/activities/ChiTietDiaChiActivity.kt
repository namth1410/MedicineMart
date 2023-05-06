package com.example.medicinemart.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.list_address
import com.example.medicinemart.databinding.ChitietdiachiBinding
import com.example.medicinemart.retrofit.RetrofitClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.math.BigDecimal

private lateinit var binding_chi_tiet_dia_chi: ChitietdiachiBinding
private lateinit var mapView: MapView
private lateinit var googleMap: GoogleMap

//private lateinit var centerMarker: ImageView
private var chitietdiachi = "chitietdiachi"

class ChiTietDiaChiActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_chi_tiet_dia_chi = ChitietdiachiBinding.inflate(layoutInflater)
        setContentView(binding_chi_tiet_dia_chi.root)

        val position = intent.getSerializableExtra("position") as Int
        Info.position = position

        Info.td_x = list_address.get(position).td_x
        Info.td_y = list_address.get(position).td_y
//        Info.position = position
//        binding_chi_tiet_dia_chi.edtHoten.text =
//            Editable.Factory.getInstance().newEditable(address.full_name)
//        binding_chi_tiet_dia_chi.edtSdt.text =
//            Editable.Factory.getInstance().newEditable(address.phone)
//        binding_chi_tiet_dia_chi.edtDiachi.text =
//            Editable.Factory.getInstance().newEditable(address.location)

        binding_chi_tiet_dia_chi.edtHoten.text =
            Editable.Factory.getInstance().newEditable(list_address.get(position).full_name)
        binding_chi_tiet_dia_chi.edtSdt.text =
            Editable.Factory.getInstance().newEditable(list_address.get(position).phone)
        binding_chi_tiet_dia_chi.edtDiachi.text =
            Editable.Factory.getInstance().newEditable(list_address.get(position).location)

        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_chi_tiet_dia_chi.edtHoten.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_chi_tiet_dia_chi.edtHoten.windowToken, 0)
                binding_chi_tiet_dia_chi.edtHoten.clearFocus()
            } else {

            }
        }

        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_chi_tiet_dia_chi.edtSdt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_chi_tiet_dia_chi.edtSdt.windowToken, 0)
                binding_chi_tiet_dia_chi.edtSdt.clearFocus()
            } else {

            }
        }

        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_chi_tiet_dia_chi.edtDiachi.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_chi_tiet_dia_chi.edtDiachi.windowToken, 0)
                binding_chi_tiet_dia_chi.edtDiachi.clearFocus()
            } else {

            }
        }

        binding_chi_tiet_dia_chi.btnHoanthanh.setOnClickListener() {
            list_address.get(position).full_name = binding_chi_tiet_dia_chi.edtHoten.text.toString()
            list_address.get(position).phone = binding_chi_tiet_dia_chi.edtSdt.text.toString()
            list_address.get(position).location = binding_chi_tiet_dia_chi.edtDiachi.text.toString()
            list_address.get(position).td_x = Info.td_x
            list_address.get(position).td_y = Info.td_y
            val call = RetrofitClient.viewPagerApi.updateAddress(
                list_address.get(position).id,
                list_address.get(position).full_name,
                list_address.get(position).phone,
                list_address.get(position).td_x,
                list_address.get(position).td_y,
                list_address.get(position).location
            )
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    // Xử lý kết quả trả về từ API nếu cần
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Xử lý lỗi nếu có
                }
            })
            Info.position = -1
            Info.td_x = BigDecimal(0.00)
            Info.td_y = BigDecimal(0.00)
            onBackPressed()
        }

        binding_chi_tiet_dia_chi.btnXoadiachi.setOnClickListener() {
            println(list_address.get(Info.position).id)
            val dialogBuilder = AlertDialog.Builder(this)
                .setMessage("Xóa địa chỉ?")
                .setPositiveButton("Xóa") { _, _ ->
                    val call =
                        RetrofitClient.viewPagerApi.deleteAddress(list_address.get(Info.position).id)
                    call.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                println("Xóa địa chỉ thành công")
                                // Xóa thành công
                            } else {
                                // Xóa không thành công
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            // Xóa không thành công do lỗi mạng hoặc lỗi server
                        }
                    })
                    list_address.removeAt(Info.position)
                    Info.position = -1
                    Info.td_x = BigDecimal(0.00)
                    Info.td_y = BigDecimal(0.00)
                    onBackPressed()
                }
                .setNegativeButton("Không") { _, _ ->
                    // Xử lý khi người dùng chọn No
                }.create()

            dialogBuilder.show()
        }

        mapView = binding_chi_tiet_dia_chi.mapView
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

//        binding_chi_tiet_dia_chi.btnXacnhan.setOnClickListener() {
//            val centerLatLng = googleMap.cameraPosition.target
//            println(centerLatLng.latitude)
//            println(centerLatLng.longitude)
//
//            val geocoder = Geocoder(this, Locale.getDefault())
//            val addresses = geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1)
//            val address = addresses?.get(0)?.getAddressLine(0)
//            println(address)
//        }

        binding_chi_tiet_dia_chi.btnBack.setOnClickListener {
            Info.position = -1
            Info.td_x = BigDecimal(0.00)
            Info.td_y = BigDecimal(0.00)
            onBackPressed()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true

        googleMap.getUiSettings().setAllGesturesEnabled(false)
        googleMap.clear()

        val location = LatLng(
            Info.td_x.toDouble(),
            Info.td_y.toDouble()
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 19f))

        var markerOptions = MarkerOptions().position(location).title("Vị trí của bạn")
        googleMap.addMarker(markerOptions)

        googleMap.setOnMapClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("code", chitietdiachi as Serializable)

            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        mapView.getMapAsync(this)
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