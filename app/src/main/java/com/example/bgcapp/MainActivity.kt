package com.example.bgcapp

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    lateinit var syncDataButton: Button
    lateinit var cleanDataButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        syncDataButton = findViewById(R.id.syncButton)

        syncDataButton.setOnClickListener {

            val fragment = com.example.bgcapp.FragmentSync()
            showFragment(fragment)

        }
    }
    fun showFragment(fragment: FragmentSync){
        val fram = supportFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)
        fram.addToBackStack("sync")
        fram.commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0){
            this.finish()
        }
        else{
            supportFragmentManager.popBackStack()
        }
    }

    fun loadProfile(){


    }













}