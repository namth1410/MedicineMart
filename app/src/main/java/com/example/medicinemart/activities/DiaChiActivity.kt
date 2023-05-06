package com.example.medicinemart.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewAddressAdapter
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.list_address
import com.example.medicinemart.databinding.DiachiBinding
import com.example.medicinemart.models.Address
import java.io.Serializable

private lateinit var binding_dia_chi: DiachiBinding
//private lateinit var mapView: MapView
//private lateinit var googleMap: GoogleMap
//private lateinit var centerMarker: ImageView

private var themdiachi = "themdiachi"
private var chitietdiachi = "chitietdiachi"

class DiaChiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_dia_chi = DiachiBinding.inflate(layoutInflater)
        setContentView(binding_dia_chi.root)

        checkList(list_address)

        val adapterRecyclerAddressAdapter= RecycleViewAddressAdapter(list_address, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Xử lý sự kiện click ở đây
                val intent = Intent(this@DiaChiActivity, ChiTietDiaChiActivity::class.java)
                Info.position = position
                intent.putExtra("position", Info.position as Serializable)

                startActivity(intent)
                Animatoo.animateSlideLeft(this@DiaChiActivity)
            }
        })

//        binding_gio_hang.recyclerView.isNestedScrollingEnabled = true
        binding_dia_chi.recyclerView.layoutManager = LinearLayoutManager(this)

        val VerticalLayout = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding_dia_chi.recyclerView.setLayoutManager(VerticalLayout)
        binding_dia_chi.recyclerView.adapter = adapterRecyclerAddressAdapter

        binding_dia_chi.btnThemdiachi.setOnClickListener() {
            val intent = Intent(this, DiaChiMoiActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        binding_dia_chi.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        checkList(list_address)
        binding_dia_chi.recyclerView.adapter?.notifyDataSetChanged()
    }

    fun checkList(list: ArrayList<Address>) {
        if (list.isEmpty()) {
            binding_dia_chi.layoutEmpty.visibility = View.VISIBLE
            binding_dia_chi.recyclerView.visibility = View.INVISIBLE

        } else {
            binding_dia_chi.layoutEmpty.visibility = View.INVISIBLE
            binding_dia_chi.recyclerView.visibility = View.VISIBLE
        }
    }
}