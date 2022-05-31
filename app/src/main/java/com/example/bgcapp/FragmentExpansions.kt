package com.example.bgcapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.Layout
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
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import java.lang.reflect.Type
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.concurrent.thread


class FragmentExpansions: Fragment() {

//    lateinit var continueButton: Button
//    lateinit var usernameText: EditText
//    lateinit var user: TextView


    lateinit var tableExpansions: TableLayout

    override fun getContext(): Context? {
        return super.getContext()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view: View = inflater.inflate(R.layout.fragment_gamelist,container,false)
        tableExpansions = view.findViewById(R.id.tableGames)
        val dbHandler = MyDBHandler( requireContext() ,null,null,1)

        showGames(dbHandler.getExpansionList(0))
        dbHandler.close()





        return view

    }


    @SuppressLint("SetTextI18n")
    fun showGames(gameList: MutableList<Game>){
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0

        val dbHandler = MyDBHandler( requireContext() ,null,null,1)



        val rows = dbHandler.countExtensions()
        //val gameList =


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

//        val iv = ImageView(requireContext())
//        iv.layoutParams = TableRow.LayoutParams(
//            200,
//            250
//        )
//        iv.setPadding(25,25,25,25)
//        iv.setBackgroundResource(R.drawable.border)
//        //iv.adjustViewBounds = true
//        //iv.setImageResource()
//
//        val url =  URL(row.thumbnail)
//        val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//        iv.setImageBitmap(bitmap)

        val tr = TableRow(requireContext())
        val trParams = TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT)
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)
        //tr.setPadding(35,0,0,0)
        tr.addView(tv2)
        tr.addView(tv4)
        tr.addView(tv)
        //tr.addView(tv3)
        //tr.id = row?.bggId.toInt()

        tableExpansions.addView(tr,trParams)








        for (i in 0 until rows) {
            var row: Game? = null
            row = gameList[i]


            val tv2 = TextView(requireContext())

            tv2.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
            //tv2.width = 100
            tv2.gravity = Gravity.CENTER
            tv2.setPadding(35, 0, 35, 0)

            tv2.setText((i+1).toString())

            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 44F)
            tv2.setTextColor(Color.parseColor("#ffffff"))
            tv2.setBackgroundResource(R.drawable.border)


            val tv = TextView(requireContext())
            tv.setBackgroundResource(R.drawable.border)
            tv.layoutParams = TableRow.LayoutParams(
                450,
                TableRow.LayoutParams.MATCH_PARENT
            )
            tv.gravity = Gravity.TOP

            //tv.gravity = Gravity.TOP
            tv.setPadding(10, 0, 0, 0)
            tv.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

            tv.setText(row?.originalTitle + " (${row?.releaseYear})")
            tv.setTextColor(Color.parseColor("#ffffff"))

            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 44F)





            val tv3 = TextView(requireContext())

            tv3.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
            //tv2.width = 100
            tv3.gravity = Gravity.CENTER
            tv3.setPadding(35, 0, 35, 0)

            tv3.setText("${row?.rankingPosition}")

            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, 44F)
            tv3.setTextColor(Color.parseColor("#ffffff"))
            tv3.setBackgroundResource(R.drawable.border)





            val iv = ImageView(requireContext())
            iv.layoutParams = TableRow.LayoutParams(
                200,
                250
            )
            iv.setPadding(25,25,25,25)
            iv.setBackgroundResource(R.drawable.border)
            //iv.adjustViewBounds = true
            //iv.setImageResource()

            val url =  URL(row.thumbnail)
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            iv.setImageBitmap(bitmap)

            val tr = TableRow(requireContext())
            val trParams = TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT)
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)
            //tr.setPadding(35,0,0,0)
            tr.addView(tv2)
            tr.addView(iv)
            tr.addView(tv)
            //tr.addView(tv3)
            tr.id = row?.bggId.toInt()

            tableExpansions.addView(tr,trParams)


        }


    }


}