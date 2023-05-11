package com.example.medicinemart.models

import com.example.medicinemart.common.Info
import java.time.LocalDateTime

data class Time(
    var orderdate: LocalDateTime,
    var shipdate: LocalDateTime,
    var receiveddate: LocalDateTime,
    var canceldate: LocalDateTime
) {
    constructor() : this(
        Info.time_defaul,
        Info.time_defaul,
        Info.time_defaul,
        Info.time_defaul
    )
}