package com.example.bgcapp

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class WebDataLoader {


    fun loadUserData(): String {
        val url = URL("https://boardgamegeek.com/xmlapi2/collection?username=rahdo&stats=1")
        val connection = url.openConnection()
        connection.connect()
        var responseText = ""
        BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->


            responseText = inp.readText()
            inp.close()



//            while (inp.readLine().also { line = it } != null) {
//                println(line)
//            }
        }

        return responseText
    }

}