package com.example.medicinemart.activities

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.medicinemart.R
import com.example.medicinemart.common.Info
import com.example.medicinemart.common.Info.customer
import com.example.medicinemart.common.Info.list_address
import com.example.medicinemart.common.Info.path_avat
import com.example.medicinemart.common.Info.sharedPref
import com.example.medicinemart.databinding.HosoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

private lateinit var binding_ho_so: HosoBinding


class HoSoActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ho_so = HosoBinding.inflate(layoutInflater)
        setContentView(binding_ho_so.root)

        var badge = binding_ho_so.bottomNavigationView.getOrCreateBadge(R.id.thongbao)
        if (Info.so_thong_bao_chua_doc == 0) {
            badge.isVisible = false
        } else {
            badge.isVisible = true
            badge.number = Info.so_thong_bao_chua_doc
        }

        path_avat = "image_path" + customer.username

        val imagePath = sharedPref.getString(path_avat, "")
        if (imagePath != "") {
            // Tạo một đối tượng File từ đường dẫn đã lưu
            val imageFile = File(imagePath)

            // Đọc dữ liệu ảnh từ file và tạo ra một đối tượng Bitmap
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            // Sử dụng đối tượng Bitmap này trong ứng dụng của bạn
            binding_ho_so.imageView2.setImageBitmap(bitmap)
        }

        println(list_address)

        binding_ho_so.tvFullname.text = customer.full_name
        binding_ho_so.bottomNavigationView.setSelectedItemId(R.id.hoso)
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.thongbao -> {
                    // put your code here
                    val intent = Intent(this@HoSoActivity, ThongBaoActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.donhang -> {
                    // put your code here
                    val intent = Intent(this@HoSoActivity, DonHangActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.home -> {
                    // put your code here
                    val intent = Intent(this@HoSoActivity, TrangChuActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding_ho_so.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // Xử lý sự kiện bấm vào Tài khoản
        binding_ho_so.tvTaikhoan.setOnClickListener() {
            val intent = Intent(this@HoSoActivity, ThongTinTaiKhoanActivity::class.java)
            startActivity(intent)
            val anim = AnimationUtils.loadAnimation(this, R.anim.btn_go)
            binding_ho_so.arrow1.startAnimation(anim)
            Animatoo.animateSlideLeft(this)

//            overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
        }

        // Xử lý sự kiện bấm vào Địa chỉ
        binding_ho_so.tvDiachi.setOnClickListener() {
            val intent = Intent(this@HoSoActivity, DiaChiActivity::class.java)
            startActivity(intent)
            val anim = AnimationUtils.loadAnimation(this, R.anim.btn_go)
            binding_ho_so.arrow2.startAnimation(anim)
            Animatoo.animateSlideLeft(this)
//            overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
        }

        // Xử lý sự kiện bấm vào Hỗ trợ
        binding_ho_so.tvHotro.setOnClickListener() {
            val intent = Intent(this@HoSoActivity, HoTroActivity::class.java)
            startActivity(intent)
            val anim = AnimationUtils.loadAnimation(this, R.anim.btn_go)
            binding_ho_so.arrow3.startAnimation(anim)
            Animatoo.animateSlideLeft(this)

//            overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
        }

        binding_ho_so.tvDoimatkhau.setOnClickListener() {
            val intent = Intent(this@HoSoActivity, DoiMatKhauActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)

//            overridePendingTransition(R.anim.no_animation,  R.anim.no_animation)
        }

        // Xử lý sự kiện bấm Đăng xuất
        binding_ho_so.tvDangxuat.setOnClickListener() {
            val editor = sharedPref.edit()

            val builder = android.app.AlertDialog.Builder(this)
            builder.setCancelable(false)
            val layoutInflater = LayoutInflater.from(this)
            val view = layoutInflater.inflate(R.layout.dialog_logout, null)
            val closeButton = view.findViewById<Button>(R.id.dialog_close_button)
            val okButton = view.findViewById<Button>(R.id.dialog_ok_button)

            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            closeButton.setOnClickListener {
                dialog.dismiss()
            }

            okButton.setOnClickListener {
                if (sharedPref.contains("username")) {
                    editor.remove("username")
                    editor.remove("password")
                    editor.apply()
                }

                val intent = Intent(this, DangNhapActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
                finish()
            }

            /*
            val dialogBuilder = AlertDialog.Builder(this)
                .setMessage("Đăng xuất khỏi tài khoản của bạn?")
                .setPositiveButton("Đăng xuất") { _, _ ->
                    // Xử lý khi người dùng chọn Yes
                    editor.remove("username")
                    editor.remove("password")
                    editor.apply()

                    val intent = Intent(this, DangNhapActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Animatoo.animateSlideLeft(this)
                    finish()
                }
                .setNegativeButton("Hủy") { _, _ ->
                    // Xử lý khi người dùng chọn No
                    println("no")
                }.create()

            dialogBuilder.show()
            println("Da dang xuat")  */
        }

    }

    override fun onResume() {
        super.onResume()
        binding_ho_so.bottomNavigationView.setSelectedItemId(R.id.hoso)

        val imagePath = sharedPref.getString(path_avat, "")
        if (imagePath != "") {
            // Tạo một đối tượng File từ đường dẫn đã lưu
            val imageFile = File(imagePath)

            // Đọc dữ liệu ảnh từ file và tạo ra một đối tượng Bitmap
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            val matrix = Matrix()
            matrix.postRotate(90f) // độ xoay (để xoay ngược chiều kim đồng hồ, hãy sử dụng số âm)
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            // Sử dụng đối tượng Bitmap này trong ứng dụng của bạn
            binding_ho_so.imageView2.setImageBitmap(rotatedBitmap)
        }
        binding_ho_so.tvFullname.text = customer.full_name

        var badge = binding_ho_so.bottomNavigationView.getOrCreateBadge(R.id.thongbao)
        if (Info.so_thong_bao_chua_doc == 0) {
            badge.isVisible = false
        } else {
            badge.isVisible = true
            badge.number = Info.so_thong_bao_chua_doc
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(CAMERA),
                REQUEST_PERMISSION)
        }
    }

//    private fun openCamera() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
//            intent.resolveActivity(packageManager)?.also {
//                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
//            }
//        }
//    }

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
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_IMAGE_CAPTURE) {
//                val bitmap = data?.extras?.get("data") as Bitmap
//                binding_ho_so.imageView2.setImageBitmap(bitmap)
//            }
//            else if (requestCode == REQUEST_PICK_IMAGE) {
//                val uri = data?.getData()
//                binding_ho_so.imageView2.setImageURI(uri)
//            }
//        }
//    }
}