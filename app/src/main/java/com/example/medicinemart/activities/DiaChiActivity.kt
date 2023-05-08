package com.example.medicinemart.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewAddressAdapter
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.list_address
import com.example.medicinemart.databinding.DiachiBinding
import com.example.medicinemart.models.Address
import com.example.medicinemart.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

public lateinit var binding_dia_chi: DiachiBinding
//private lateinit var mapView: MapView
//private lateinit var googleMap: GoogleMap
//private lateinit var centerMarker: ImageView

var require_reload_data_address = false
private var progressDialog: ProgressDialog? = null



private var themdiachi = "themdiachi"
private var chitietdiachi = "chitietdiachi"


fun getAddressFromDB() {
    val call = RetrofitClient.viewPagerApi.getAddress(Info._username)
    call.enqueue(object : Callback<java.util.ArrayList<Address>> {
        override fun onResponse(call: Call<java.util.ArrayList<Address>>, response: Response<java.util.ArrayList<Address>>) {
            if (response.isSuccessful) {
                // Xử lý kết quả trả về nếu thêm hàng mới thành công
                if (progressDialog != null) {
                    progressDialog?.dismiss()
                    progressDialog = null
                }
                list_address.clear()
                for (i in response.body()!!) {
                    val id = i.id
                    val username = i.username
                    val fullname = i.full_name
                    val phone = i.phone
                    val td_x = i.td_x
                    val td_y = i.td_y
                    val location = i.location
                    val tmp = Address(id, phone, username, fullname, td_x, td_y, location)
                    list_address.add(tmp)
                }
                if (require_reload_data_address == true) {
                    binding_dia_chi.recyclerView.adapter?.notifyDataSetChanged()
                    require_reload_data_address = false
                    checkList(list_address)
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
}

fun checkList(list: ArrayList<Address>) {
    println("checklist haot dong")
    if (list.isEmpty()) {
        binding_dia_chi.layoutEmpty.visibility = View.VISIBLE
        binding_dia_chi.recyclerView.visibility = View.INVISIBLE

    } else {
        binding_dia_chi.layoutEmpty.visibility = View.INVISIBLE
        binding_dia_chi.recyclerView.visibility = View.VISIBLE
    }
}

class DiaChiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_dia_chi = DiachiBinding.inflate(layoutInflater)
        setContentView(binding_dia_chi.root)

        checkList(list_address)

        val adapterRecyclerAddressAdapter= RecycleViewAddressAdapter(list_address, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
                val intent = Intent(this@DiaChiActivity, ChiTietDiaChiActivity::class.java)
                Info.position = position
                intent.putExtra("position", Info.position as Serializable)

                startActivity(intent)
                Animatoo.animateSlideLeft(this@DiaChiActivity)
            }
        })

//        binding_gio_hang.recyclerView.isNestedScrollingEnabled = true
        binding_dia_chi.recyclerView.layoutManager = LinearLayoutManager(this)

        val VerticalLayout = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding_dia_chi.recyclerView.setLayoutManager(VerticalLayout)
        binding_dia_chi.recyclerView.adapter = adapterRecyclerAddressAdapter

        binding_dia_chi.btnThemdiachi.setOnClickListener() {
            val intent = Intent(this, DiaChiMoiActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        binding_dia_chi.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        if (require_reload_data_address) {
//            progressDialog = ProgressDialog(this)
//            progressDialog?.setCancelable(false)
//            progressDialog?.setMessage("Đợi xíu...")
//            progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//            progressDialog?.setProgress(0)
//            progressDialog?.show()
            getAddressFromDB()
        }
    }
}