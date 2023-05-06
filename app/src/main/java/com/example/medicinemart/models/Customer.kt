package com.example.medicinemart.models

import java.io.Serializable

data class Customer(
    var id: Int,
    var username: String,
    var full_name: String,
    var phone: String,
    var email: String
) : Serializable {
    constructor() : this(0,"", "","","")
}