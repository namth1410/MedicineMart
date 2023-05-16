package com.example.medicinemart.activities

import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.medicinemart.R
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.password_list
import com.example.medicinemart.common.Info.salt_list
import com.example.medicinemart.databinding.DoimatkhauBinding
import com.example.medicinemart.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public lateinit var binding_doi_mat_khau: DoimatkhauBinding


class DoiMatKhauActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_doi_mat_khau = DoimatkhauBinding.inflate(layoutInflater)
        setContentView(binding_doi_mat_khau.root)

        var salt_csdl = Info.salt_list.get(Info.username_list.indexOf(Info.customer.username))

        binding_doi_mat_khau.oldPassLayout.setEndIconVisible(false)

        binding_doi_mat_khau.edtOldpassword.addTextChangedListener() {
            val hashpassword =
                sha256(binding_doi_mat_khau.edtOldpassword.text.toString(), salt_csdl)
            if ((password_list.indexOf(hashpassword) == Info.username_list.indexOf(Info.customer.username))) {
                binding_doi_mat_khau.oldPassLayout.setEndIconVisible(true)
            } else {
                binding_doi_mat_khau.oldPassLayout.setEndIconVisible(false)
            }
        }

        binding_doi_mat_khau.root.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_doi_mat_khau.root.windowToken, 0)
            }
        }

        binding_doi_mat_khau.edtOldpassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding_doi_mat_khau.oldPassLayout.setEndIconVisible(false)
                val hashpassword =
                    sha256(binding_doi_mat_khau.edtOldpassword.text.toString(), salt_csdl)
                if ((password_list.indexOf(hashpassword) == Info.username_list.indexOf(Info.customer.username))) {
                    binding_doi_mat_khau.oldPassLayout.setEndIconVisible(true)
                } else {
                    binding_doi_mat_khau.oldPassLayout.setEndIconVisible(false)
                }
            } else {
                binding_doi_mat_khau.oldPassLayout.error = ""
                val hashpassword =
                    sha256(binding_doi_mat_khau.edtOldpassword.text.toString(), salt_csdl)
                if ((password_list.indexOf(hashpassword) == Info.username_list.indexOf(Info.customer.username))) {
                    binding_doi_mat_khau.oldPassLayout.setEndIconVisible(true)
                } else {
                    binding_doi_mat_khau.oldPassLayout.setEndIconVisible(false)
                }
            }
        }

        binding_doi_mat_khau.edtNewpassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding_doi_mat_khau.newPassLayout.setEndIconVisible(false)
            } else {
                binding_doi_mat_khau.newPassLayout.error = ""
                binding_doi_mat_khau.newPassLayout.setEndIconVisible(true)
            }
        }

        binding_doi_mat_khau.edtRenewpassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding_doi_mat_khau.renewPassLayout.setEndIconVisible(false)
            } else {
                binding_doi_mat_khau.renewPassLayout.error = ""
                binding_doi_mat_khau.renewPassLayout.setEndIconVisible(true)
            }
        }

        binding_doi_mat_khau.btnSave.setOnClickListener() {
            val old_pass = binding_doi_mat_khau.edtOldpassword.text.toString()
            val new_pass = binding_doi_mat_khau.edtNewpassword.text.toString()
            val renew_pass = binding_doi_mat_khau.edtRenewpassword.text.toString()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding_doi_mat_khau.root.windowToken, 0)
            binding_doi_mat_khau.root.clearFocus()

            val hashpassword =
                sha256(binding_doi_mat_khau.edtOldpassword.text.toString(), salt_csdl)
            if (Info.PASSWORD_PATTERN.matches(new_pass) && checkRePassword(
                    new_pass,
                    renew_pass
                ) && (password_list.indexOf(hashpassword) == Info.username_list.indexOf(Info.customer.username))
            ) {
                var salt = generateSalt() // tạo salt
                val hashedPassword = sha256(new_pass, salt) // tạo hash SHA-256
                // Lưu salt và hashedPassword vào cơ sở dữ liệu
                val call = RetrofitClient.viewPagerApi.changePass(
                    Info.customer.username,
                    hashedPassword,
                    salt
                )
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            // Xử lý kết quả trả về nếu thêm hàng mới thành công
                            println("Dang ky thanh cong")
                            Toast.makeText(
                                this@DoiMatKhauActivity,
                                "Đổi mật khẩu thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                            password_list.set(
                                password_list.indexOf(sha256(old_pass, salt_csdl)),
                                hashedPassword
                            )
                            salt_list.set(salt_list.indexOf(salt_csdl), salt)
                            val editor = Info.sharedPref.edit()
                            if (Info.sharedPref.contains("username")) {
                                editor.remove("password")
                                editor.putString("password", new_pass)
                                editor.apply()
                            }

                            onBackPressed()
                        } else {
                            // Xử lý lỗi nếu thêm hàng mới thất bại
                            println("Loi dang ky")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Xử lý lỗi nếu không thể kết nối tới server
                    }
                })
            }

            if (old_pass.isNullOrEmpty()) {
                binding_doi_mat_khau.oldPassLayout.error = "Không được để trống!"
            } else {
                val hashpassword =
                    sha256(binding_doi_mat_khau.edtOldpassword.text.toString(), salt_csdl)
                if (!(password_list.indexOf(hashpassword) == Info.username_list.indexOf(Info.customer.username))) {
                    binding_doi_mat_khau.oldPassLayout.error = "Mật khẩu không đúng!"
                } else {
                    binding_doi_mat_khau.oldPassLayout.error = ""
                }
            }

            if (new_pass.isNullOrEmpty()) {
                binding_doi_mat_khau.newPassLayout.error = "Không được để trống!"
            } else {
                if (new_pass.length < 6) {
                    binding_doi_mat_khau.newPassLayout.error = "Ít nhất 6 ký tự!"
                } else if (new_pass.length > 15) {
                    binding_doi_mat_khau.newPassLayout.error = "Không quá 15 ký tự!"
                }

                if (new_pass.contains(" ")) {
                    binding_doi_mat_khau.newPassLayout.error = "Không được chứa dấu cách!"
                }
            }

            if (new_pass.isNullOrEmpty() || !checkRePassword(new_pass, renew_pass)) {
                binding_doi_mat_khau.renewPassLayout.error = "Mật khẩu không trùng khớp!"
            }
        }



        binding_doi_mat_khau.btnBack.setOnClickListener() {
            val animation = AnimationUtils.loadAnimation(this, R.anim.btn_back)
            binding_doi_mat_khau.btnBack.startAnimation(animation)
            onBackPressed()
        }
    }
}