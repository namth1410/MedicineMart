package com.example.medicinemart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicinemart.R
import com.example.medicinemart.activities.ChatApplication
import com.example.medicinemart.models.Customer

class CustomerAdapter(val context: Context, val userlist: ArrayList<Customer>):
    RecyclerView.Adapter<CustomerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.customer_list, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val currentUser = userlist[position]

        holder.textName.text = currentUser.full_name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatApplication::class.java)
            intent.putExtra("name", currentUser.full_name)
            intent.putExtra("id", currentUser.id)

            context.startActivity(intent)
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById<TextView>(R.id.txt_name)
    }
}