package com.example.medicinemart.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.medicinemart.R
import com.example.medicinemart.adapter.MessageAdapter

class ChatApplication : AppCompatActivity() {

    private lateinit var chatRecycleView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_application)

        val name = intent.getStringExtra("name")
        val id = intent.getStringExtra("id")

        supportActionBar?.title = name

        chatRecycleView = findViewById(R.id.chatRecycleView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)
    }
}