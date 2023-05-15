package com.example.medicinemart.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.R
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info._username
import com.example.medicinemart.common.Info.id_address_max_in_db
import com.example.medicinemart.common.Info.location_default
import com.example.medicinemart.databinding.DiachimoiBinding
import com.example.medicinemart.models.Address
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

private lateinit var binding_dia_chi_moi: DiachimoiBinding
private lateinit var mapView: MapView
private lateinit var googleMap: GoogleMap
private lateinit var location: LatLng

private var progressDialog: ProgressDialog? = null


private var themdiachi = "themdiachi"


class DiaChiMoiActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_dia_chi_moi = DiachimoiBinding.inflate(layoutInflater)
        setContentView(binding_dia_chi_moi.root)

        val goto = intent.getSerializableExtra("goto") as String

//        Info.address.td_x = Info.location_default.latitude.toBigDecimal()
//        Info.address.td_y = Info.location_default.longitude.toBigDecimal()
//        val td_x = intent.getStringExtra("td_x")
//        val td_y = intent.getStringExtra("td_y")

        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_dia_chi_moi.edtHoten.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_dia_chi_moi.edtHoten.windowToken, 0)
                binding_dia_chi_moi.edtHoten.clearFocus()
            } else {
                if (binding_dia_chi_moi.edtHoten.text.toString() == "Không được để trống!") {
                    binding_dia_chi_moi.edtHoten.text.clear()
                    binding_dia_chi_moi.edtHoten.setTextColor(Color.BLACK)
                }
            }
        }

        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_dia_chi_moi.edtSdt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_dia_chi_moi.edtSdt.windowToken, 0)
                binding_dia_chi_moi.edtSdt.clearFocus()
            } else {
                if (binding_dia_chi_moi.edtSdt.text.toString() == "Không được để trống!") {
                    binding_dia_chi_moi.edtSdt.text.clear()
                    binding_dia_chi_moi.edtSdt.setTextColor(Color.BLACK)
                }
            }
        }

        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_dia_chi_moi.edtDiachi.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_dia_chi_moi.edtDiachi.windowToken, 0)
                binding_dia_chi_moi.edtDiachi.clearFocus()
            } else {
                if (binding_dia_chi_moi.edtDiachi.text.toString() == "Không được để trống!") {
                    binding_dia_chi_moi.edtDiachi.text.clear()
                    binding_dia_chi_moi.edtDiachi.setTextColor(Color.BLACK)
                }
            }
        }

        mapView = binding_dia_chi_moi.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        binding_dia_chi_moi.btnHoanthanh.setOnClickListener() {
//            val centerLatLng = googleMap.cameraPosition.target
//            println(centerLatLng.latitude)
//            println(centerLatLng.longitude)
//
//            val geocoder = Geocoder(this, Locale.getDefault())
//            val addresses = geocoder.getFromLocation(centerLatLng.latitude, centerLatLng.longitude, 1)
//            val address = addresses?.get(0)?.getAddressLine(0)
//            println(address)

            val hoten = binding_dia_chi_moi.edtHoten.text
            val sdt = binding_dia_chi_moi.edtSdt.text
            val diachi = binding_dia_chi_moi.edtDiachi.text

            if (hoten.isNullOrEmpty() || (hoten.toString() == "Không được để trống!")) {
                binding_dia_chi_moi.edtHoten.text =
                    Editable.Factory.getInstance().newEditable("Không được để trống!")
                binding_dia_chi_moi.edtHoten.setTextColor(Color.RED)
            }
            if (sdt.isNullOrEmpty() || (sdt.toString() == "Không được để trống!")) {
                binding_dia_chi_moi.edtSdt.text =
                    Editable.Factory.getInstance().newEditable("Không được để trống!")
                binding_dia_chi_moi.edtSdt.setTextColor(Color.RED)
            }
            if (diachi.isNullOrEmpty() || (diachi.toString() == "Không được để trống!")) {
                binding_dia_chi_moi.edtDiachi.text =
                    Editable.Factory.getInstance().newEditable("Không được để trống!")
                binding_dia_chi_moi.edtDiachi.setTextColor(Color.RED)
            }

            if (!hoten.isNullOrEmpty() && !sdt.isNullOrEmpty() && !diachi.isNullOrEmpty() && (hoten.toString() != "Không được để trống!") && (sdt.toString() != "Không được để trống!") && (diachi.toString() != "Không được để trống!")) {
                if (Info.td_x == BigDecimal(0.00)) {
                    Info.td_x = location_default.latitude.toBigDecimal()
                    Info.td_y = location_default.longitude.toBigDecimal()
                }
                val id = id_address_max_in_db + 1
//                list_address.get(list_address.size - 1).id + 1
                val s = Address(
                    id,
                    sdt.toString(),
                    _username,
                    hoten.toString(),
                    Info.td_x,
                    Info.td_y,
                    diachi.toString()
                )
//                list_address.add(s)
                val call = RetrofitClient.viewPagerApi.insertAddress(
                    s.username, s.full_name, s.phone, s.td_x, s.td_y, s.location
                )
                progressDialog = ProgressDialog(this)
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage("Đợi xíu...")
                progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progressDialog?.setProgress(0)
                progressDialog?.show()
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>, response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            // Xử lý kết quả trả về nếu thêm hàng mới thành công

                            val call = RetrofitClient.viewPagerApi.getAddress(Info._username)
                            call.enqueue(object : Callback<java.util.ArrayList<Address>> {
                                override fun onResponse(call: Call<java.util.ArrayList<Address>>, response: Response<java.util.ArrayList<Address>>) {
                                    if (response.isSuccessful) {
                                        // Xử lý kết quả trả về nếu thêm hàng mới thành công
                                        Info.list_address.clear()
                                        for (i in response.body()!!) {
                                            val id = i.id
                                            val username = i.username
                                            val fullname = i.full_name
                                            val phone = i.phone
                                            val td_x = i.td_x
                                            val td_y = i.td_y
                                            val location = i.location
                                            val tmp = Address(id, phone, username, fullname, td_x, td_y, location)
                                            Info.list_address.add(tmp)
                                        }

                                    } else {
                                        println(response.errorBody())
                                        // Xử lý lỗi nếu thêm hàng mới thất bại
                                    }
                                }

                                override fun onFailure(call: Call<java.util.ArrayList<Address>>, t: Throwable) {
                                    // Xử lý lỗi nếu không thể kết nối tới server
                                }
                            })

                            require_reload_data_address = true
                            progressDialog?.dismiss()
                            val builder = AlertDialog.Builder(this@DiaChiMoiActivity)
                            builder.setCancelable(false)

                            val view = layoutInflater.inflate(R.layout.dialog_success, null)
                            val closeButton = view.findViewById<Button>(R.id.btn_ok)
                            view.findViewById<TextView>(R.id.message).text = "Đã thêm địa chỉ mới."


                            builder.setView(view)
                            val dialog = builder.create()
                            dialog.show()

                            closeButton.setOnClickListener {
                                dialog.dismiss()
                                if (goto == "diachi") {
                                    onBackPressed()
                                } else {
                                    val intent = Intent(this@DiaChiMoiActivity, CartActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                                    finish()
                                }
                            }

                        } else {
                            // Xử lý lỗi nếu thêm hàng mới thất bại
                            println("fail dia chi moi")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Xử lý lỗi nếu không thể kết nối tới server
                    }
                })
//                id_address_max_in_db++
//                Info.position = -1
//                Info.td_x = BigDecimal(0.00)
//                Info.td_y = BigDecimal(0.00)
            }

        }


        binding_dia_chi_moi.btnBack.setOnClickListener {
//            Info.position = -1
//            Info.td_x = BigDecimal(0.00)
//            Info.td_y = BigDecimal(0.00)
            val anim = AnimationUtils.loadAnimation(this, R.anim.btn_back)
            binding_dia_chi_moi.btnBack.startAnimation(anim)
            onBackPressed()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true

        googleMap.getUiSettings().setAllGesturesEnabled(false)
        googleMap.clear()

        if (Info.td_x == BigDecimal(0.00)) {
            location = location_default
        } else {
            location = LatLng(Info.td_x.toDouble(), Info.td_y.toDouble())
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 19f))

        var markerOptions = MarkerOptions().position(location).title("")
        googleMap.addMarker(markerOptions)

        googleMap.setOnMapClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("code", themdiachi as Serializable)
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