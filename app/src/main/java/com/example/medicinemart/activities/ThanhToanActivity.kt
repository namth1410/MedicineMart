package com.example.medicinemart.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewThanhToanAdapter
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.customer
import com.example.medicinemart.common.Info.delivery_address
import com.example.medicinemart.common.Info.list_address
import com.example.medicinemart.common.Info.product_to_pay
import com.example.medicinemart.common.Info.products_in_cart
import com.example.medicinemart.common.Info.quantity_product_to_pay
import com.example.medicinemart.databinding.ThanhtoanBinding
import com.example.medicinemart.models.Address
import com.example.medicinemart.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat

private lateinit var binding_thanh_toan: ThanhtoanBinding

private lateinit var progressDialog: ProgressDialog

class ThanhToanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_thanh_toan = ThanhtoanBinding.inflate(layoutInflater)
        setContentView(binding_thanh_toan.root)

        if (list_address.isEmpty()) {
            delivery_address = Address(
                0, "Chưa có số điện thoại!", "", "Chưa có tên!", BigDecimal(
                    Info.location_default.latitude
                ), BigDecimal(Info.location_default.longitude), "Chưa có địa chỉ!"
            )
        } else {
            delivery_address = list_address.get(0)
        }

        val adapterRecyclerThanhToanAdapter =
            RecycleViewThanhToanAdapter(product_to_pay, this, object :
                OnItemClickListener {
                override fun onItemClick(position: Int) {

                }
            })
        binding_thanh_toan.recyclerView.layoutManager = LinearLayoutManager(this)

        val VerticalLayout = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding_thanh_toan.recyclerView.setLayoutManager(VerticalLayout)
        binding_thanh_toan.recyclerView.adapter = adapterRecyclerThanhToanAdapter


        binding_thanh_toan.fullName.text = delivery_address!!.full_name
        binding_thanh_toan.phone.text = delivery_address!!.phone
        binding_thanh_toan.location.text = delivery_address!!.location


        var tongTienHang = 0
        for (i in 0 until product_to_pay.size) {
            tongTienHang += product_to_pay.get(i).price * quantity_product_to_pay.get(i)
        }
        val formatter: NumberFormat = DecimalFormat("#,###")
        binding_thanh_toan.tvTongtienhang.text = formatter.format(tongTienHang) + "đ"
        binding_thanh_toan.tvTongthanhtoan.text = formatter.format(tongTienHang) + "đ"
        binding_thanh_toan.totalPrice.text = formatter.format(tongTienHang) + "đ"


        binding_thanh_toan.tvDiachi.setOnClickListener() {
            val intent = Intent(this, ChonDiaChiNhanHangActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        binding_thanh_toan.btnDathang.setOnClickListener() {
            progressDialog = ProgressDialog(this)
            progressDialog.setCancelable(false)
            progressDialog.setMessage("Đơn hàng của bạn đang được xử lý...")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog.setProgress(0)
            progressDialog.show()
            println(delivery_address)
            val call = RetrofitClient.viewPagerApi.addOrder(customer.id, delivery_address!!.id)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        // Xử lý kết quả trả về nếu thêm hàng mới thành công
                        require_reload_data_order = true
                        for (i in product_to_pay) {
                            val call = RetrofitClient.viewPagerApi.addOrderDetail(i.id, quantity_product_to_pay.get(
                                product_to_pay.indexOf(i)), i.price)
                            call.enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    if (response.isSuccessful) {
                                        val call1 = RetrofitClient.viewPagerApi.deleteProductInCart(customer.id, i.id)
                                        call1.enqueue(object : Callback<Void> {
                                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                                if (response.isSuccessful) {
                                                    products_in_cart.remove(i)
                                                    val content = "Đơn hàng mã " + i.id + " đã được hệ thống ghi nhận. Hãy chờ người bán xác nhận!"
                                                    val call1 = RetrofitClient.viewPagerApi.addNotification(customer.id, Info.title_cho_xac_nhan, "hih", i.id)
                                                    call1.enqueue(object : Callback<ResponseBody> {
                                                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                                            if (response.isSuccessful) {
                                                                // Xóa thành công
                                                                progressDialog.dismiss()
                                                                println("da doi can load lai trang" + require_reload_data_cart)
                                                                require_reload_data_thong_bao = true
                                                                require_reload_data_cart = true
                                                                println("da doi can load lai trang" + require_reload_data_cart)
                                                                Info.alertDialog(this@ThanhToanActivity)
                                                            } else {
                                                                // Xóa không thành công
                                                            }
                                                        }

                                                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                                            // Xóa không thành công do lỗi mạng hoặc lỗi server
                                                        }
                                                    })
                                                    // Xóa thành công
                                                } else {
                                                    // Xóa không thành công
                                                }
                                            }

                                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                                // Xóa không thành công do lỗi mạng hoặc lỗi server
                                            }
                                        })
                                    } else {
                                        // Xử lý lỗi nếu thêm hàng mới thất bại
                                    }
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    // Xử lý lỗi nếu không thể kết nối tới server
                                }
                            })

