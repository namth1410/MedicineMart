package com.example.medicinemart.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medicinemart.R
import com.example.medicinemart.activities.ChiTietSanPhamActivity
import com.example.medicinemart.activities.binding_gio_hang
import com.example.medicinemart.activities.checkList
import com.example.medicinemart.activities.total_price
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.customer
import com.example.medicinemart.common.Info.product_to_pay
import com.example.medicinemart.common.Info.products_in_cart
import com.example.medicinemart.common.Info.quantity_product_in_cart
import com.example.medicinemart.common.Info.quantity_product_to_pay
import com.example.medicinemart.common.Info.total_products_checcked_in_cart
import com.example.medicinemart.models.Sanpham
import com.example.medicinemart.retrofit.RetrofitClient.viewPagerApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat


interface OnCheckedChangeListener {
    fun onCheckedChanged(totalPrice: Int)
}

interface OnItemInteractionListener : OnItemClickListener, OnCheckedChangeListener


class RecycleViewCartAdapter(
    private val mList: List<Sanpham>,
    private val quantity_array: List<Int>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecycleViewCartAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        val quantity_item = quantity_array[position]
        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.checkBox)
        var quantity = holder.itemView.findViewById<TextView>(R.id.quantity)

//        holder.itemView.setAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade))
//        holder.itemView.findViewById<LinearLayout>(R.id.container).startAnimation(
//            android.view.animation.AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade)
//        )


        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        if (binding_gio_hang.allCheckBox.isChecked) {
            checkBox.isChecked = true
            product_to_pay.clear()
            product_to_pay.addAll(products_in_cart)
            quantity_product_to_pay.clear()
            quantity_product_to_pay.addAll(quantity_product_in_cart)
        } else {
            if (total_products_checcked_in_cart == 0) {
                checkBox.isChecked = false
                product_to_pay.clear()
                quantity_product_to_pay.clear()
            }
        }

        checkBox.setOnClickListener() {
            if (total_products_checcked_in_cart == 0) {
                total_price = 0
            }
            if (checkBox.isChecked) {
                total_price += ItemsViewModel.price * quantity_product_in_cart.get(position)
                total_products_checcked_in_cart++
                product_to_pay.add(ItemsViewModel)
                quantity_product_to_pay.add(quantity_product_in_cart.get(position))
                if (total_products_checcked_in_cart == products_in_cart.size) {
                    binding_gio_hang.allCheckBox.isChecked = true
                }
            } else {
                total_price -= ItemsViewModel.price * quantity_product_in_cart.get(position)
                if (total_products_checcked_in_cart == products_in_cart.size) {
                    binding_gio_hang.allCheckBox.isChecked = false
                }
                quantity_product_to_pay.removeAt(product_to_pay.indexOf(ItemsViewModel))
                product_to_pay.remove(ItemsViewModel)
                total_products_checcked_in_cart--
            }
            println(total_price)
            val formatter: NumberFormat = DecimalFormat("#,###")
            binding_gio_hang.totalPrice.text = formatter.format(total_price) + "đ"
        }

        holder.itemView.findViewById<Button>(R.id.btn_sub).setOnClickListener() {
//            (quantity.text).toString().toInt()

            if ((quantity.text).toString().toInt() == 1) {
                val dialogBuilder = AlertDialog.Builder(context)
                    .setMessage("Bạn chắc chắn muốn bỏ sản phẩm này?")
                    .setPositiveButton("Yes") { _, _ ->
                        // Xử lý khi người dùng chọn Yes
//                        reloadCartRecycleView()
                        val call = viewPagerApi.deleteProductInCart(customer.id, ItemsViewModel.id)
                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    // Xóa thành công
                                    products_in_cart.removeAt(position)
                                    quantity_product_in_cart.removeAt(position)
                                    if (checkBox.isChecked) {
                                        total_price -= ItemsViewModel.price
                                        checkBox.isChecked = false
                                    }
                                    if (products_in_cart.isEmpty()) {
                                        binding_gio_hang.allCheckBox.isChecked = false
                                        checkList(products_in_cart)
                                    }
                                    binding_gio_hang.recyclerView.adapter?.notifyDataSetChanged()
                                    val formatter: NumberFormat = DecimalFormat("#,###")
                                    binding_gio_hang.totalPrice.text =
                                        formatter.format(total_price) + "đ"
                                } else {
                                    // Xóa không thành công
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                // Xóa không thành công do lỗi mạng hoặc lỗi server
                            }
                        })
                    }
                    .setNegativeButton("No") { _, _ ->
                        // Xử lý khi người dùng chọn No
                    }.create()

                dialogBuilder.show()
            } else {
                quantity.text = ((quantity.text).toString().toInt() - 1).toString()
                var currentQuantity = quantity_product_in_cart.get(position)
                currentQuantity -= 1
                quantity_product_in_cart.set(position, currentQuantity)
                if (checkBox.isChecked) {
                    total_price -= ItemsViewModel.price
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    binding_gio_hang.totalPrice.text = formatter.format(total_price) + "đ"
                    val i = product_to_pay.indexOf(ItemsViewModel)
                    quantity_product_to_pay.set(i, quantity_product_to_pay.get(i) - 1)
                }
                val call = viewPagerApi.updateQuantity(
                    customer.id,
                    ItemsViewModel.id,
                    (quantity.text).toString().toInt()
                )
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        // Xử lý kết quả trả về từ API nếu cần
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Xử lý lỗi nếu có
                    }
                })
            }
        }

        holder.itemView.findViewById<Button>(R.id.btn_sum).setOnClickListener() {
            (quantity.text).toString().toInt()
            quantity.text = ((quantity.text).toString().toInt() + 1).toString()
            var currentQuantity = Info.quantity_product_in_cart.get(position)
            currentQuantity += 1
            quantity_product_in_cart.set(position, currentQuantity)
            if (checkBox.isChecked) {
                total_price += ItemsViewModel.price
                val formatter: NumberFormat = DecimalFormat("#,###")
                binding_gio_hang.totalPrice.text = formatter.format(total_price) + "đ"
                val i = product_to_pay.indexOf(ItemsViewModel)
                quantity_product_to_pay.set(i, quantity_product_to_pay.get(i) + 1)
            }
            val call = viewPagerApi.updateQuantity(
                customer.id,
                ItemsViewModel.id,
                (quantity.text).toString().toInt()
            )
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    // Xử lý kết quả trả về từ API nếu cần
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Xử lý lỗi nếu có
                }
            })
        }

        holder.name.text = ItemsViewModel.name
        holder.quantity.text = quantity_item.toString()
        val formatter: NumberFormat = DecimalFormat("#,###")
        holder.price.text = formatter.format(ItemsViewModel.price) + "đ"
        if (ItemsViewModel.image.startsWith("\"") && ItemsViewModel.image.endsWith("\"")) {
            ItemsViewModel.image =
                ItemsViewModel.image.substring(1, ItemsViewModel.image.length - 1)
        }
//        val link = ItemsViewModel.image.substring(1, ItemsViewModel.image.length - 1)
        Glide
            .with(context)
            .load(ItemsViewModel.image)
            .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
        val quantity: TextView = itemView.findViewById(R.id.quantity)

        init {
            imageView.setOnClickListener {
                val intent = Intent(imageView.context, ChiTietSanPhamActivity::class.java)
                intent.putExtra("item", products_in_cart.get(position) as java.io.Serializable)
                intent.putExtra("goto", "cart")
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    imageView.context as Activity,
                    imageView,
                    imageView.transitionName
                )
                imageView.context.startActivity(intent, options.toBundle())
            }
            ViewCompat.setTransitionName(imageView, "shared_element")
        }
    }

}
