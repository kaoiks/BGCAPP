package com.example.bgcapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class MainActivity : AppCompatActivity() {

    lateinit var syncDataButton: Button
    lateinit var cleanDataButton: Button

    lateinit var gameListButton: Button
    lateinit var extensionListButton: Button

    lateinit var user: TextView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameListButton = findViewById(R.id.gameListButton)

        extensionListButton = findViewById(R.id.extensionListButton)



        //deleteDatabase("gamesDB.db")

        if (databaseList().size == 0){
            val dbHandler = MyDBHandler( this ,null,null,1)

            dbHandler.close()
            val fragment = com.example.bgcapp.FragmentUser()
            showFragmentUser(fragment)
        }

        val dbHandler = MyDBHandler( this ,null,null,1)


        user = findViewById(R.id.username)
        user.text = dbHandler.checkUser()

        gameListButton.setText("GAMES: ${dbHandler.countGames()}")

        extensionListButton.setText("EXTENSIONS: ${dbHandler.countExtensions()}")

        dbHandler.close()
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

    fun showFragmentUser(fragment: FragmentUser){
        val fram = supportFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)
        fram.addToBackStack("setUser")
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
        //val fileContent = File("profile")

    }













}