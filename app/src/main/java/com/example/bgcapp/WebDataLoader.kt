package com.example.bgcapp

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory


class WebDataLoader(val context: Context) {


    fun loadUserDataGames() {

        val dbHandler = MyDBHandler( context ,null,null,1)

        var games :MutableList<Game> = mutableListOf()



        val url = URL("https://boardgamegeek.com/xmlapi2/collection?username=${dbHandler.checkUser()}&stats=1&subtype=boardgame&excludesubtype=boardgameexpansion")
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
            return
        }
        catch (e: FileNotFoundException) {
            return
        }
        catch (e: IOException) {
            return
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
                var thumbnail = "https://i.imgur.com/4ma0EnA.png"
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





        return
    }


    fun loadUserDataExtensions(){

        val dbHandler = MyDBHandler( context ,null,null,1)

        var expansions :MutableList<Game> = mutableListOf()



        val url = URL("https://boardgamegeek.com/xmlapi2/collection?username=${dbHandler.checkUser()}&stats=1&subtype=boardgameexpansion")
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
            return
        }
        catch (e: FileNotFoundException) {
            return
        }
        catch (e: IOException) {
            return
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
            try {
                val item: Node = items.item(i)
                if (item.nodeType == Node.ELEMENT_NODE) {
                    val elem = item as Element
                    val children = elem.childNodes


                    var bggId: Long = 0
                    var title = "N/A"
                    var originalTitle = "N/A"
                    var releaseYear: Int = 0
                    var thumbnail = "https://i.imgur.com/4ma0EnA.png"
                    var ranking: Int = 0

                    bggId = elem.getAttribute("objectid").toLong()
                    val rankingTags = elem.getElementsByTagName("rank")

                    for (j in 0 until children.length) {
                        val node = children.item(j)
                        if (node is Element) {
                            when (node.nodeName) {

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


                    val game = Game(title, originalTitle, releaseYear, bggId, ranking, thumbnail)
                    expansions.add(game)

                }
            }
            catch(e: Exception){
                continue
            }

        }



        val expansionIds :MutableList<Long> = mutableListOf()
        for (expansion in expansions){
            if (expansionIds.contains(expansion.bggId)) {continue}
            expansionIds.add(expansion.bggId)
            if(dbHandler.findGame(expansion.bggId) == null){

                dbHandler.addExpansion(expansion)
            }
            else{
                dbHandler.deleteExpansion(expansion.bggId)
                dbHandler.addExpansion(expansion)
            }

        }


        dbHandler.updateSync()





        return
    }

}