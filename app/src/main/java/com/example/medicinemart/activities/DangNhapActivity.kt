package com.example.medicinemart.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.R
import com.example.medicinemart.databinding.DangnhapBinding
import com.example.medicinemart.models.Customer
import com.example.medicinemart.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var binding_dang_nhap: DangnhapBinding

var _username = ""
var customer = Customer()
fun getInfoCustomer() {
    val call = RetrofitClient.viewPagerApi.getInfoCustomer(_username)
    println("_username " + _username)
    call.enqueue(object : Callback<Customer> {
        override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
            if (response.isSuccessful) {
                // Xử lý kết quả trả về nếu thêm hàng mới thành công
                println(response.body().toString())
                customer.username = response.body()!!.username
                customer.phone = response.body()!!.phone
                customer.email = response.body()!!.email
                customer.full_name = response.body()!!.full_name
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

//        val intent = Intent(this, TrangChuActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//        Animatoo.animateSlideRight(this)
//        finish()

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            // Có kết nối mạng, thực hiện các thao tác yêu cầu mạng ở đây
            getAccounts()
        } else {
            // Không có kết nối mạng, hiển thị thông báo hoặc thông báo cho người dùng biết
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)

            val view = layoutInflater.inflate(R.layout.dialog_no_network, null)
            val closeButton = view.findViewById<Button>(R.id.dialog_close_button)

            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

            closeButton.setOnClickListener {
                dialog.dismiss()
            }

        }

        // Xử lý khi ấn vào "Chưa có tài khoản"
        binding_dang_nhap.chuacotaikhoan.setOnClickListener {
            val intent = Intent(this@DangNhapActivity, DangKyActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            //finish()
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
            println(hashpassword)
            println(username_list.indexOf(username))
            if (!(password_list.indexOf(hashpassword) == username_list.indexOf(username))) {
                binding_dang_nhap.errPassword.text = "Mật khẩu không đúng !"
                return@setOnClickListener
            }

            if (isUsernamePasswordValid(username, password)) {
                _username = username
                getInfoCustomer()
                val intent = Intent(this, TrangChuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Animatoo.animateSlideRight(this)
                finish()
            }
        }
    }
}