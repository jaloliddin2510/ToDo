package com.example.todo

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Info : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        var set = findViewById<ImageView>(R.id.info_back)
        set.setOnClickListener {
            finish()
        }
    }
}