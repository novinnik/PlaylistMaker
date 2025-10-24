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
import com.practicum.playlistmaker.search.ui.activity.SearchActivity
import com.practicum.playlistmaker.setting.ui.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Для поддержки 15 Android
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        //отступы для системных виджетов
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }



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