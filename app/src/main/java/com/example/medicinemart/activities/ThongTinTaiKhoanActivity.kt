package com.example.medicinemart.activities

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.medicinemart.common.Info._username
import com.example.medicinemart.common.Info.customer
import com.example.medicinemart.databinding.ThongTinTaiKhoanBinding
import com.example.medicinemart.models.Customer
import com.example.medicinemart.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

private lateinit var binding_thong_tin_tai_khoan: ThongTinTaiKhoanBinding

private var progressDialog: ProgressDialog? = null

class ThongTinTaiKhoanActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_thong_tin_tai_khoan = ThongTinTaiKhoanBinding.inflate(layoutInflater)
        setContentView(binding_thong_tin_tai_khoan.root)

        binding_thong_tin_tai_khoan.edtUsername.text =
            Editable.Factory.getInstance().newEditable(_username)
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
                if (s.toString() != customer.full_name) {
                    binding_thong_tin_tai_khoan.edtHoten.setTextColor(Color.BLACK)
                    binding_thong_tin_tai_khoan.btnSave.visibility = View.VISIBLE
                } else {
                    binding_thong_tin_tai_khoan.edtHoten.setTextColor(Color.GRAY)
                    if (binding_thong_tin_tai_khoan.edtSdt.text.toString() == customer.phone) {
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
                if (s.toString() != customer.phone) {
                    binding_thong_tin_tai_khoan.edtSdt.setTextColor(Color.BLACK)
                    binding_thong_tin_tai_khoan.btnSave.visibility = View.VISIBLE
                } else {
                    binding_thong_tin_tai_khoan.edtSdt.setTextColor(Color.GRAY)
                    if (binding_thong_tin_tai_khoan.edtHoten.text.toString() == customer.full_name) {
                        binding_thong_tin_tai_khoan.btnSave.visibility = View.GONE
                    }
                }
            }
        })

        binding_thong_tin_tai_khoan.btnBack.setOnClickListener {
            onBackPressed()
        }

        // Xử lý khi nhấn lưu thông tin
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
            progressDialog = ProgressDialog(this)
            progressDialog?.setCancelable(false)
            progressDialog?.setMessage("Đợi xíu...")
            progressDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog?.setProgress(0)
            progressDialog?.show()
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>,
                ) {
                    // Xử lý kết quả trả về từ API nếu cần
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

                                progressDialog?.dismiss()
                                binding_thong_tin_tai_khoan.btnSave.visibility = View.GONE
                                customer.full_name = binding_thong_tin_tai_khoan.edtHoten.text.toString()
                                customer.phone = binding_thong_tin_tai_khoan.edtSdt.text.toString()
                            } else {
                                // Xử lý lỗi nếu thêm hàng mới thất bại
                            }
                        }

                        override fun onFailure(call: Call<Customer>, t: Throwable) {
                            // Xử lý lỗi nếu không thể kết nối tới server
                        }
                    })

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Xử lý lỗi nếu có
                    binding_thong_tin_tai_khoan.btnSave.visibility = View.GONE
                }
            })
        }

        // Xử lý thay đổi avatar
        binding_thong_tin_tai_khoan.avat.setOnClickListener() {
            val dialogBuilder = AlertDialog.Builder(this)
                .setMessage("Bạn muốn lấy ảnh từ?")
                .setPositiveButton("Thư viện ảnh") { _, _ ->
                    // Xử lý khi người dùng chọn Yes
                    openGallery()
                }
                .setNegativeButton("Camera") { _, _ ->
                    // Xử lý khi người dùng chọn No
                    openCamera()
                }.create()

            dialogBuilder.show()
        }

    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)

            println("chap nhan")
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_PICK_IMAGE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val bitmap = data?.extras?.get("data") as Bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                println(byteArray)
                val bitmap1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                val matrix = Matrix()
                matrix.postRotate(90f) // độ xoay (để xoay ngược chiều kim đồng hồ, hãy sử dụng số âm)
                val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                binding_thong_tin_tai_khoan.imageView2.setImageBitmap(rotatedBitmap)
            }
            else if (requestCode == REQUEST_PICK_IMAGE) {
                val uri = data?.getData()
                println(uri)
                binding_thong_tin_tai_khoan.imageView2.setImageURI(uri)
            }
        }
    }
}