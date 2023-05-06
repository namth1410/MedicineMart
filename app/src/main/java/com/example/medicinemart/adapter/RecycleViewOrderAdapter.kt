package com.example.medicinemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medicinemart.R
import com.example.medicinemart.models.Sanpham
import java.text.DecimalFormat
import java.text.NumberFormat

@Suppress("UNREACHABLE_CODE")
class RecycleViewOrderAdapter (private val mList: List<Sanpham>, private val quantity_array: List<Int>, private  val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecycleViewOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecycleViewOrderAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.donhang_item, parent, false)

        return RecycleViewOrderAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecycleViewOrderAdapter.ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        val quantity_item = quantity_array[position]


        holder.name.text = ItemsViewModel.name
        val formatter: NumberFormat = DecimalFormat("#,###")
        holder.price.text = formatter.format(ItemsViewModel.price) + "đ"
        holder.quantity.text = "x" + formatter.format(quantity_item)
        holder.total_price.text = formatter.format(ItemsViewModel.price * quantity_item) + "đ"
        if (ItemsViewModel.image.startsWith("\"") && ItemsViewModel.image.endsWith("\"")) {
            ItemsViewModel.image =
                ItemsViewModel.image.substring(1, ItemsViewModel.image.length - 1)
        }
        Glide
            .with(context)
            .load(ItemsViewModel.image)
            .into(holder.imageView)
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview_order)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val total_price: TextView = itemView.findViewById(R.id.total_price)
    }

}