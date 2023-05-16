package com.example.medicinemart.activities

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.medicinemart.R
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewHomeAdapter
import com.example.medicinemart.adapter.ViewPagerAdapter
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.all_product
import com.example.medicinemart.common.Info.doubleBackDelay
import com.example.medicinemart.common.Info.doubleBackPressed
import com.example.medicinemart.common.Info.id_address_max_in_db
import com.example.medicinemart.common.Info.products_in_cart
import com.example.medicinemart.databinding.CartBinding
import com.example.medicinemart.databinding.DonhangBinding
import com.example.medicinemart.databinding.TrangchuBinding
import com.example.medicinemart.models.BannerAds
import com.example.medicinemart.models.Sanpham
import com.example.medicinemart.retrofit.RetrofitClient.viewPagerApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.*
import java.util.*


private lateinit var binding_trang_chu: TrangchuBinding
private lateinit var viewPagerAdapter: ViewPagerAdapter
private lateinit var viewPager: ViewPager


//private var doubleBackPressed = false
public val doubleBackToExitHandler = Handler(Looper.getMainLooper())
//private val doubleBackDelay: Long = 1500

private lateinit var handler: Handler
private var page = 0
private var delay: Long = 3000

var siroHoThaoDuoc = ArrayList<Sanpham>()
var xuongKhop = ArrayList<Sanpham>()
var dauNgaiCuu = ArrayList<Sanpham>()
var daiTrang = ArrayList<Sanpham>()
var imageURLList = ArrayList<String>()

fun String.removeAccent(): String {
    val regex = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    val temp = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
    return regex.replace(temp, "").replace("Đ", "D").replace("đ", "d")
}


class TrangChuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_trang_chu = TrangchuBinding.inflate(layoutInflater)
        binding_gio_hang = CartBinding.inflate(layoutInflater)
        binding_don_hang = DonhangBinding.inflate(layoutInflater)
        setContentView(binding_trang_chu.root)

        binding_trang_chu.root.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_trang_chu.root.windowToken, 0)
            }
        }

        // Hiện số lượng thông báo chưa đọc ở logo thôgn báo trong menu
        var badge = binding_trang_chu.bottomNavigationView.getOrCreateBadge(R.id.thongbao)
        if (Info.so_thong_bao_chua_doc == 0) {
            badge.isVisible = false
        } else {
            badge.isVisible = true
            badge.number = Info.so_thong_bao_chua_doc
        }

        // Hiện số lượng sản phẩm đang có trong giỏ hàng
        if (products_in_cart.isEmpty()) {
            binding_trang_chu.quantityInCart.visibility = View.GONE
        } else {
            binding_trang_chu.quantityInCart.visibility = View.VISIBLE
            binding_trang_chu.quantityInCart.text = products_in_cart.size.toString()
        }

        // Xử lý quét mã vạch
        val barLauncher = registerForActivityResult(ScanContract()) { result ->
            if (result.contents != null) {
                // Xử lý kết quả quét mã vạch ở đây
                var product = Sanpham()
                var flag = false
                for (i in Info.all_product) {
                    val originalString = i.barcode
                    val nonAccentString = originalString.removeAccent()
                    if (nonAccentString.contains(result.contents.toString(), true)) {
                        product = i
                        productSearchList.add(product)
                        flag = true
                        break
                    }
                }
                if (flag == true) {
                    flag = false
                    val intent = Intent(this, ChiTietSanPhamActivity::class.java)
                    intent.putExtra("item", product as java.io.Serializable)
                    intent.putExtra("goto", "trangchu")
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setCancelable(false)

                    val view = layoutInflater.inflate(R.layout.dialog_no_address, null)
                    val closeButton = view.findViewById<Button>(R.id.dialog_close_button)
                    var dialog_message = view.findViewById<TextView>(R.id.dialog_message)
                    view.findViewById<TextView>(R.id.dialog_message).text = "Xin lỗi chúng tôi không tìm thấy sản phẩm nào!"
                    view.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.sad)

                    builder.setView(view)
                    val dialog = builder.create()
                    dialog.show()

                    closeButton.setOnClickListener {
                        dialog.dismiss()
                    }
                }
            }
        }

        binding_trang_chu.barCodeScanner.setOnClickListener() {
            val animation = AnimationUtils.loadAnimation(this, R.anim.btn_anim)
            binding_trang_chu.barCodeScanner.startAnimation(animation)
            val options = ScanOptions()
            options.setOrientationLocked(false)
            options.setBeepEnabled(true)
            options.setOrientationLocked(true)
            options.setCaptureActivity(CaptureAct::class.java)
            barLauncher.launch(options)
        }

