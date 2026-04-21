package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Показываем фрагмент с деталями товара
        if (savedInstanceState == null) { // смотрим первый ли раз запускаем Activity
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ProductFragmentDetails())
                .commit()
        }
    }
}