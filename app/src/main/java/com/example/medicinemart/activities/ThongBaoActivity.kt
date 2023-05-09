package com.example.medicinemart.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicinemart.R
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewHomeAdapter
import com.example.medicinemart.adapter.RecycleViewThongBaoAdapter
import com.example.medicinemart.common.Info.customer
import com.example.medicinemart.databinding.ThongbaoBinding
import com.example.medicinemart.models.Notification
import com.example.medicinemart.retrofit.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private lateinit var binding_thong_bao: ThongbaoBinding

private var progressDialog: ProgressDialog? = null

var require_reload_data_thong_bao = false

var notification_list = ArrayList<Notification>()


fun getNotification() {
    val call = RetrofitClient.viewPagerApi.getNotification(customer.id)
    call.enqueue(object : Callback<java.util.ArrayList<JsonObject>> {
        override fun onResponse(call: Call<java.util.ArrayList<JsonObject>>, response: Response<java.util.ArrayList<JsonObject>>) {
            if (response.isSuccessful) {
                // Xử lý kết quả trả về nếu thêm hàng mới thành công
                notification_list.clear()
                for (i in response.body()!!) {
                    val id = i.getAsJsonPrimitive("id").asInt
                    val id_customer = i.getAsJsonPrimitive("id_customer").asInt
                    val title = i.getAsJsonPrimitive("title").asString
                    val content = i.getAsJsonPrimitive("content").asString
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    var _time = i.getAsJsonPrimitive("time").asString
                    val time = LocalDateTime.parse(_time, formatter)
                    val id_product = i.getAsJsonPrimitive("id_product").asInt
                    val id_order = i.getAsJsonPrimitive("id_order").asInt
                    val image = i.getAsJsonPrimitive("image").asString

                    val tmp = Notification(id, id_customer, title, content, time, id_product, id_customer, image)
                    notification_list.add(tmp)
                }
                if (require_reload_data_thong_bao) {
                    binding_thong_bao.recyclerView.adapter?.notifyDataSetChanged()
                    require_reload_data_thong_bao = false
                    progressDialog?.dismiss()
                }
            } else {
                println(response.errorBody())
                // Xử lý lỗi nếu thêm hàng mới thất bại
            }
        }

        override fun onFailure(call: Call<java.util.ArrayList<JsonObject>>, t: Throwable) {
            // Xử lý lỗi nếu không thể kết nối tới server

        }
    })
}
class ThongBaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_thong_bao = ThongbaoBinding.inflate(layoutInflater)
        setContentView(binding_thong_bao.root)

        binding_thong_bao.bottomNavigationView.setSelectedItemId(R.id.thongbao)
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.hoso -> {
                    // put your code here
                    val intent = Intent(this@ThongBaoActivity, HoSoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.donhang -> {
                    // put your code here
                    val intent = Intent(this@ThongBaoActivity, DonHangActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.home -> {
                    // put your code here
                    val intent = Intent(this@ThongBaoActivity, TrangChuActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding_thong_bao.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        val adapterRecyclerThongBao = RecycleViewThongBaoAdapter(notification_list, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {

            }
        })

        binding_thong_bao.recyclerView.layoutManager = LinearLayoutManager(this)
        val VerticalLayout = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding_thong_bao.recyclerView.setLayoutManager(VerticalLayout)
        binding_thong_bao.recyclerView.adapter = adapterRecyclerThongBao

    }

    override fun onResume() {
        super.onResume()
        if (require_reload_data_thong_bao) {
            progressDialog = ProgressDialog(this)
            progressDialog?.setCancelable(false)
            progressDialog?.setMessage("Đợi xíu...")
            progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog?.setProgress(0)
            progressDialog?.show()
            getNotification()
        }
    }

}