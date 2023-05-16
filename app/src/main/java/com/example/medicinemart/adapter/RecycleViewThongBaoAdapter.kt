package com.example.medicinemart.adapter

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.medicinemart.R
import com.example.medicinemart.common.Info
import com.example.medicinemart.models.Notification

@Suppress("UNREACHABLE_CODE")
class RecycleViewThongBaoAdapter (private val mList: List<Notification>, private  val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecycleViewThongBaoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecycleViewThongBaoAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.thongbao_item, parent, false)

        return RecycleViewThongBaoAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: RecycleViewThongBaoAdapter.ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
//        val quantity_item = quantity_array[position]

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }

        holder.title.text = ItemsViewModel.title

        val builder = SpannableStringBuilder(ItemsViewModel.content)
        val regex = Regex("\\d+")
        val matches = regex.findAll(ItemsViewModel.content)
        for (match in matches) {
            val start = match.range.first
            val end = match.range.last + 1
            val blueSpan = ForegroundColorSpan(Color.BLUE)
            builder.setSpan(blueSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        holder.content.text = builder

        holder.time.text = ItemsViewModel.time.format(Info.time_formatter).toString()

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
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val title: TextView = itemView.findViewById(R.id.title)
        val content: TextView = itemView.findViewById(R.id.content)
        val time: TextView = itemView.findViewById(R.id.time)
    }

}