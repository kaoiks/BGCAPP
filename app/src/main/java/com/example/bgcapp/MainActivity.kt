package com.example.bgcapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.lang.Exception
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    lateinit var syncDataButton: Button
    lateinit var cleanDataButton: Button

    lateinit var gameListButton: Button
    lateinit var extensionListButton: Button

    lateinit var dateLastSyncMain: TextView
    lateinit var user: TextView

    lateinit var progressBar2: ProgressBar

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameListButton = findViewById(R.id.gameListButton)

        extensionListButton = findViewById(R.id.extensionListButton)

        cleanDataButton = findViewById(R.id.cleanButton)

        user = findViewById(R.id.username)

        progressBar2 = findViewById(R.id.progressBar2)

        dateLastSyncMain = findViewById(R.id.dateLastSyncMain)
        //deleteDatabase("gamesDB.db")



        if (databaseList().size == 0){


            val dbHandler = MyDBHandler( this ,null,null,1)


            dbHandler.close()
            val fragment = com.example.bgcapp.FragmentUser()
            showFragmentUser(fragment)
        }

        val dbHandler = MyDBHandler( this ,null,null,1)

        if (dbHandler.checkUser() == null){
            val fragment = com.example.bgcapp.FragmentUser()
            showFragmentUser(fragment)
        }

        try
        {
            user = findViewById(R.id.username)
            user.text = dbHandler.checkUser()

            dateLastSyncMain.text = dbHandler.checkSync()

            gameListButton.setText("GAMES: ${dbHandler.countGames()}")

            extensionListButton.setText("EXPANSIONS: ${dbHandler.countExtensions()}")
        }
        catch (e: Exception){

        }

        dbHandler.close()
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        syncDataButton = findViewById(R.id.syncButton)

        syncDataButton.setOnClickListener {

            val fragment = com.example.bgcapp.FragmentSync()
            showFragment(fragment)

        }

        cleanDataButton.setOnClickListener{

            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Are you sure?").setCancelable(true)
                .setPositiveButton("Yes") { dialog, id ->
                    deleteDatabase("gamesDB.db")
                    exitProcess(0)
                }.setNegativeButton("No"){dialog, id ->
                    dialog.dismiss()


                }
            val alert = alertDialogBuilder.create()
            alert.show()

            //deleteDatabase("gamesDB.db")
            //exitProcess(0)
        }


        gameListButton.setOnClickListener{


            val task =  loadGamesListTask()
            task.execute()

        }
        extensionListButton.setOnClickListener{

            val task =  loadExpansionListTask()
            task.execute()

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

    fun showFragmentGames(fragment: FragmentGames){
        val fram = supportFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)
        fram.addToBackStack("listGames")
        fram.commit()
    }
    fun showFragmentExpansions(fragment: FragmentExpansions){
        val fram = supportFragmentManager.beginTransaction()
        fram.replace(R.id.fragment_main,fragment)
        fram.addToBackStack("listExpansions")
        fram.commit()
    }

    fun refreshData(){
        val dbHandler = MyDBHandler( this ,null,null,1)
        user.text = dbHandler.checkUser()

        dateLastSyncMain.text = dbHandler.checkSync()

        gameListButton.setText("GAMES: ${dbHandler.countGames()}")

        extensionListButton.setText("EXPANSIONS: ${dbHandler.countExtensions()}")

        dbHandler.close()
    }

    override fun onBackPressed() {

        Log.d("APP","BACK PRESSED + ${supportFragmentManager.backStackEntryCount}")
        if(supportFragmentManager.backStackEntryCount == 0){
            this.finish()
        }

        else{
            supportFragmentManager.popBackStack()
            if (supportFragmentManager.backStackEntryCount == 1) {
                refreshData()
            }
        }
    }


    private  inner class loadExpansionListTask: AsyncTask<String, Int, String>(){

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar2.visibility = View.VISIBLE


        }

        override fun doInBackground(vararg p0: String?): String {

            //syncButton.text = "SYNCHRONIZING"
            val fragment = com.example.bgcapp.FragmentExpansions()
            showFragmentExpansions(fragment)

            return "Finished"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)


        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressBar2.visibility = View.INVISIBLE

        }

    }

    private  inner class loadGamesListTask: AsyncTask<String, Int, String>(){

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar2.visibility = View.VISIBLE


        }

        override fun doInBackground(vararg p0: String?): String {

            //syncButton.text = "SYNCHRONIZING"
            val fragment = com.example.bgcapp.FragmentGames()
            showFragmentGames(fragment)

            return "Finished"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)


        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressBar2.visibility = View.INVISIBLE

        }

    }











}