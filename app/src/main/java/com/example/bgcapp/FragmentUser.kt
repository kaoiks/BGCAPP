package com.example.bgcapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.concurrent.thread


class FragmentUser: Fragment() {

    lateinit var continueButton: Button
    lateinit var usernameText: EditText
    lateinit var user: TextView

    override fun getContext(): Context? {
        return super.getContext()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view: View = inflater.inflate(R.layout.fragment_user,container,false)

            continueButton = view.findViewById(R.id.submitUserButton)
            usernameText = view.findViewById(R.id.editTextTextPersonName)
            user = view.findViewById(R.id.username)

            continueButton.setOnClickListener{

                if (usernameText.text.toString() == ""){
                }
                else{
                    if(checkUserExists(usernameText.text.toString())){
                        val dbHandler = MyDBHandler( requireContext() ,null,null,1)
                        dbHandler.setUser(usernameText.text.toString())
                        user.text = usernameText.text.toString()

                        activity?.onBackPressed()

                    }
                }

            }



        return view

    }

    fun checkUserExists(username: String):Boolean{
        val url = URL("https://boardgamegeek.com/xmlapi2/user?name=$username")
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

            val fos = FileOutputStream( "$testDirectory/usernameInfo.xml")
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
            return false
        }
        catch (e: FileNotFoundException) {
            return false
        }
        catch (e: IOException) {
            return false
        }


        val fileName = "usernameInfo.xml"
        val path = context?.filesDir
        val inDir = File(path,"XML")

        //val fos = FileOutputStream( "$testDirectory/userData.xml")
        val file = File(inDir,fileName)



        val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)


        xmlDoc.documentElement.normalize()

        val items: NodeList = xmlDoc.getElementsByTagName("user")

        for (i in 0 until items.length){
            val item : Node = items.item(i)
            if (item.nodeType == Node.ELEMENT_NODE){
                val elem = item as Element
                return elem.getAttribute("id") != ""

            }
        }

        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




    }











}