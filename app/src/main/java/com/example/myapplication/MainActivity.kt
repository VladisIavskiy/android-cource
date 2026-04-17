package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    // todo в debug посмотреть ключ. открыть в браузере,
    //  подгружается товар. как называеются недостающие поля.
    //  некоторые будут возвращать null. которые null сделать в ProductDetails типы nullable.
    //  сделать VisibilityGone

    // android kotlin прогнать. поготовиться

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Показываем фрагмент с деталями товара
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ProductFragmentDetails())
                .commit()
        }
    }
}