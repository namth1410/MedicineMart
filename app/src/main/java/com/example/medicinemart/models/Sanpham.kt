package com.example.medicinemart.models

import java.io.Serializable

data class Sanpham(
    var id: Int,
    var name: String,
    var type: String,
    var price: Int,
    var describe: String,
    var image: String
) : Serializable {
    constructor() : this(0,"", "", 0,"","")

}