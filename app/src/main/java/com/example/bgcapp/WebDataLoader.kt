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
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory


class WebDataLoader(val context: Context) {


    fun loadUserData(): String {
        val url = URL("https://boardgamegeek.com/xmlapi2/collection?username=Minmyska&stats=1")
        var responseText = ""



        try {

            val connection = url.openConnection()
            Log.e("APPLICATION","LOADING DATA")
            connection.connect()
            val lengthOfFile = connection.contentLength
            val isStream= url.openStream()

            //val catalogPath =  Paths.get("").toAbsolutePath().toString()
            val testDirectory = File(  "${context.filesDir}/XML")
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
        val path = context.filesDir
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

                var title: String? = null
                var originalTitle: String? = null
                var releaseYear: Int = 0


                for (j in 0 until children.length){
                    val node = children.item(j)
                    if (node is Element){
                        when (node.nodeName){

                            "yearpublished" -> {
                                releaseYear = node.textContent.toInt()
                            }

                        }

                    }
                }
                //Log.d("DEBUGYEAR",releaseYear.toString())
                Log.e("DEBUGYEAR",releaseYear.toString())

            }
        }


        return ""
    }

}