package com.example.medicinemart.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medicinemart.R
import com.example.medicinemart.activities.loadDataCart
import com.example.medicinemart.activities.reloadCartRecycleView
import com.example.medicinemart.activities.total_price
import com.example.medicinemart.models.Sanpham
import com.example.medicinemart.retrofit.RetrofitClient.viewPagerApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat


class RecycleViewCartAdapter (private val mList: List<Sanpham>, private val quantity_array: List<Int>, private  val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecycleViewCartAdapter.ViewHolder>(){

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

        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        checkBox.setOnClickListener() {
            if (checkBox.isChecked) {
                total_price += ItemsViewModel.price * quantity_item
            } else {
                total_price -= ItemsViewModel.price * quantity_item
            }
        }

        holder.itemView.findViewById<Button>(R.id.btn_sub).setOnClickListener() {
            (quantity.text).toString().toInt()

            if ((quantity.text).toString().toInt() == 1) {
                val dialogBuilder = AlertDialog.Builder(context)
                    .setMessage("Bạn chắc chắn muốn bỏ sản phẩm này?")
                    .setPositiveButton("Yes") { _, _ ->
                        // Xử lý khi người dùng chọn Yes
                        reloadCartRecycleView()
                        val call = viewPagerApi.deleteProductInCart(1, ItemsViewModel.id)
                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    // Xóa thành công
                                    println("Xoa ok")
                                    reloadCartRecycleView()
                                    loadDataCart()
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
                        println("no")
                    }.create()

                dialogBuilder.show()
            } else {
                quantity.text = ((quantity.text).toString().toInt() - 1).toString()
                val call = viewPagerApi.updateQuantity(1, ItemsViewModel.id, (quantity.text).toString().toInt())
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        // Xử lý kết quả trả về từ API nếu cần
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Xử lý lỗi nếu có
                    }
                })
                loadDataCart()
            }
        }

        holder.itemView.findViewById<Button>(R.id.btn_sum).setOnClickListener() {
            (quantity.text).toString().toInt()
            quantity.text = ((quantity.text).toString().toInt() + 1).toString()

            val call = viewPagerApi.updateQuantity(1, ItemsViewModel.id, (quantity.text).toString().toInt())
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    // Xử lý kết quả trả về từ API nếu cần
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Xử lý lỗi nếu có
                }
            })
            loadDataCart()
            reloadCartRecycleView()
//            this.notifyDataSetChanged()
        }

        holder.name.text = ItemsViewModel.name
        holder.quantity.text = quantity_item.toString()
        val formatter: NumberFormat = DecimalFormat("#,###")
        holder.price.text = formatter.format(ItemsViewModel.price) + "đ"
        val link = ItemsViewModel.image.substring(1, ItemsViewModel.image.length - 1)
        Glide
            .with(context)
            .load(link)
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
    }
}
