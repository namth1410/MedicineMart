package com.example.medicinemart.models

import com.example.medicinemart.common.Info.location_default
import java.io.Serializable
import java.math.BigDecimal

data class Address(
    var id: Int,
    var phone: String,
    var username: String,
    var full_name: String,
    var td_x: BigDecimal,
    var td_y: BigDecimal,
    var location: String
) : Serializable {
    constructor() : this(0, "", "", "", BigDecimal(location_default.latitude), BigDecimal(location_default.longitude), "")
}