//                            val call1 = RetrofitClient.viewPagerApi.deleteProductInCart(customer.id, i.id)
//                            call1.enqueue(object : Callback<Void> {
//                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                                    if (response.isSuccessful) {
//                                        // Xóa thành công
//                                    } else {
//                                        // Xóa không thành công
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<Void>, t: Throwable) {
//                                    // Xóa không thành công do lỗi mạng hoặc lỗi server
//                                }
//                            })

//                            quantity_product_in_cart.removeAt(products_in_cart.indexOf(i))
//                            products_in_cart.remove(i)
//                            binding_gio_hang.recyclerView.adapter?.notifyDataSetChanged()
//                            donHangChoXacNhanItemList.add(i)
//                            quantityChoXacNhan.add(quantity_product_to_pay.get(product_to_pay.indexOf(i)))
                            binding_don_hang.recyclerView.adapter?.notifyDataSetChanged()

                        }


                    } else {
                        // Xử lý lỗi nếu thêm hàng mới thất bại
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Xử lý lỗi nếu không thể kết nối tới server
                }
            })
//            progressDialog = ProgressDialog(this)
//            progressDialog.setCancelable(false)
//            progressDialog.setMessage("Đơn hàng của bạn đang được xử lý...")
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//            progressDialog.setProgress(0)
//            progressDialog.show()
//            Info.total_products_checcked_in_cart = 0
//            Handler().postDelayed({
//                progressDialog.dismiss()
//                product_to_pay.clear()
//                quantity_product_to_pay.clear()
//
//
//                val builder = AlertDialog.Builder(this)
//                builder.setCancelable(false)
//                val view = layoutInflater.inflate(R.layout.dialog_success, null)
//                val closeButton = view.findViewById<Button>(R.id.btn_ok)
//
//                builder.setView(view)
//                val dialog = builder.create()
//                dialog.show()
//
//                closeButton.setOnClickListener {
//                    dialog.dismiss()
//                    val intent = Intent(this, TrangChuActivity::class.java)
//                    startActivity(intent)
//                    Animatoo.animateSlideLeft(this)
//                }
//                overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
//            }, 1500)

//// Hiển thị ProgressBar và layout xám
//            progressDialog.visibility = View.VISIBLE
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//
//// Ẩn ProgressBar và layout xám
//            progressDialog.visibility = View.GONE
//            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        binding_thanh_toan.btnBack.setOnClickListener() {
            onBackPressed()
            product_to_pay.clear()
            quantity_product_to_pay.clear()
        }

    }

    override fun onResume() {
        super.onResume()
        binding_thanh_toan.fullName.text = delivery_address!!.full_name
        binding_thanh_toan.phone.text = delivery_address!!.phone
        binding_thanh_toan.location.text = delivery_address!!.location
    }
}