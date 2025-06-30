package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searсhButton = findViewById<MaterialButton>(R.id.button_search)
        val mediaButton = findViewById<MaterialButton>(R.id.button_media)
        val settingButton = findViewById<MaterialButton>(R.id.button_setting)

        val buttonSeaкchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }

        searсhButton.setOnClickListener(buttonSeaкchClickListener)

        mediaButton.setOnClickListener{
            val mediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settingButton.setOnClickListener{
            val settingIntent= Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingIntent)
        }
    }
}