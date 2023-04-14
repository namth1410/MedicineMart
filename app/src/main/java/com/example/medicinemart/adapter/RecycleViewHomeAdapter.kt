package com.example.medicinemart.adapter

import android.annotation.SuppressLint
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

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class RecycleViewHomeAdapter(private val mList: List<Sanpham>, private  val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecycleViewHomeAdapter.ViewHolder>(){

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        //holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
        holder.name.text = ItemsViewModel.name
        val formatter: NumberFormat = DecimalFormat("#,###")
        holder.price.text = formatter.format(ItemsViewModel.price) + "đ"
        Glide
            .with(context)
            .load(ItemsViewModel.image)
            .into(holder.imageView)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
    }
}
