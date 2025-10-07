package com.practicum.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.button.MaterialButton
import com.practicum.playlistmaker.media.ui.MediaActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.setting.ui.SettingsActivity

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

        mediaButton.setOnClickListener {
            val mediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settingButton.setOnClickListener {
            val settingIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingIntent)
        }
    }
}