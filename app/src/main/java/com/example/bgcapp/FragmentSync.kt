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
import android.widget.TextView
import android.widget.Toast
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
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.concurrent.thread


class FragmentSync: Fragment() {

    lateinit var syncButton: Button
    lateinit var lastSync: TextView
    private var task = loadGamesExtensionsTask()

    override fun getContext(): Context? {
        return super.getContext()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view: View = inflater.inflate(R.layout.fragment_synchronization,container,false)

        lastSync = view.findViewById(R.id.dateLastSync)
        val dbHandler = MyDBHandler( requireContext() ,null,null,1)


        lastSync.text = dbHandler.checkSync()
        dbHandler.close()

        syncButton = view.findViewById(R.id.syncStartButton)

        syncButton.setOnClickListener{

            task.execute()


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
            Toast.makeText(activity,"Synchronization completed",Toast.LENGTH_SHORT).show()
        }

    }



}