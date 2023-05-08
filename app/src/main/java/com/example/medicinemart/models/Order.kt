package com.example.medicinemart.models

data class Order(
    var id: Int,
    var sanpham: Sanpham,
    var quantity: Int,
    var address: Address,
    var time: Time
) {
    constructor() : this(0, Sanpham(), 0, Address(), Time())

}