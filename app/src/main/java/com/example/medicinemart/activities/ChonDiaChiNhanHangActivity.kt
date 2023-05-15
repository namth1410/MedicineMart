package com.example.medicinemart.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.R
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewAddressAdapter
import com.example.medicinemart.common.Info.delivery_address
import com.example.medicinemart.common.Info.list_address
import com.example.medicinemart.databinding.ChondiachinhanhangBinding

private lateinit var binding_chon_dia_chi_nhan_hang: ChondiachinhanhangBinding


class ChonDiaChiNhanHangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_chon_dia_chi_nhan_hang = ChondiachinhanhangBinding.inflate(layoutInflater)
        setContentView(binding_chon_dia_chi_nhan_hang.root)

        if (list_address.isEmpty()) {
            binding_chon_dia_chi_nhan_hang.emptyAddress.visibility = View.VISIBLE
            val spannable = SpannableString(binding_chon_dia_chi_nhan_hang.tvEmpty.text)
            val startIndex = binding_chon_dia_chi_nhan_hang.tvEmpty.text.indexOf("Hồ Sơ")
            val endIndex = startIndex + "Hồ Sơ".length

            spannable.setSpan(
                ForegroundColorSpan(Color.BLUE),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                ForegroundColorSpan(Color.BLACK),
                0,
                startIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                ForegroundColorSpan(Color.BLACK),
                endIndex,
                binding_chon_dia_chi_nhan_hang.tvEmpty.text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding_chon_dia_chi_nhan_hang.tvEmpty.text = spannable


        } else {
            binding_chon_dia_chi_nhan_hang.emptyAddress.visibility = View.GONE
        }

        binding_chon_dia_chi_nhan_hang.tvThemdiachi.setPaintFlags(binding_chon_dia_chi_nhan_hang.tvThemdiachi.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding_chon_dia_chi_nhan_hang.tvThemdiachi.setOnClickListener() {
            val intent = Intent(this, DiaChiMoiActivity::class.java)
            intent.putExtra("goto", "chondiachinhanhang")
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        val adapterRecyclerAddressAdapter= RecycleViewAddressAdapter(list_address, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
                delivery_address = list_address.get(position)
                onBackPressed()
            }
        })

//        binding_gio_hang.recyclerView.isNestedScrollingEnabled = true
        binding_chon_dia_chi_nhan_hang.recyclerView.layoutManager = LinearLayoutManager(this)

        val VerticalLayout = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding_chon_dia_chi_nhan_hang.recyclerView.setLayoutManager(VerticalLayout)
        binding_chon_dia_chi_nhan_hang.recyclerView.adapter = adapterRecyclerAddressAdapter


        binding_chon_dia_chi_nhan_hang.btnBack.setOnClickListener() {
            val anim = AnimationUtils.loadAnimation(this, R.anim.btn_back)
            binding_chon_dia_chi_nhan_hang.btnBack.startAnimation(anim)
            onBackPressed()
        }
    }
}