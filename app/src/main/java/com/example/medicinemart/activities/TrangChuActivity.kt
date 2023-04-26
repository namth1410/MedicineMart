package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.medicinemart.R
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewHomeAdapter
import com.example.medicinemart.adapter.ViewPagerAdapter
import com.example.medicinemart.databinding.TrangchuBinding
import com.example.medicinemart.models.BannerAds
import com.example.medicinemart.models.Sanpham
import com.example.medicinemart.retrofit.RetrofitClient.viewPagerApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import java.util.*

private lateinit var binding_trang_chu: TrangchuBinding
private lateinit var viewPagerAdapter: ViewPagerAdapter
private lateinit var viewPager: ViewPager

private lateinit var handler: Handler
private var timer: Timer? = null
private var page = 0
private var delay: Long = 3000

var siroHoThaoDuoc = ArrayList<Sanpham>()
var xuongKhop = ArrayList<Sanpham>()
var imageURLList = ArrayList<String>()


lateinit var bottomNav: BottomNavigationView


class TrangChuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_trang_chu = TrangchuBinding.inflate(layoutInflater)
        setContentView(binding_trang_chu.root)

        println(customer)
        // --ViewPager
        viewPager = binding_trang_chu.idViewPager

//        val quotesApi = RetrofitClient.getInstance().create(ViewPagerAPI::class.java)
//
//        GlobalScope.launch {
//            val result = async {quotesApi.getAll()}
//            val test = result.await().body()
//            println(test?.get(0)?.name)
//        }

        // launching a new coroutine
//        viewPagerApi = RetrofitClient.getInstance().create(API::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            loadDataCart()
            loadDataDonhang()

            val res_getBannerAds = async { viewPagerApi.getBannerAds() }
            val res_getAllProduct = async { viewPagerApi.getAllProduct() }
            var list_banner_ads: ArrayList<BannerAds>
            var list_san_pham: ArrayList<Sanpham>
            list_banner_ads = res_getBannerAds.await().body()!!
            list_san_pham = res_getAllProduct.await().body()!!
            withContext(Dispatchers.Main) {
            if (imageURLList.isEmpty()) {
                for (i in list_banner_ads) {
                    imageURLList.add(i.link)
                }
                reloadAllDataListView()
            }

            if (siroHoThaoDuoc.isEmpty()) {
                for (i in list_san_pham) {
                    if (i.type == "Xương khớp") {
                        xuongKhop.add(i)
                    } else if (i.type == "Siro ho thảo dược") {
                        siroHoThaoDuoc.add(i)

                    }

                }
                reloadAllDataListView()
            }

                // Update UI here
            }
        }
        viewPagerAdapter = ViewPagerAdapter(this, imageURLList)
        viewPager.adapter = viewPagerAdapter

        // --ViewPager end


        // --RecycleView
//        productArrayList.clear()
//        productArrayList.add(test)

        val adapterRecyclerHome1 = RecycleViewHomeAdapter(siroHoThaoDuoc, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@TrangChuActivity, ChiTietSanPhamActivity::class.java)
                intent.putExtra("item", siroHoThaoDuoc.get(position) as java.io.Serializable)

                startActivity(intent)
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
            }
        })
        val adapterRecyclerHome2 = RecycleViewHomeAdapter(xuongKhop, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
            }
        })

        binding_trang_chu.recyclerView1.layoutManager = LinearLayoutManager(this)
        binding_trang_chu.recyclerView2.layoutManager = LinearLayoutManager(this)
        val HorizontalLayout1 = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val HorizontalLayout2 = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding_trang_chu.recyclerView1.setLayoutManager(HorizontalLayout1)
        binding_trang_chu.recyclerView1.adapter = adapterRecyclerHome1
        binding_trang_chu.recyclerView2.setLayoutManager(HorizontalLayout2)
        binding_trang_chu.recyclerView2.adapter = adapterRecyclerHome2


        // --RecycleView end

//      Xử lý thanh điều hướng BottomNavigationView
        val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.hoso -> {
                        // put your code here
                        val intent = Intent(this@TrangChuActivity, HoSoActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.thongbao -> {
                        // put your code here
                        val intent = Intent(this@TrangChuActivity, ThongBaoActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.donhang -> {
                        // put your code here
                        val intent = Intent(this@TrangChuActivity, DonHangActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

        binding_trang_chu.bottomNavigationView.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        // Xử lý sự kiện khi bấm vào giỏ hàng
        binding_trang_chu.btnCart.setOnClickListener() {
            val intent = Intent(this, CartActivity::class.java)
            loadDataCart()

            startActivity(intent)
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
        }

        // Xử lý người dùng tìm kiếm
        binding_trang_chu.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Xử lý khi người dùng nhấn nút tìm kiếm
                val intent = Intent(this@TrangChuActivity, SearchActivity::class.java).apply {
                    putExtra("text_search", query)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Xử lý khi người dùng thay đổi nội dung tìm kiếm
                println(newText)
                return false
            }
        })

        startTimer()
    }




    fun reloadAllDataListView() {
        //arrayAdapter.clear()
        //arrayAdapter.addAll(productArrayList)
//        imageURLList.clear()
//        for (i in imageURLList) {
//            imageURLList.add(i)
//        }
        viewPager.adapter?.notifyDataSetChanged()
        binding_trang_chu.recyclerView1.adapter?.notifyDataSetChanged()
    }

    private fun startTimer() {
        handler = Handler(Looper.getMainLooper())
        val update = Runnable {
            if (page == imageURLList.size - 1) {
                page = 0
            }
            viewPager.setCurrentItem(page++, true)
        }
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, delay, delay)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                page = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    timer.cancel()
                }
            }
        })
//        timer = Timer()
//        timer?.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                runOnUiThread {
//                    println(page)
//                    if (page == imageURLList.size - 1) {
//                        page = 0
//                    } else {
//                        page++
//                    }
//                    viewPager.setCurrentItem(page, true)
//                }
//            }
//        }, delay, delay) // chuyển đổi sau 3 giây và thực hiện lại sau 3 giây
//
//        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
//
//            override fun onPageSelected(position: Int) {
//                // cập nhật giá trị của biến page khi người dùng vuốt sang ảnh khác
//                page = position
//                delay = 3000
//                timer?.cancel()
//                timer = null
//                startTimer()
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
    }
}