package com.example.myapplication

import android.view.View
import android.widget.TextView

// Расширение для любой View: ставит текст, если он есть, иначе скрывает View
fun TextView.setTextOrInvisible(value: Any?) {
    if (value != null) {
        this.text = value.toString()
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}
