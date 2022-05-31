package com.example.bgcapp

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.rangeTo
import androidx.fragment.app.Fragment
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.concurrent.thread
import kotlin.system.exitProcess


class FragmentSync: Fragment() {

    lateinit var syncButton: Button
    lateinit var lastSync: TextView
    private var task = loadGamesExtensionsTask()

    lateinit var progressBarSync: ProgressBar
    override fun getContext(): Context? {
        return super.getContext()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view: View = inflater.inflate(R.layout.fragment_synchronization,container,false)

        lastSync = view.findViewById(R.id.dateLastSync)
        var dbHandler = MyDBHandler( requireContext() ,null,null,1)


        lastSync.text = dbHandler.checkSync()
        dbHandler.close()

        progressBarSync = view.findViewById(R.id.progressBarSync)


        syncButton = view.findViewById(R.id.syncStartButton)

        syncButton.setOnClickListener{

            dbHandler = MyDBHandler( requireContext() ,null,null,1)
            val syncDate = dbHandler.checkSync()
            dbHandler.close()
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val syncDateParsed = sdf.parse(syncDate.toString())
            val currentDateParsed = sdf.parse(sdf.format(Date()))


//            val dif = currentDateParsed.time - syncDateParsed.time
//            //val hours = dif / (1000*60*60)
//            val counted = currentDateParsed.time - 86400000
//            Log.e("APPLICATION","${counted} NOW")
//            Log.e("APPLICATION","${currentDateParsed.time } NOW")

            //Log.e("APPLICATION","${hours} minutes")
            if (currentDateParsed.time - syncDateParsed.time < 86400000){
                val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                alertDialogBuilder.setMessage("Are you sure?\nRanking is updated only once a day.").setCancelable(true)
                    .setPositiveButton("Yes") { dialog, id ->
                        //deleteDatabase("gamesDB.db")
                        //exitProcess(0)
                        task.execute()
                    }.setNegativeButton("No"){dialog, id ->
                        dialog.dismiss()


                    }
                val alert = alertDialogBuilder.create()
                alert.show()


            }
            else{
                task.execute()
            }

            //var response2 = loadUserDataExtensions()



        }

        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




    }


    private  inner class loadGamesExtensionsTask: AsyncTask<String, Int, String>(){

        override fun onPreExecute() {
            super.onPreExecute()
            progressBarSync.visibility = View.VISIBLE

        }

        override fun doInBackground(vararg p0: String?): String {

            //syncButton.text = "SYNCHRONIZING"
            publishProgress(0)

            WebDataLoader(requireContext()).loadUserDataGames()
            publishProgress(5)
            WebDataLoader(requireContext()).loadUserDataExtensions()

            publishProgress(10)

            return "Finished"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)

            if (values[0] == 0) {
                syncButton.text = "SYNCHRONIZING"
                Toast.makeText(activity,"Synchronizing games",Toast.LENGTH_SHORT).show()
            }
            else if (values[0] == 5) {

                Toast.makeText(activity,"Synchronizing expansions",Toast.LENGTH_SHORT).show()
            }
            else if (values[0] == 10) {

                val dbHandler = MyDBHandler( requireContext() ,null,null,1)
                lastSync.text = dbHandler.checkSync()
                dbHandler.close()
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            syncButton.text = "SYNCHRONIZE"

            progressBarSync.visibility = View.INVISIBLE
            Toast.makeText(activity,"Synchronization completed",Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()

        }

    }



}