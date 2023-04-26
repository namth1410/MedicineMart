package com.example.medicinemart.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.medicinemart.databinding.ThongTinTaiKhoanBinding
import com.example.medicinemart.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var binding_thong_tin_tai_khoan: ThongTinTaiKhoanBinding


class ThongTinTaiKhoanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_thong_tin_tai_khoan = ThongTinTaiKhoanBinding.inflate(layoutInflater)
        setContentView(binding_thong_tin_tai_khoan.root)


        binding_thong_tin_tai_khoan.edtUsername.text =
            Editable.Factory.getInstance().newEditable(customer.username)
        binding_thong_tin_tai_khoan.edtHoten.text =
            Editable.Factory.getInstance().newEditable(customer.full_name)
        binding_thong_tin_tai_khoan.edtSdt.text =
            Editable.Factory.getInstance().newEditable(customer.phone)

        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_thong_tin_tai_khoan.edtHoten.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_thong_tin_tai_khoan.edtHoten.windowToken, 0)
                binding_thong_tin_tai_khoan.edtHoten.clearFocus()
            } else {

            }
        }

        // Xử lý khi nhấn vào nút cho phép chỉnh sửa Họ tên
        binding_thong_tin_tai_khoan.btnEdithoten.setOnClickListener() {
            binding_thong_tin_tai_khoan.edtHoten.isEnabled = true
            binding_thong_tin_tai_khoan.edtHoten.setTextColor(Color.BLACK)
            binding_thong_tin_tai_khoan.edtHoten.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(
                binding_thong_tin_tai_khoan.edtHoten,
                InputMethodManager.SHOW_IMPLICIT
            )
            binding_thong_tin_tai_khoan.edtHoten.setSelection(binding_thong_tin_tai_khoan.edtHoten.text!!.length)
        }

        // Xử lý khi ô Họ tên thay đổi nội dung
        binding_thong_tin_tai_khoan.edtHoten.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do something before the text changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do something when the text changed
            }

            override fun afterTextChanged(s: Editable?) {
                // Do something after the text changed
                if (s.toString() != "Nguyen Van Tuyen") {
                    binding_thong_tin_tai_khoan.edtHoten.setTextColor(Color.BLACK)
                    binding_thong_tin_tai_khoan.btnSave.visibility = View.VISIBLE
                } else {
                    binding_thong_tin_tai_khoan.edtHoten.setTextColor(Color.GRAY)
                    if (binding_thong_tin_tai_khoan.edtSdt.text.toString() == "0345518088") {
                        binding_thong_tin_tai_khoan.btnSave.visibility = View.GONE
                    }
                }
            }
        })


        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_thong_tin_tai_khoan.edtSdt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_thong_tin_tai_khoan.edtSdt.windowToken, 0)
                binding_thong_tin_tai_khoan.edtSdt.clearFocus()
            }
        }

        // Xử lý khi nhấn vào nút cho phép chỉnh sửa Số ĐT
        binding_thong_tin_tai_khoan.btnEditsdt.setOnClickListener() {
            binding_thong_tin_tai_khoan.edtSdt.isEnabled = true
            binding_thong_tin_tai_khoan.edtSdt.setTextColor(Color.BLACK)
            binding_thong_tin_tai_khoan.edtSdt.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding_thong_tin_tai_khoan.edtSdt, InputMethodManager.SHOW_IMPLICIT)
            binding_thong_tin_tai_khoan.edtSdt.setSelection(binding_thong_tin_tai_khoan.edtSdt.text!!.length)
        }

        // Xử lý khi ô SĐT thay đổi nội dung
        binding_thong_tin_tai_khoan.edtSdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do something before the text changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do something when the text changed
            }

            override fun afterTextChanged(s: Editable?) {
                // Do something after the text changed
                if (s.toString() != "0345518088") {
                    binding_thong_tin_tai_khoan.edtSdt.setTextColor(Color.BLACK)
                    binding_thong_tin_tai_khoan.btnSave.visibility = View.VISIBLE
                } else {
                    binding_thong_tin_tai_khoan.edtSdt.setTextColor(Color.GRAY)
                    if (binding_thong_tin_tai_khoan.edtHoten.text.toString() == "Nguyen Van Tuyen") {
                        binding_thong_tin_tai_khoan.btnSave.visibility = View.GONE
                    }
                }
            }
        })

        binding_thong_tin_tai_khoan.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding_thong_tin_tai_khoan.btnSave.setOnClickListener() {
            binding_thong_tin_tai_khoan.edtHoten.setTextColor(Color.GRAY)
            binding_thong_tin_tai_khoan.edtSdt.setTextColor(Color.GRAY)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding_thong_tin_tai_khoan.root.windowToken, 0)
            binding_thong_tin_tai_khoan.root.clearFocus()

            val call = RetrofitClient.viewPagerApi.updateInfoCus(
                customer.username,
                binding_thong_tin_tai_khoan.edtHoten.text.toString(),
                binding_thong_tin_tai_khoan.edtSdt.text.toString()
            )
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    // Xử lý kết quả trả về từ API nếu cần
                    binding_thong_tin_tai_khoan.btnSave.visibility = View.GONE
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Xử lý lỗi nếu có
                    binding_thong_tin_tai_khoan.btnSave.visibility = View.GONE
                }
            })
        }

    }
}