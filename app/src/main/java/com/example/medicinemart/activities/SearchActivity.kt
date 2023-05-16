package com.example.medicinemart.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.medicinemart.R
import com.example.medicinemart.adapter.OnItemClickListener
import com.example.medicinemart.adapter.RecycleViewSearchAdapter
import com.example.medicinemart.common.Info
import com.example.medicinemart.databinding.SearchBinding
import com.example.medicinemart.models.Sanpham
import java.io.Serializable

private lateinit var binding_search: SearchBinding

val priceComparator = Comparator<Sanpham> { item1, item2 ->
    item1.price.compareTo(item2.price)
}

var productSearchList = ArrayList<Sanpham>()
var productSearchListCopy = ArrayList<Sanpham>(productSearchList)


class SearchActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_search = SearchBinding.inflate(layoutInflater)
        setContentView(binding_search.root)
        val text_search = intent.getStringExtra("text_search")
//        binding_search.searchView.setQuery(text_search, false)
        binding_search.editTextSearch.setText(text_search)
        productSearchListCopy.addAll(productSearchList)

        if (Info.products_in_cart.isEmpty()) {
            binding_search.quantityInCart.visibility = View.GONE
        } else {
            binding_search.quantityInCart.visibility = View.VISIBLE
            binding_search.quantityInCart.text = Info.products_in_cart.size.toString()
        }

        binding_search.root.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_search.root.windowToken, 0)
            }
        }

        //xử lý nút quay lại
        binding_search.btnBack.setOnClickListener {
            onBackPressed()
        }

        // Xử lý spinner
        val options = resources.getStringArray(R.array.options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding_search.spinner.adapter = adapter

        binding_search.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_search.root.windowToken, 0)
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Xử lý khi người dùng chọn tùy chọn
                if (selectedItem == "Thấp đến cao") {
                    productSearchList.sortWith(priceComparator)
                    binding_search.recyclerView.adapter?.notifyDataSetChanged()
                } else if (selectedItem == "Cao đến thấp"){
                    productSearchList.sortWith(priceComparator)
                    productSearchList.reverse()
                    binding_search.recyclerView.adapter?.notifyDataSetChanged()
                } else {
                    productSearchList.clear()
                    productSearchList.addAll(productSearchListCopy)
                    binding_search.recyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Xử lý khi không có tùy chọn nào được chọn
            }

        }



        // Xử lý adapter cho RecycleView
        val adapterRecyclerSearch = RecycleViewSearchAdapter(productSearchList, this, object :
            OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@SearchActivity, ChiTietSanPhamActivity::class.java)
                intent.putExtra("item", productSearchList.get(position) as Serializable)
                intent.putExtra("goto", "search")

                startActivity(intent)
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
            }
        })

        val layoutManager = GridLayoutManager(this, 2)

        binding_search.recyclerView.setLayoutManager(layoutManager)
        binding_search.recyclerView.adapter = adapterRecyclerSearch
        // --

        for (product in Info.all_product) {
            if (product.name.contains(text_search.toString().removeAccent(), true)) {
                productSearchList.add(product)
                println(product.name)
                binding_search.recyclerView.adapter?.notifyDataSetChanged()
            }
        }

        // Xử lý sự kiện khi bấm vào giỏ hàng
        binding_search.btnCart.setOnClickListener() {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.no_animation, R.anim.no_animation)
        }

        binding_search.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do something before the text changed

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do something when the text changed
            }

            override fun afterTextChanged(s: Editable?) {
                // Do something after the text changed
                productSearchList.clear()
                for (product in Info.all_product) {
                    val originalString = product.name
                    val nonAccentString = originalString.removeAccent()
                    if (nonAccentString.contains(s.toString().removeAccent(), true)) {
                        productSearchList.add(product)
                    }
                }
                binding_search.recyclerView.adapter?.notifyDataSetChanged()
            }
        })

        binding_search.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Xử lý khi người dùng nhấn nút tìm kiếm trên bàn phím
                productSearchList.clear()
                for (product in Info.all_product) {
                    val originalString = product.name
                    val nonAccentString = originalString.removeAccent()
                    if (nonAccentString.contains(binding_search.editTextSearch.text.toString().removeAccent(), true)) {
                        productSearchList.add(product)
                    }
                }
                binding_search.recyclerView.adapter?.notifyDataSetChanged()
                binding_search.editTextSearch.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding_search.root.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }


                /*
                // Xử lý người dùng tìm kiếm
                binding_search.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        // Xử lý khi người dùng nhấn nút tìm kiếm

                        productSearchList.clear()
                        for (product in Info.all_product) {
                            val originalString = product.name
                            val nonAccentString = originalString.removeAccent()
                            if (nonAccentString.contains(query.toString(), true)) {
                                productSearchList.add(product)
                                println(product.name)
                            }
                        }
                        binding_search.recyclerView.adapter?.notifyDataSetChanged()
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        // Xử lý khi người dùng thay đổi nội dung tìm kiếm
                        println(newText)
                        productSearchList.clear()
                        for (product in Info.all_product) {
                            val originalString = product.name
                            val nonAccentString = originalString.removeAccent()
                            if (nonAccentString.contains(newText.toString(), true)) {
                                productSearchList.add(product)
                                println(product.name)
                            }
                        }
                        binding_search.recyclerView.adapter?.notifyDataSetChanged()
                        return false
                    }
                }) */
    }

    override fun onResume() {
        super.onResume()
        if (Info.products_in_cart.isEmpty()) {
            binding_search.quantityInCart.visibility = View.GONE
        } else {
            binding_search.quantityInCart.visibility = View.VISIBLE
            binding_search.quantityInCart.text = Info.products_in_cart.size.toString()
        }
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    /*
    private fun isTouchInsideSearchView(ev: MotionEvent): Boolean {
        if (binding_search.searchView != null) {
            val location = IntArray(2)
            binding_search.searchView?.getLocationOnScreen(location)
            val x = ev.rawX + binding_search.searchView!!.left - location[0]
            val y = ev.rawY + binding_search.searchView!!.top - location[1]
            if (x > 0 && y > 0 && x < binding_search.searchView!!.width && y < binding_search.searchView!!.height) {
                return true
            }
        }
        return false
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN && !isTouchInsideSearchView(ev)) {
            binding_search.searchView.clearFocus()

//            imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }  */

}