package com.example.medicinemart.common

import android.content.SharedPreferences
import com.example.medicinemart.models.Address
import com.example.medicinemart.models.Customer
import com.example.medicinemart.models.Sanpham
import com.google.android.gms.maps.model.LatLng
import java.math.BigDecimal

object Info {
    lateinit var sharedPref: SharedPreferences

    val USERNAME_PATTERN = "^[a-zA-Z0-9]{6,30}\$".toRegex()
    val PASSWORD_PATTERN = "^\\S{6,15}\$".toRegex()

    var username_list = ArrayList<String>()
    var password_list = ArrayList<String>()
    var salt_list = ArrayList<String>()

    var _username = ""
    var user_id= ""

    var customer = Customer()

    var all_product = ArrayList<Sanpham>()


    var products_in_cart = ArrayList<Sanpham>()
    var quantity_product_in_cart = ArrayList<Int>()
    var total_products_checcked_in_cart = 0
    var product_to_pay = ArrayList<Sanpham>()
    var quantity_product_to_pay = ArrayList<Int>()

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

//    var address = Address()

}