package com.example.medicinemart.models

import java.io.Serializable

data class Message (
    var message: String,
    var receiverId: String,
    var senderId: String,
    var time: Int,
    ) : Serializable