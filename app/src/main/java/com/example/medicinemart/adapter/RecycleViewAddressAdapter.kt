package com.example.medicinemart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicinemart.R
import com.example.medicinemart.models.Address


class RecycleViewAddressAdapter(private val mList: List<Address>, private  val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecycleViewAddressAdapter.ViewHolder>(){

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diachi_item, parent, false)

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
        holder.hoten.text = Editable.Factory.getInstance().newEditable(ItemsViewModel.full_name)
        holder.sdt.text = Editable.Factory.getInstance().newEditable(ItemsViewModel.phone)
        holder.diachi.text = Editable.Factory.getInstance().newEditable(ItemsViewModel.location)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val hoten: TextView = itemView.findViewById(R.id.full_name)
        val sdt: TextView = itemView.findViewById(R.id.phone)
        val diachi: TextView = itemView.findViewById(R.id.location)
    }
}
