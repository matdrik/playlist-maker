package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  Нажатие на картинку вторым способом
        val image = findViewById<ImageView>(R.id.poster)
        image.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на картинку", Toast.LENGTH_SHORT).show()
            image.setBackgroundColor(getColor(R.color.black))
            image.scaleType = ImageView.ScaleType.CENTER_CROP
            image.setImageResource(R.drawable.poster)
        }

        //  Нажатие на кнопку вторым способом
        val buttonNo = findViewById<Button>(R.id.button_no)
        buttonNo.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на НЕТ", Toast.LENGTH_SHORT).show()
            image.setBackgroundColor(getColor(R.color.black))
            image.scaleType = ImageView.ScaleType.CENTER_CROP
            image.setImageResource(R.drawable.poster)
        }

        //  Нажатие на кнопку первым способом
        val buttonYes = findViewById<Button>(R.id.button_yes)
        val buttonYesClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            Toast.makeText(this@MainActivity, "Нажали на ДА", Toast.LENGTH_SHORT).show()
        } }
        buttonYes.setOnClickListener(buttonYesClickListener)
    }
}