package com.example.medicinemart.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.R
import com.example.medicinemart.common.Info._username
import com.example.medicinemart.common.Info.customer
import com.example.medicinemart.common.Info.password_list
import com.example.medicinemart.common.Info.salt_list
import com.example.medicinemart.common.Info.sharedPref
import com.example.medicinemart.common.Info.username_list
import com.example.medicinemart.databinding.DangnhapBinding
import com.example.medicinemart.models.Customer
import com.example.medicinemart.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var binding_dang_nhap: DangnhapBinding


fun getInfoCustomer() {
    val call = RetrofitClient.viewPagerApi.getInfoCustomer(_username)
    call.enqueue(object : Callback<Customer> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
            if (response.isSuccessful) {
                // Xử lý kết quả trả về nếu thêm hàng mới thành công
                customer.id = response.body()!!.id
                customer.username = response.body()!!.username
                customer.phone = response.body()!!.phone
                customer.email = response.body()!!.email
                customer.full_name = response.body()!!.full_name
//                loadDataCart()
//                loadDataDonhang()
//                getAddressFromDB()
//                getNotification()
            } else {
                // Xử lý lỗi nếu thêm hàng mới thất bại
            }
        }

        override fun onFailure(call: Call<Customer>, t: Throwable) {
            // Xử lý lỗi nếu không thể kết nối tới server
        }
    })
}
class DangNhapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_dang_nhap = DangnhapBinding.inflate(layoutInflater)
        setContentView(binding_dang_nhap.root)

        // Lấy SharedPreferences dựa trên tên file "my_prefs"
        sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        val username = sharedPref.getString("username", "")
        val password = sharedPref.getString("password", "")

        if (username!!.isNotEmpty() && password!!.isNotEmpty()) {
            // Thực hiện đăng nhập tự động
            _username = username
            customer.username = _username
            getInfoCustomer()
            val intent = Intent(this, PreLoadingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Animatoo.animateSlideRight(this)
            finish()
        }

//        val broadcastReceiver = NetworkChangeReceiver()
//        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        registerReceiver(broadcastReceiver, intentFilter)


        // Kiểm tra kết nối mạng
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

//        if (networkInfo != null && networkInfo.isConnected) {
//            // Có kết nối mạng, thực hiện các thao tác yêu cầu mạng ở đây
//            getAccounts()
//        } else {
//            // Không có kết nối mạng, hiển thị thông báo hoặc thông báo cho người dùng biết
//            val builder = AlertDialog.Builder(this)
//            builder.setCancelable(false)
//
//            val view = layoutInflater.inflate(R.layout.dialog_no_network, null)
//            val closeButton = view.findViewById<Button>(R.id.dialog_close_button)
//
//            builder.setView(view)
//            val dialog = builder.create()
//            dialog.show()
//
//            closeButton.setOnClickListener {
//                dialog.dismiss()
//            }
//
//        }

        // Xử lý khi ấn vào "Chưa có tài khoản"
        binding_dang_nhap.chuacotaikhoan.setOnClickListener {
            binding_dang_nhap.chuacotaikhoan.setTextColor(ContextCompat.getColor(this, R.color.blue))
            val intent = Intent(this@DangNhapActivity, DangKyActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_dang_nhap.edtUsername.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_dang_nhap.edtUsername.windowToken, 0)
                binding_dang_nhap.edtUsername.clearFocus()
            }  else {
                binding_dang_nhap.errUsername.text = ""
            }
        }
        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_dang_nhap.edtPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_dang_nhap.edtPassword.windowToken, 0)
                binding_dang_nhap.edtPassword.clearFocus()
            }  else {
                binding_dang_nhap.errPassword.text = ""
            }
        }

        // Xử lý ấn vào nút hiện mật khẩu
        binding_dang_nhap.showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            var cursorPosition: Int = 0
            cursorPosition = binding_dang_nhap.edtPassword.selectionEnd
            if (isChecked) {
                binding_dang_nhap.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding_dang_nhap.edtPassword.setSelection(cursorPosition)
            } else {
                binding_dang_nhap.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding_dang_nhap.edtPassword.setSelection(cursorPosition)
            }
        }

        // Xử lý khi ấn vào nút Đăng Nhập
        binding_dang_nhap.btnDangnhap.setOnClickListener {

            val animation = AnimationUtils.loadAnimation(this, R.anim.btn_anim)
            binding_dang_nhap.btnDangnhap.startAnimation(animation)

            var username = binding_dang_nhap.edtUsername.text.toString()
            var password = binding_dang_nhap.edtPassword.text.toString()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding_dang_nhap.root.windowToken, 0)
            binding_dang_nhap.root.clearFocus()

            if (username.isNullOrEmpty()) {
                binding_dang_nhap.errUsername.text = "Bạn chưa nhập tài khoản !"
            }
            if (password.isNullOrEmpty()) {
                binding_dang_nhap.errPassword.text = "Bạn chưa nhập mật khẩu !"
                return@setOnClickListener
            }

            if (!username_list.contains(username)) {
                binding_dang_nhap.errUsername.text = "Tài khoản không tồn tại !"
                return@setOnClickListener
            }

            var salt_csdl = salt_list.get(username_list.indexOf(username))
            val hashpassword = sha256(password, salt_csdl)
            if (!(password_list.indexOf(hashpassword) == username_list.indexOf(username))) {
                binding_dang_nhap.errPassword.text = "Mật khẩu không đúng !"
                return@setOnClickListener
            }

            if (isUsernamePasswordValid(username, password)) {
                // Lưu trữ thông tin đăng nhập của người dùng
                val editor = sharedPref.edit()
                editor.putString("username", username)
                editor.putString("password", password)
                editor.apply()

                _username = username
                getInfoCustomer()
                val intent = Intent(this, PreLoadingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Animatoo.animateSlideRight(this)
                finish()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        binding_dang_nhap.chuacotaikhoan.setTextColor(ContextCompat.getColor(this, R.color.grey))
    }
}