//        val broadcastReceiver = NetworkChangeReceiver()
//        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        registerReceiver(broadcastReceiver, intentFilter)
//        getAddressFromDB()

        // --ViewPager
        viewPager = binding_trang_chu.idViewPager

        GlobalScope.launch(Dispatchers.IO) {
            val res_getBannerAds = async { viewPagerApi.getBannerAds() }
//            val res_getAllProduct = async { viewPagerApi.getAllProduct() }
//            val res_getAddress = async { viewPagerApi.getAddress(_username) }
            val res_getIdAddressMax = async { viewPagerApi.getIdAddressMax() }
            id_address_max_in_db = res_getIdAddressMax.await().body()!!
            var list_banner_ads: ArrayList<BannerAds>
            list_banner_ads = res_getBannerAds.await().body()!!
//            all_product = res_getAllProduct.await().body()!!
//            list_address = res_getAddress.await().body()!!
            withContext(Dispatchers.Main) {
                if (imageURLList.isEmpty()) {
                    for (i in list_banner_ads) {
                        imageURLList.add(i.link)
                    }
                    reloadAllDataListView()
                }

                if (siroHoThaoDuoc.isEmpty()) {
                    for (i in all_product) {
                        if (i.type == "Xương khớp") {
                            xuongKhop.add(i)
                        } else if (i.type == "Siro ho thảo dược") {
                            siroHoThaoDuoc.add(i)
                        } else if (i.type == "Dầu ngải cứu") {
                            dauNgaiCuu.add(i)
                        } else if (i.type == "Đại tràng") {
                            daiTrang.add(i)
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
                intent.putExtra("goto", "")
                startActivity(intent)
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
            }
        })

        val adapterRecyclerHome2 = RecycleViewHomeAdapter(xuongKhop, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
                val intent = Intent(this@TrangChuActivity, ChiTietSanPhamActivity::class.java)
                intent.putExtra("item", xuongKhop.get(position) as java.io.Serializable)
                intent.putExtra("goto", "")
                startActivity(intent)
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
            }
        })

        val adapterRecyclerHome3 = RecycleViewHomeAdapter(dauNgaiCuu, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
                val intent = Intent(this@TrangChuActivity, ChiTietSanPhamActivity::class.java)
                intent.putExtra("item", dauNgaiCuu.get(position) as java.io.Serializable)
                intent.putExtra("goto", "")
                startActivity(intent)
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
            }
        })

        val adapterRecyclerHome4 = RecycleViewHomeAdapter(daiTrang, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
                val intent = Intent(this@TrangChuActivity, ChiTietSanPhamActivity::class.java)
                intent.putExtra("item", daiTrang.get(position) as java.io.Serializable)
                intent.putExtra("goto", "")
                startActivity(intent)
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
            }
        })

        binding_trang_chu.recyclerView1.layoutManager = LinearLayoutManager(this)
        binding_trang_chu.recyclerView2.layoutManager = LinearLayoutManager(this)
        binding_trang_chu.recyclerView3.layoutManager = LinearLayoutManager(this)
        binding_trang_chu.recyclerView4.layoutManager = LinearLayoutManager(this)
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
        val HorizontalLayout3 = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val HorizontalLayout4 = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding_trang_chu.recyclerView1.setLayoutManager(HorizontalLayout1)
        binding_trang_chu.recyclerView1.adapter = adapterRecyclerHome1
        binding_trang_chu.recyclerView2.setLayoutManager(HorizontalLayout2)
        binding_trang_chu.recyclerView2.adapter = adapterRecyclerHome2
        binding_trang_chu.recyclerView3.setLayoutManager(HorizontalLayout3)
        binding_trang_chu.recyclerView3.adapter = adapterRecyclerHome3
        binding_trang_chu.recyclerView4.setLayoutManager(HorizontalLayout4)
        binding_trang_chu.recyclerView4.adapter = adapterRecyclerHome4


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
            val animation = AnimationUtils.loadAnimation(this, R.anim.btn_anim)
            binding_trang_chu.btnCart.startAnimation(animation)
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
        }

        binding_trang_chu.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Xử lý khi người dùng nhấn nút tìm kiếm trên bàn phím
                val intent = Intent(this@TrangChuActivity, SearchActivity::class.java).apply {
                    putExtra("text_search", binding_trang_chu.editTextSearch.text.toString())
                }
                for (product in all_product) {
                    val originalString = product.name
                    val nonAccentString = originalString.removeAccent()
                    if (nonAccentString.contains(binding_trang_chu.editTextSearch.text.toString().removeAccent(), true)) {
                        productSearchList.add(product)
                    }
                }
                startActivity(intent)
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                return@setOnEditorActionListener true
            }
            false
        }

        /*
        // Xử lý người dùng tìm kiếm
        binding_trang_chu.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Xử lý khi người dùng nhấn nút tìm kiếm
                val intent = Intent(this@TrangChuActivity, SearchActivity::class.java).apply {
                    putExtra("text_search", query)
                }
                for (product in all_product) {
                    val originalString = product.name
                    val nonAccentString = originalString.removeAccent()
                    if (nonAccentString.contains(query.toString(), true)) {
                        productSearchList.add(product)
                    }
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
        })  */

        binding_trang_chu.tvXemthem1.setOnClickListener() {
            binding_trang_chu.tvXemthem1.setTextColor(ContextCompat.getColor(this, R.color.grey))
            val query = "siro ho thao duoc"
            val intent = Intent(this@TrangChuActivity, SearchActivity::class.java).apply {
                putExtra("text_search", "Siro ho thảo dược")
            }
            for (product in all_product) {
                val originalString = product.type
                val nonAccentString = originalString.removeAccent()
                if (nonAccentString.contains(query, true)) {
                    productSearchList.add(product)
                }
            }
            startActivity(intent)
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
        }
        binding_trang_chu.tvXemthem2.setOnClickListener() {
            binding_trang_chu.tvXemthem2.setTextColor(ContextCompat.getColor(this, R.color.grey))
            val query = "xuong khop"
            val intent = Intent(this@TrangChuActivity, SearchActivity::class.java).apply {
                putExtra("text_search", "Xương khớp")
            }
            for (product in all_product) {
                val originalString = product.type
                val nonAccentString = originalString.removeAccent()
                if (nonAccentString.contains(query, true)) {
                    productSearchList.add(product)
                }
            }
            startActivity(intent)
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
        }
        binding_trang_chu.tvXemthem3.setOnClickListener() {
            binding_trang_chu.tvXemthem3.setTextColor(ContextCompat.getColor(this, R.color.grey))
            val query = "dau ngai cuu"
            val intent = Intent(this@TrangChuActivity, SearchActivity::class.java).apply {
                putExtra("text_search", "Dầu ngải cứu")
            }
            for (product in all_product) {
                val originalString = product.type
                val nonAccentString = originalString.removeAccent()
                if (nonAccentString.contains(query, true)) {
                    productSearchList.add(product)
                }
            }
            startActivity(intent)
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
        }
        binding_trang_chu.tvXemthem4.setOnClickListener() {
            binding_trang_chu.tvXemthem4.setTextColor(ContextCompat.getColor(this, R.color.grey))
            val query = "dai trang"
            val intent = Intent(this@TrangChuActivity, SearchActivity::class.java).apply {
                putExtra("text_search", "Đại tràng")
            }
            for (product in all_product) {
                val originalString = product.type
                val nonAccentString = originalString.removeAccent()
                if (nonAccentString.contains(query, true)) {
                    productSearchList.add(product)
                }
            }
            startActivity(intent)
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
        }
        startTimer()
    }

    fun reloadAllDataListView() {
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
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                page = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    timer.cancel()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding_trang_chu.bottomNavigationView.setSelectedItemId(R.id.home)
        binding_trang_chu.tvXemthem1.setTextColor(ContextCompat.getColor(this, R.color.blue))
        binding_trang_chu.tvXemthem2.setTextColor(ContextCompat.getColor(this, R.color.blue))
        binding_trang_chu.tvXemthem3.setTextColor(ContextCompat.getColor(this, R.color.blue))
        binding_trang_chu.tvXemthem4.setTextColor(ContextCompat.getColor(this, R.color.blue))

        binding_trang_chu.editTextSearch.clearFocus()
        binding_trang_chu.editTextSearch.setText("")

        if (products_in_cart.isEmpty()) {
            binding_trang_chu.quantityInCart.visibility = View.GONE
        } else {
            binding_trang_chu.quantityInCart.visibility = View.VISIBLE
            binding_trang_chu.quantityInCart.text = products_in_cart.size.toString()
        }

        productSearchList.clear()
        productSearchListCopy.clear()
//        binding_trang_chu.searchView.setQuery("", false)
//        binding_trang_chu.searchView.clearFocus()

        var badge = binding_trang_chu.bottomNavigationView.getOrCreateBadge(R.id.thongbao)
        if (Info.so_thong_bao_chua_doc == 0) {
            badge.isVisible = false
        } else {
            badge.isVisible = true
            badge.number = Info.so_thong_bao_chua_doc
        }
    }

    override fun onBackPressed() {
        if (doubleBackPressed) {
            super.onBackPressed()
            finishAffinity()
            return
        }

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.getRunningTasks(1) // Lấy danh sách task chạy hiện tại

//        if (tasks.isNotEmpty() && tasks[0].numActivities == 1) {
            // Chỉ có một activity duy nhất trong stack
            doubleBackPressed = true
            Toast.makeText(this, "Chạm lần nữa để thoát", Toast.LENGTH_SHORT).show()
//        } else {
//            super.onBackPressed()
//        }

        doubleBackToExitHandler.postDelayed({
            doubleBackPressed = false
        }, doubleBackDelay)
    }
}