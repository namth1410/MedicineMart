package com.example.medicinemart.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.R
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewCartAdapter
import com.example.medicinemart.common.Info.customer
import com.example.medicinemart.common.Info.product_to_pay
import com.example.medicinemart.common.Info.products_in_cart
import com.example.medicinemart.common.Info.quantity_product_in_cart
import com.example.medicinemart.common.Info.quantity_product_to_pay
import com.example.medicinemart.common.Info.total_products_checcked_in_cart
import com.example.medicinemart.databinding.CartBinding
import com.example.medicinemart.models.Sanpham
import com.example.medicinemart.retrofit.RetrofitClient
import com.google.gson.JsonObject
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

public lateinit var binding_gio_hang: CartBinding
private var progressDialog: ProgressDialog? = null

var require_reload_data_cart = false

var total_price = 0

//fun loadDataCart() {
//    GlobalScope.launch(Dispatchers.Main) {
//        val getCart = async { RetrofitClient.viewPagerApi.getCart(customer.id) }
//        val res_getCart = getCart.await().body()
//        products_in_cart.clear()
//        quantity_product_in_cart.clear()
//        for (i in res_getCart!!) {
//            val id = i.getAsJsonPrimitive("id_product").asInt
//            val name = i.getAsJsonPrimitive("name").toString()
//            val type = i.getAsJsonPrimitive("type").toString()
//            val price = i.getAsJsonPrimitive("price").asInt
//            val describe = i.getAsJsonPrimitive("describe").toString()
//            val image = i.getAsJsonPrimitive("image").toString()
//            val tmp = Sanpham(id, name, type, price, describe, image)
//            products_in_cart.add(tmp)
//            quantity_product_in_cart.add(i.getAsJsonPrimitive("quantity").asInt)
//        }
//    }
//}

fun loadDataCart() {
    val call = RetrofitClient.viewPagerApi.getCart(customer.id)
    call.enqueue(object : Callback<java.util.ArrayList<JsonObject>> {
        override fun onResponse(
            call: Call<java.util.ArrayList<JsonObject>>,
            response: Response<java.util.ArrayList<JsonObject>>
        ) {
            if (response.isSuccessful) {
                // Xử lý kết quả trả về nếu thêm hàng mới thành công
                require_reload_data_cart = false
                if (progressDialog != null) {
                    progressDialog?.dismiss()
                    progressDialog = null
                }
                products_in_cart.clear()
                quantity_product_in_cart.clear()
                for (i in response.body()!!) {
                    val id = i.getAsJsonPrimitive("id_product").asInt
                    val name = i.getAsJsonPrimitive("name").toString()
                    val type = i.getAsJsonPrimitive("type").toString()
                    val price = i.getAsJsonPrimitive("price").asInt
                    val describe = i.getAsJsonPrimitive("describe").toString()
                    val image = i.getAsJsonPrimitive("image").toString()
                    val tmp = Sanpham(id, name, type, price, describe, image)
                    products_in_cart.add(tmp)
                    quantity_product_in_cart.add(i.getAsJsonPrimitive("quantity").asInt)
                }
                checkList(products_in_cart)
                binding_gio_hang.recyclerView.adapter?.notifyDataSetChanged()


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

fun checkList(list: ArrayList<Sanpham>) {
    if (list.isEmpty()) {
        binding_gio_hang.layoutEmpty.visibility = View.VISIBLE
        binding_gio_hang.recyclerView.visibility = View.INVISIBLE
        binding_gio_hang.bottomBar.visibility = View.INVISIBLE
    } else {
        binding_gio_hang.layoutEmpty.visibility = View.INVISIBLE
        binding_gio_hang.recyclerView.visibility = View.VISIBLE
        binding_gio_hang.bottomBar.visibility = View.VISIBLE
    }
}

class CartActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_gio_hang = CartBinding.inflate(layoutInflater)
        setContentView(binding_gio_hang.root)

        checkList(products_in_cart)

        val adapterRecyclerCartAdapter =
            RecycleViewCartAdapter(products_in_cart, quantity_product_in_cart, this, object :
                OnItemClickListener {
                override fun onItemClick(position: Int) {
                    // Xử lý sự kiện click ở đây
                    println(products_in_cart)
                    val intent = Intent(this@CartActivity, ChiTietSanPhamActivity::class.java)
                    intent.putExtra("item", products_in_cart.get(position) as java.io.Serializable)
                    intent.putExtra("goto", "cart")

                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
                }
            })

//        binding_gio_hang.recyclerView.isNestedScrollingEnabled = true
        binding_gio_hang.recyclerView.layoutManager = LinearLayoutManager(this)

        val VerticalLayout = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding_gio_hang.recyclerView.setLayoutManager(VerticalLayout)
        binding_gio_hang.recyclerView.adapter = adapterRecyclerCartAdapter
        binding_gio_hang.recyclerView.adapter?.notifyDataSetChanged()


        // Adding Listener Refresh page
        binding_gio_hang.swipeContainer.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            // Your code here
            Toast.makeText(applicationContext, "Works!", Toast.LENGTH_LONG).show()
            // To keep animation for 4 seconds
            Handler().postDelayed(Runnable { // Stop animation (This will be after 3 seconds)
                binding_gio_hang.swipeContainer.setRefreshing(false)
            }, 500) // Delay in millis
        })

//        binding_gio_hang.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    // Load lại dữ liệu ở đây
//                    loadDataCart()
//                    adapterRecyclerCartAdapter.notifyDataSetChanged()
//
//                }
//            }
//        })

        binding_gio_hang.allCheckBox.setOnClickListener() {
            if (binding_gio_hang.allCheckBox.isChecked) {
                total_price = 0
                for (i in 0 until products_in_cart.size) {
                    total_price += products_in_cart.get(i).price * quantity_product_in_cart.get(i)
                }
                total_products_checcked_in_cart = products_in_cart.size
            } else {
                println("ko check all")
                total_price = 0
                total_products_checcked_in_cart = 0
            }
            val formatter: NumberFormat = DecimalFormat("#,###")
            binding_gio_hang.totalPrice.text = formatter.format(total_price) + "đ"
            binding_gio_hang.recyclerView.adapter?.notifyDataSetChanged()

        }

        binding_gio_hang.btnMuahang.setOnClickListener {

            if (quantity_product_to_pay.isEmpty()) {
                val toasty = Toasty.warning(
                    applicationContext,
                    "Hello,aldlashldihjalshdlhasldhlakshdlkhaslkdasd Toasty!",
                    Toast.LENGTH_SHORT,
                    true
                )
                toasty.setGravity(Gravity.CENTER, 0, 0)
                toasty.show()
            } else {
                val intent = Intent(this, ThanhToanActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
            }
        }

        binding_gio_hang.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        if (require_reload_data_cart) {
            progressDialog = ProgressDialog(this)
            progressDialog?.setCancelable(false)
            progressDialog?.setMessage("Đợi xíu...")
            progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog?.setProgress(0)
            progressDialog?.show()
            loadDataCart()
        }
        total_price = 0
        binding_gio_hang.totalPrice.text = "0đ"
        binding_gio_hang.allCheckBox.isChecked = false
        total_products_checcked_in_cart = 0
        product_to_pay.clear()
        quantity_product_to_pay.clear()
        binding_gio_hang.recyclerView.adapter?.notifyDataSetChanged()
        println("san pham trong gio hang " + products_in_cart)
    }
}