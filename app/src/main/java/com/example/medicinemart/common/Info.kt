package com.example.medicinemart.common

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.R
import com.example.medicinemart.activities.NetworkChangeReceiver
import com.example.medicinemart.activities.TrangChuActivity
import com.example.medicinemart.models.*
import com.google.android.gms.maps.model.LatLng
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Info {
    var broadcastReceiver = NetworkChangeReceiver()

    lateinit var sharedPref: SharedPreferences

    val USERNAME_PATTERN = "^[a-zA-Z0-9]{6,30}\$".toRegex()
    val PASSWORD_PATTERN = "^\\S{6,15}\$".toRegex()

    var doubleBackPressed = false
    val doubleBackDelay: Long = 1500

    var username_list = ArrayList<String>()
    var password_list = ArrayList<String>()
    var salt_list = ArrayList<String>()

    var _username = ""

    var customer = Customer()

    var path_avat = ""

    var all_product = ArrayList<Sanpham>()

    var so_thong_bao_chua_doc = 0

    var products_in_cart = ArrayList<Sanpham>()
    var quantity_product_in_cart = ArrayList<Int>()
    var total_products_checcked_in_cart = 0
    var product_to_pay = ArrayList<Sanpham>()
    var quantity_product_to_pay = ArrayList<Int>()

//    val time_defaul = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    val time_defaul = LocalDateTime.of(2000, 1, 1, 0, 0, 0)
    val time_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val price_formatter: NumberFormat = DecimalFormat("#,###")


//    var order_detail = Order()

    var list_address = ArrayList<Address>()
    var delivery_address: Address? = null
    var position = -1
    var td_x = BigDecimal(00.00000000)
    var td_y = BigDecimal(00.00000000)

    var id_address_max_in_db = 0

    val location_default = LatLng(
        21.03823945605729,
        105.78267775475979
    )

    const val title_cho_xac_nhan = "Đơn hàng chờ xác nhận"
    const val title_da_huy = "Đơn hàng đã hủy"
    const val title_dang_giao = "Đơn hàng đang giao"
    const val title_đã_giao = "Đơn hàng giao thành công"

    fun switchActivity(context: Context, targetActivity: Class<*>) {
        val intent = Intent(context, targetActivity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        Animatoo.animateSlideRight(context)
    }


    fun alertDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.dialog_success, null)
        val closeButton = view.findViewById<Button>(R.id.btn_ok)

        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        closeButton.setOnClickListener {
            dialog.dismiss()
            switchActivity(context, TrangChuActivity::class.java)
        }
    }

//    var address = Address()

}