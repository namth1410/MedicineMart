package com.example.medicinemart.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.medicinemart.R
import com.example.medicinemart.databinding.HotroBinding

private lateinit var binding_ho_tro: HotroBinding


class HoTroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_ho_tro = HotroBinding.inflate(layoutInflater)
        setContentView(binding_ho_tro.root)

        val spannableStringZalo = SpannableString(binding_ho_tro.linkZalo.text)
        spannableStringZalo.setSpan(UnderlineSpan(), 0, binding_ho_tro.linkZalo.text.length, 0)

        binding_ho_tro.linkZalo.text = spannableStringZalo

        val spannableStringFB = SpannableString(binding_ho_tro.linkFacebook.text)
        spannableStringFB.setSpan(UnderlineSpan(), 0, binding_ho_tro.linkFacebook.text.length, 0)

        binding_ho_tro.linkFacebook.text = spannableStringFB

        binding_ho_tro.linkZalo.setOnClickListener() {
            val animation = AnimationUtils.loadAnimation(this, R.anim.btn_anim)
            binding_ho_tro.linkZalo.startAnimation(animation)
            val url = "https://zalo.me/0365004062"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding_ho_tro.linkFacebook.setOnClickListener() {
            val animation = AnimationUtils.loadAnimation(this, R.anim.btn_anim)
            binding_ho_tro.linkFacebook.startAnimation(animation)
            val url = "https://www.facebook.com/than.xom.33"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding_ho_tro.btnCall.setOnClickListener() {
            val animation = AnimationUtils.loadAnimation(this, R.anim.btn_anim)
            binding_ho_tro.btnCall.startAnimation(animation)
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:0345518088")
            startActivity(intent)
        }

        binding_ho_tro.btnBack.setOnClickListener() {
            val anim = AnimationUtils.loadAnimation(this, R.anim.btn_back)
            binding_ho_tro.btnBack.startAnimation(anim)
            onBackPressed()
        }
    }
}