package com.example.medicinemart.activities

import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicinemart.R
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewThongBaoAdapter
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.customer
import com.example.medicinemart.databinding.ThongbaoBinding
import com.example.medicinemart.models.Notification
import com.example.medicinemart.retrofit.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private lateinit var binding_thong_bao: ThongbaoBinding

private var progressDialog: ProgressDialog? = null

var require_reload_data_thong_bao = false

var notification_list = ArrayList<Notification>()

fun getNotification() {
    val call = RetrofitClient.viewPagerApi.getNotification(customer.id)
    call.enqueue(object : Callback<java.util.ArrayList<JsonObject>> {
        override fun onResponse(
            call: Call<java.util.ArrayList<JsonObject>>,
            response: Response<java.util.ArrayList<JsonObject>>
        ) {
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

                    val tmp = Notification(
                        id,
                        id_customer,
                        title,
                        content,
                        time,
                        id_product,
                        id_customer,
                        image
                    )
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

        var badge = binding_thong_bao.bottomNavigationView.getOrCreateBadge(R.id.thongbao)
        if (Info.so_thong_bao_chua_doc == 0) {
            badge.isVisible = false
        } else {
            badge.isVisible = true
            badge.number = Info.so_thong_bao_chua_doc
        }

        /*

        val barLauncher = registerForActivityResult(ScanContract()) { result ->
            if (result.contents != null) {
                // Xử lý kết quả quét mã vạch ở đây
                var query = ""
                for (product in Info.all_product) {
                    println(product.barcode + "barcode p")
                    val originalString = product.barcode
                    val nonAccentString = originalString.removeAccent()
                    if (nonAccentString.contains(result.contents.toString(), true)) {
                        productSearchList.add(product)
                        query = product.name
                        break
                    }
                }
                val intent = Intent(this@ThongBaoActivity, SearchActivity::class.java).apply {
                    putExtra("text_search", query)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
            }
        }

        binding_thong_bao.tvThongbao.setOnClickListener() {
            val options = ScanOptions()
            options.setOrientationLocked(false)
            options.setBeepEnabled(true)
            options.setOrientationLocked(true)
            options.setCaptureActivity(CaptureAct::class.java)
            barLauncher.launch(options)
        }  */


        if (notification_list.isEmpty()) {
            binding_thong_bao.empty.visibility = View.VISIBLE
            binding_thong_bao.lottie.playAnimation()
        } else {
            binding_thong_bao.empty.visibility = View.GONE
        }

        if (Info.products_in_cart.isEmpty()) {
            binding_thong_bao.quantityInCart.visibility = View.GONE
        } else {
            binding_thong_bao.quantityInCart.visibility = View.VISIBLE
            binding_thong_bao.quantityInCart.text = Info.products_in_cart.size.toString()
        }

        binding_thong_bao.btnCart.setOnClickListener() {
            val animation = AnimationUtils.loadAnimation(this, R.anim.btn_anim)
            binding_thong_bao.btnCart.startAnimation(animation)
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
        }

        binding_thong_bao.bottomNavigationView.setSelectedItemId(R.id.thongbao)
        val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.hoso -> {
                        // put your code here
                        val intent = Intent(this@ThongBaoActivity, HoSoActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                        return@OnNavigationItemSelectedListener true
                    }

                    R.id.donhang -> {
                        // put your code here
                        val intent = Intent(this@ThongBaoActivity, DonHangActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.home -> {
                        // put your code here
                        val intent = Intent(this@ThongBaoActivity, TrangChuActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

        binding_thong_bao.bottomNavigationView.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )


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

        Info.doubleBackPressed = false
        binding_thong_bao.bottomNavigationView.setSelectedItemId(R.id.thongbao)

        if (require_reload_data_thong_bao) {
            progressDialog = ProgressDialog(this)
            progressDialog?.setCancelable(false)
            progressDialog?.setMessage("Đợi xíu...")
            progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog?.setProgress(0)
            progressDialog?.show()
            getNotification()
        }

        if (notification_list.isEmpty()) {
            binding_thong_bao.empty.visibility = View.VISIBLE
            binding_thong_bao.lottie.playAnimation()
        } else {
            binding_thong_bao.empty.visibility = View.GONE
        }

        if (Info.products_in_cart.isEmpty()) {
            binding_thong_bao.quantityInCart.visibility = View.GONE
        } else {
            binding_thong_bao.quantityInCart.visibility = View.VISIBLE
            binding_thong_bao.quantityInCart.text = Info.products_in_cart.size.toString()
        }

        Info.so_thong_bao_chua_doc = 0
        var badge = binding_thong_bao.bottomNavigationView.getOrCreateBadge(R.id.thongbao)
        badge.isVisible = false

    }
    override fun onBackPressed() {
        if (Info.doubleBackPressed) {
            super.onBackPressed()
            finishAffinity()
            return
        }

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.getRunningTasks(1) // Lấy danh sách task chạy hiện tại

//        if (tasks.isNotEmpty() && tasks[0].numActivities == 1) {
        // Chỉ có một activity duy nhất trong stack
        Info.doubleBackPressed = true
        Toast.makeText(this, "Chạm lần nữa để thoát", Toast.LENGTH_SHORT).show()
//        } else {
//            super.onBackPressed()
//        }

        doubleBackToExitHandler.postDelayed({
            Info.doubleBackPressed = false
        }, Info.doubleBackDelay)
    }



}