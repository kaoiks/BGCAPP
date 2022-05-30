package com.example.bgcapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
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

            var response = loadUserDataGames()



            Toast.makeText(activity,"Synchronization completed",Toast.LENGTH_SHORT).show()
        }

        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




    }

    fun loadUserDataGames(): String {


        var games :MutableList<Game> = mutableListOf()



        val url = URL("https://boardgamegeek.com/xmlapi2/collection?username=Minmyska&stats=1&subtype=boardgame&excludesubtype=boardgameexpansion")
        var responseText = ""

        try {

            val connection = url.openConnection()
            Log.e("APPLICATION","LOADING DATA")
            connection.connect()
            val lengthOfFile = connection.contentLength
            val isStream= url.openStream()

            //val catalogPath =  Paths.get("").toAbsolutePath().toString()
            val testDirectory = File(  "${context?.filesDir}/XML")
            if (!testDirectory.exists()) testDirectory.mkdir()

            val fos = FileOutputStream( "$testDirectory/userData.xml")
            val data = ByteArray ( 1024)
            var count=0
            var total: Long = 0
            var progress = 0
            count = isStream.read(data)
            while (count != -1) {
                total += count.toLong()
                val progress_temp = total.toInt() *100/ lengthOfFile
                if (progress_temp %10 == 0 && progress != progress_temp) {
                    progress = progress_temp
                }
                fos.write(data,  0,count)
                count =  isStream.read(data)
            }
            isStream.close()
            fos.close()
            Log.e("APPLICATION","DATA LOADED TO FILE")
        } catch (e: MalformedURLException) {
            return "Zły URL"
        }
        catch (e: FileNotFoundException) {
            return "Brak pliku"
        }
        catch (e: IOException) {
            return "Wyjątek 10"
        }

        val catalogPath =  Paths.get("").toAbsolutePath().toString()
        val testDirectory = File(  "$catalogPath/XML")

        val fileName = "userData.xml"
        val path = context?.filesDir
        val inDir = File(path,"XML")

        //val fos = FileOutputStream( "$testDirectory/userData.xml")
        val file = File(inDir,fileName)



        val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)


        xmlDoc.documentElement.normalize()

        val items: NodeList = xmlDoc.getElementsByTagName("item")

        for (i in 0 until items.length){
            val item : Node = items.item(i)
            if (item.nodeType == Node.ELEMENT_NODE){
                val elem = item as Element
                val children = elem.childNodes


                var bggId: Long = 0
                var title = "N/A"
                var originalTitle = "N/A"
                var releaseYear: Int = 0
                var thumbnail = ""
                var ranking: Int = 0

                bggId = elem.getAttribute("objectid").toLong()
                val rankingTags =  elem.getElementsByTagName("rank")

                for (j in 0 until children.length){
                    val node = children.item(j)
                    if (node is Element){
                        when (node.nodeName){

                            "yearpublished" -> {
                                releaseYear = node.textContent.toInt()
                            }
                            "name" -> {
                                originalTitle = node.textContent.toString()
                            }
                            "thumbnail" -> {
                                thumbnail = node.textContent.toString()
                            }


                        }

                    }
                }

                for (j in 0 until rankingTags.length) {
                    val item2: Node = rankingTags.item(j)
                    if (item2.nodeType == Node.ELEMENT_NODE) {
                        val elem2 = item2 as Element

                        if (elem2.getAttribute("friendlyname") == "Board Game Rank") {

                            if (elem2.getAttribute("value").toString() == "Not Ranked") {
                                ranking = 0
                            } else {
                                ranking = elem2.getAttribute("value").toInt()
                            }
                        }

                    }
                }
                val game = Game(title,originalTitle,releaseYear,bggId,ranking,thumbnail)
                games.add(game)

            }

        }

        val dbHandler = MyDBHandler( requireContext() ,null,null,1)

        val gameIds :MutableList<Long> = mutableListOf()
        for (game in games){
            if (gameIds.contains(game.bggId)) {continue}
            gameIds.add(game.bggId)
            if(dbHandler.findGame(game.bggId) == null){

                dbHandler.addGame(game)
            }
            else{
                dbHandler.deleteGame(game.bggId)
                dbHandler.addGame(game)
            }

        }

        dbHandler.updateSync()

        lastSync.text = dbHandler.checkSync()

        //Thread.sleep(10000)
        //dbHandler.close()

//        for (game in games){
//            if(dbHandler.findGame(game.bggId) != null){
//                dbHandler.deleteGame(game.bggId)
//            }
//
//        }


        return ""
    }









}