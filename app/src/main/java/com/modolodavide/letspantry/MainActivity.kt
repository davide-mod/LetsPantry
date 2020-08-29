package com.modolodavide.letspantry

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var drawer: DrawerLayout
    private lateinit var handler: Handler
    private val navigationListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            drawer.closeDrawer(GravityCompat.START)
            handler.postDelayed({
                when (item.itemId) {
                    R.id.itemDispensa -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.mainFragment)
                    }
                    R.id.itemListaSpesa -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.listaSpesaFragment)
                    }
                    R.id.itemRicette -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.ricetteFragment)
                    }
                    R.id.itemSupermercati -> {
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data =
                            Uri.parse("https://www.google.com/search?q=supermercati%20nelle%20vicinanze")
                        startActivity(openURL)
                    }
                    R.id.itemCredits -> {
                        findNavController(R.id.nav_host_fragment).navigate(R.id.creditsFragment)
                    }
                }
            }, 0)
            true
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_lato)
        handler = Handler()
        drawer = findViewById(R.id.drawerMain)
        val menulaterale = findViewById<NavigationView>(R.id.menulaterale)
        menulaterale.setNavigationItemSelectedListener(navigationListener)
    }
    fun actionBarColor(colore: Int){
        val actionBarColore = supportActionBar
        actionBarColore?.setBackgroundDrawable(ColorDrawable(colore))
    }

    fun openDrawer() {
        drawer.openDrawer(GravityCompat.START)
    }
}