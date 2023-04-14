package com.example.medicinemart.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.medicinemart.R
import com.example.medicinemart.models.Sanpham

class DonHangAdapter(private val context: Activity, private val arraylist: ArrayList<Sanpham>) :
    ArrayAdapter<Sanpham>(context, R.layout.donhang_item, arraylist) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.donhang_item, parent, false)
        val imageView: ImageView = view.findViewById(R.id.image)
        val ten : TextView = view.findViewById(R.id.name)
        val mota : TextView = view.findViewById(R.id.mota)

        //imageView.setImageResource(arraylist[position].imageId)
        ten.text = arraylist[position].name
        mota.text = arraylist[position].describe

        return view
    }

    override fun add(`object`: Sanpham?) {
        super.add(`object`)
    }
}