package com.example.medicinemart.models

import com.example.medicinemart.common.Info
import java.io.Serializable
import java.time.LocalDateTime

data class Notification (
    var id: Int,
    var id_customer: Int,
    var title: String,
    var content: String,
    var time: LocalDateTime,
    var id_product: Int,
    var id_order: Int,
    var image: String
) : Serializable {
    constructor() : this(0,0, "", "", Info.time_defaul, 0, 0, "")
}