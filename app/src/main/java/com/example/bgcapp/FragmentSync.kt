package com.example.bgcapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment



class FragmentSync: Fragment() {

    lateinit var syncButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view: View = inflater!!.inflate(R.layout.fragment_synchronization,container,false)

        syncButton = view.findViewById(R.id.syncStartButton)

        syncButton.setOnClickListener{

            var response =  WebDataLoader().loadUserData()



            Toast.makeText(activity,"ESSA",Toast.LENGTH_SHORT).show()
        }

        return view




    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




    }










}