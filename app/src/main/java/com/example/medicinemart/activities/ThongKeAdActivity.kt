package com.example.medicinemart.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.medicinemart.R
import com.example.medicinemart.databinding.ThongkeAdminBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView


private lateinit var binding_thongke_ad: ThongkeAdminBinding

class ThongKeAdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_thongke_ad = ThongkeAdminBinding.inflate(layoutInflater)
        setContentView(binding_thongke_ad.root)

        val barChart = findViewById<BarChart>(R.id.bar_chart)

// Create an array of data entries
        val entries = arrayListOf<BarEntry>()
        entries.add(BarEntry(0f, 3f))
        entries.add(BarEntry(1f, 4f))
        entries.add(BarEntry(2f, 2f))
        entries.add(BarEntry(3f, 6f))
        entries.add(BarEntry(4f, 8f))
        entries.add(BarEntry(5f, 4f))
        entries.add(BarEntry(6f, 10f))

// Create a BarDataSet from the data entries
        val dataSet = BarDataSet(entries, "Sales Data")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK

// Set the data and styling properties of the chart
        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.setTouchEnabled(true)
        barChart.setPinchZoom(true)

// Set the X and Y axis labels and styling properties
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul"))
        xAxis.textColor = Color.BLACK

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.textColor = Color.BLACK

        val yAxisRight = barChart.axisRight
        yAxisRight.isEnabled = false

        // Update the chart
        barChart.invalidate()

        binding_thongke_ad.bottomNavigationView.setSelectedItemId(R.id.thongke)
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // put your code here
                    val intent = Intent(this@ThongKeAdActivity, TrangChuAdActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.chat -> {
                    // put your code here
                    val intent = Intent(this@ThongKeAdActivity, ChatAdActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.chucnang -> {
                    // put your code here
                    val intent = Intent(this@ThongKeAdActivity, ChucNangAdActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding_thongke_ad.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        /*binding_thongke_ad.btnDoanhThu.setOnClickListener {
            handlerButton(binding_thongke_ad.btnDoanhThu)
            handlerData(binding_thongke_ad.btnDoanhThu)
        }

        binding_thongke_ad.btnLuongKhachHang.setOnClickListener {
            handlerButton(binding_thongke_ad.btnLuongKhachHang)
            handlerData(binding_thongke_ad.btnLuongKhachHang)
        }
         */
    }

}