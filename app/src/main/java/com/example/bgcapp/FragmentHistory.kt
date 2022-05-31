package com.example.bgcapp

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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


class FragmentHistory(): Fragment() {

    var gameId:Int = 0
    constructor(gameId: Int) : this() {
        this.gameId = gameId

    }
    lateinit var gameName: TextView
    //lateinit var gameNameTextView: TextView

    lateinit var tableHistory: TableLayout

    override fun getContext(): Context? {
        return super.getContext()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view: View = inflater.inflate(R.layout.fragment_historyranking,container,false)
        //gameNameTextView = view.findViewById(R.id.gameNameTextView)
        tableHistory = view.findViewById(R.id.tableHistory)
        gameName = view.findViewById(R.id.gameName)
        //gameNameTextView.text = gameId.toString()

        //tableGames = view.findViewById(R.id.tableGames)
        val dbHandler = MyDBHandler( requireContext() ,null,null,1)


        gameName.text = dbHandler.findGame(gameId.toLong())?.originalTitle.toString()
        showHistoricalRanking(dbHandler.getHistoryofGame(gameId.toLong()))






        return view

    }





    fun showHistoricalRanking(rankingList: MutableList<String>){
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0




        val dbHandler = MyDBHandler( requireContext() ,null,null,1)



        val rows = rankingList.size




        val tv2 = TextView(requireContext())

        tv2.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )
        //tv2.width = 100
        tv2.gravity = Gravity.CENTER
        tv2.setPadding(35, 0, 35, 0)

        tv2.setText("")

        tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 44F)
        tv2.setTextColor(Color.parseColor("#ffffff"))

        //tv2.setBackgroundResource(R.drawable.border)
        tv2.setBackgroundColor(Color.parseColor("#7703fc"))

        val tv = TextView(requireContext())
        //tv.setBackgroundResource(R.drawable.border)
        tv.layoutParams = TableRow.LayoutParams(
            350,
            TableRow.LayoutParams.MATCH_PARENT
        )
        tv.gravity = Gravity.TOP

        //tv.gravity = Gravity.TOP
        tv.setPadding(10, 0, 0, 0)
        tv.textAlignment = View.TEXT_ALIGNMENT_CENTER

        tv.setText("Name")
        tv.setTextColor(Color.parseColor("#ffffff"))

        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 44F)

        tv.setBackgroundColor(Color.parseColor("#7703fc"))



        val tv3 = TextView(requireContext())

        tv3.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )
        //tv2.width = 100
        tv3.gravity = Gravity.CENTER
        tv3.setPadding(35, 0, 35, 0)

        tv3.setText("Ranking")

        tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, 44F)
        tv3.setTextColor(Color.parseColor("#ffffff"))
        //tv3.setBackgroundResource(R.drawable.border)
        tv3.setBackgroundColor(Color.parseColor("#7703fc"))

        val tv4 = TextView(requireContext())

        tv3.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        )
        //tv2.width = 100
        tv4.gravity = Gravity.CENTER
        tv4.setPadding(35, 0, 35, 0)

        tv4.setText("Thumbnail")

        tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, 44F)
        tv4.setTextColor(Color.parseColor("#ffffff"))
        //tv4.setBackgroundResource(R.drawable.border)
        tv4.setBackgroundColor(Color.parseColor("#7703fc"))


        val tr = TableRow(requireContext())
        val trParams = TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT)
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)
        //tr.setPadding(35,0,0,0)
        tr.addView(tv2)
        tr.addView(tv4)
        tr.addView(tv)
        tr.addView(tv3)
        //tr.id = row?.bggId.toInt()

        tableHistory.addView(tr,trParams)


        for (i in 0 until rows) {

            val row = rankingList[i]


            val tv2 = TextView(requireContext())

            tv2.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
            //tv2.width = 100
            tv2.gravity = Gravity.CENTER
            tv2.setPadding(35, 0, 35, 0)

            tv2.setText(row)

            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 84F)
            tv2.setTextColor(Color.parseColor("#ffffff"))
            tv2.setBackgroundResource(R.drawable.border)



            val tr = TableRow(requireContext())
            val trParams = TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)
            //tr.setPadding(35,0,0,0)
            tr.addView(tv2)
            //tr.addView(iv)
            //tr.addView(tv)
            //tr.addView(tv3)

            tableHistory.addView(tr,trParams)


        }


    }










}