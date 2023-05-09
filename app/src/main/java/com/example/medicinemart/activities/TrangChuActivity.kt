package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
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
import com.example.medicinemart.retrofit.API
import com.example.medicinemart.retrofit.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private lateinit var binding_trang_chu: TrangchuBinding
private lateinit var viewPagerAdapter: ViewPagerAdapter
private lateinit var viewPager: ViewPager

var siroHoThaoDuoc = ArrayList<Sanpham>()
private var xuongKhop = ArrayList<Sanpham>()
var imageURLList = ArrayList<String>()


lateinit var bottomNav: BottomNavigationView

class TrangChuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_trang_chu = TrangchuBinding.inflate(layoutInflater)
        setContentView(binding_trang_chu.root)

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
        val viewPagerApi = RetrofitClient.getInstance().create(API::class.java)
        GlobalScope.launch(Dispatchers.Main) {
            val res_getBannerAds = async { viewPagerApi.getBannerAds() }
            val res_getAllProduct = async { viewPagerApi.getAllProduct() }
            var list_banner_ads: ArrayList<BannerAds>
            var list_san_pham: ArrayList<Sanpham>
            list_banner_ads = res_getBannerAds.await().body()!!
            list_san_pham = res_getAllProduct.await().body()!!

            if (imageURLList.isEmpty()) {
                for (i in list_banner_ads) {
                    imageURLList.add(i.link)
                }
                reloadAllDataListView()
            }

            println("size " + list_san_pham.size)
            if (siroHoThaoDuoc.isEmpty()) {
                for (i in list_san_pham) {
                    if (i.type == "Xương khớp") {
                        xuongKhop.add(i)
                    } else if (i.type == "Siro ho thảo dược") {
                        siroHoThaoDuoc.add(i)

                    }

                }
                println(siroHoThaoDuoc.size)
                reloadAllDataListView()
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
                println("asdh")
            }
        })
        val adapterRecyclerHome2 = RecycleViewHomeAdapter(xuongKhop, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
                println("asdh")
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

//      Xử lý điều hướng thanh BottomNavigationView
        binding_trang_chu.bottomNavigationView.setSelectedItemId(R.id.home)
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.hoso -> {
                    // put your code here
                    val intent = Intent(this@TrangChuActivity, HoSoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.thongbao -> {
                    // put your code here
                    val intent = Intent(this@TrangChuActivity, ThongBaoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.donhang -> {
                    // put your code here
                    val intent = Intent(this@TrangChuActivity, DonHangActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding_trang_chu.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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

}