package com.example.myapplication

import android.view.View
import android.widget.ImageView
import android.widget.TextView

// Расширение для любой View: ставит текст, если он есть, иначе скрывает View
fun TextView.setTextOrGone(value: Any?) {
    // Проверяем не только на null, но и на пустую строку
    if (value != null && value.toString().isNotEmpty()) {
        this.text = value.toString()
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

