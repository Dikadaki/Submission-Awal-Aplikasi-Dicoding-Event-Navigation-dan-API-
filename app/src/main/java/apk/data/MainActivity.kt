package com.DhikaWisata.wisatareview

import apk.data.ui.fragment.KegiatanSelesaiFragments
import apk.data.ui.fragment.KegiatanAktifFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Default fragment ketika aplikasi dibuka
        loadFragment(KegiatanAktifFragment())


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_kegiatan_aktif -> {
                    loadFragment(KegiatanAktifFragment())
                    true
                }
                R.id.nav_kegiatan_selesai -> {
                    loadFragment(KegiatanSelesaiFragments())
                    true
                }
                else -> false
            }
        }

    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
