package com.example.medicinemart.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.widget.Button
import com.example.medicinemart.R

class NetworkChangeReceiver : BroadcastReceiver() {
    private var isConnected = false

    override fun onReceive(context: Context, intent: Intent) {
        if (isNetworkAvailable(context)) {
            // Kết nối internet trở lại
            println(isConnected)
            if (!this.isConnected) {
                this.isConnected = true
                val currentActivity = (context as Activity).also {
                    it.recreate()
                }
            }
        } else {
            // Không có kết nối internet
            if (this.isConnected) {
                this.isConnected = false
            }
            showNetworkLostNotification(context)

        }
    }


    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
    private fun showNetworkLostNotification(context: Context) {
        // Tạo và hiển thị thông báo mất kết nối mạng
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_no_network, null)
        val closeButton = view.findViewById<Button>(R.id.dialog_close_button)

        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}
