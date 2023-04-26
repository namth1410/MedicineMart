package com.example.medicinemart.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.databinding.DangkyBinding
import com.example.medicinemart.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

private lateinit var binding_dang_ky: DangkyBinding

val USERNAME_PATTERN = "^[a-zA-Z0-9]{6,30}\$".toRegex()
val PASSWORD_PATTERN = "^\\S{6,15}\$".toRegex()

var username_list = ArrayList<String>()
var password_list = ArrayList<String>()
var salt_list = ArrayList<String>()

fun getAccounts() {
    GlobalScope.launch(Dispatchers.IO) {
        val getAccounts = async { RetrofitClient.viewPagerApi.getAccounts() }
        val res_getAccounts = getAccounts.await().body()
        username_list.clear()
        password_list.clear()
        salt_list.clear()
        for (i in res_getAccounts!!) {
            username_list.add(i.get("username").asString)
        }
        for (i in res_getAccounts!!) {
            password_list.add(i.get("password").asString)
        }
        for (i in res_getAccounts!!) {
            salt_list.add(i.get("salt").asString)
        }
    }
}
fun isUsernamePasswordValid(username: String, password: String): Boolean {
    return USERNAME_PATTERN.matches(username) && PASSWORD_PATTERN.matches(password)
}

// Kiểm tra xem có ký tự đặc biệt không
fun hasSpecialChar(input: String): Boolean {
    val regex = Regex("[^a-zA-Z0-9 ]")
    return regex.containsMatchIn(input)
}
fun checkRePassword (password: String, repassword: String): Boolean {
    if (password == repassword) {
        return true
    }
    return false
}

// Hàm tạo salt
fun generateSalt(): String {
    val random = SecureRandom()
    val salt = ByteArray(16)
    random.nextBytes(salt)
    return Base64.getEncoder().encodeToString(salt)
}

// Hàm tạo hash SHA-256
fun sha256(input: String, salt: String): String {
    val bytes = (input + salt).toByteArray(Charset.defaultCharset())
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}

fun hexStringToByteArray(hexString: String): ByteArray {
    val len = hexString.length
    val byteArray = ByteArray(len / 2)
    for (i in 0 until len step 2) {
        byteArray[i / 2] = ((Character.digit(hexString[i], 16) shl 4) +
                Character.digit(hexString[i + 1], 16)).toByte()
    }
    return byteArray
}
// Hàm xử lý đăng ký
fun register(username: String, password: String) {
    var salt = generateSalt() // tạo salt
    println(password)
    val hashedPassword = sha256(password, salt) // tạo hash SHA-256
    // Lưu salt và hashedPassword vào cơ sở dữ liệu
    val call = RetrofitClient.viewPagerApi.signUp(username, hashedPassword, salt)
    call.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                // Xử lý kết quả trả về nếu thêm hàng mới thành công
                println("Dang ky thanh cong")
                getAccounts()
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

class DangKyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_dang_ky = DangkyBinding.inflate(layoutInflater)
        setContentView(binding_dang_ky.root)

        binding_dang_ky.dacotaikhoan.setOnClickListener {
            val intent = Intent(this@DangKyActivity, DangNhapActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Animatoo.animateSlideRight(this)
            finish()
        }

        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_dang_ky.edtUsername.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_dang_ky.edtUsername.windowToken, 0)
                binding_dang_ky.edtUsername.clearFocus()
            } else {
                binding_dang_ky.errUsername.text = ""
            }
        }
        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_dang_ky.edtPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_dang_ky.edtPassword.windowToken, 0)
                binding_dang_ky.edtPassword.clearFocus()
            } else {
                binding_dang_ky.errPassword.text = ""
            }
        }
        // Xử lý khi ấn ra ngoài EditText thì ẩn bàn phím và bỏ focus
        binding_dang_ky.edtRepassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_dang_ky.edtRepassword.windowToken, 0)
                binding_dang_ky.edtRepassword.clearFocus()
            } else {
                binding_dang_ky.errRepassword.text = ""
            }
        }

        // Xử lý khi nhấn vào nút Đăng Ký
        binding_dang_ky.btnDangky.setOnClickListener {
            val username = binding_dang_ky.edtUsername.text.toString()
            val password = binding_dang_ky.edtPassword.text.toString()
            val repassword = binding_dang_ky.edtRepassword.text.toString()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding_dang_ky.root.windowToken, 0)
            binding_dang_ky.root.clearFocus()

            if (username_list.contains(username)) {
                binding_dang_ky.errUsername.text = "Tài khoản đã tồn tại !"
                return@setOnClickListener
            }

            if (isUsernamePasswordValid(username, password) && checkRePassword(password, repassword)) {
                register(username, password)
                Toast.makeText(this@DangKyActivity, "Đăng ký thành công, mời bạn đăng nhập lại", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@DangKyActivity, DangNhapActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Animatoo.animateSlideRight(this)
                finish()
            } else {
                println("Loi roi con oi")
                if (username.isNullOrEmpty()) {
                    binding_dang_ky.errUsername.text = "Bạn chưa nhập tài khoản !"
                } else {
                    println(username.length)
                    if (username.length < 6) {
                        binding_dang_ky.errUsername.text = "Ít nhất 6 ký tự !"
                    } else if (username.length > 30) {
                        binding_dang_ky.errUsername.text = "Không quá 30 ký tự !"
                    }

                    if (hasSpecialChar(username) || username.contains(" ")) {
                        binding_dang_ky.errUsername.text = "Không được chứa ký tự đặc biệt !"
                    }
                }
                if (password.isNullOrEmpty()) {
                    binding_dang_ky.errPassword.text = "Bạn chưa nhập mật khẩu !"
                } else {
                    if (password.length < 6) {
                        binding_dang_ky.errPassword.text = "Ít nhất 6 ký tự !"
                    } else if (password.length > 15) {
                        binding_dang_ky.errPassword.text = "Không quá 15 ký tự !"
                    }

                    if (password.contains(" ")) {
                        binding_dang_ky.errRepassword.text = "Không được chứa dấu cách !"
                    }
                }
                if (repassword.isNullOrEmpty() || !checkRePassword(password, repassword)) {
                    binding_dang_ky.errRepassword.text = "Mật khẩu không trùng khớp !"
                }
            }
        }
    }
}