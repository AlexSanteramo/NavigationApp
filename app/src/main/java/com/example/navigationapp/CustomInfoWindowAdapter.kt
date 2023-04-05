package com.example.navigationapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    override fun getInfoWindow(p0: Marker): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)

        val imageView = view.findViewById<ImageView>(R.id.info_window_image)
        val titleTextView = view.findViewById<TextView>(R.id.info_window_title)
        val snippetTextView = view.findViewById<TextView>(R.id.info_window_description)

        titleTextView.text = p0.title
        snippetTextView.text = p0.snippet

        //Residence Halls
        if (p0.title == "The Commons"){
            imageView.setImageResource(R.drawable.common)
        } else if (p0.title == "The Hill") {
            imageView.setImageResource(R.drawable.hill)
        } else if (p0.title == "Irmagarde Tator Residence Hall") {
            imageView.setImageResource(R.drawable.irma)
        } else if (p0.title == "Dana English Residence Hall"){
            imageView.setImageResource(R.drawable.dana)
        } else if (p0.title == "The Village") {
            imageView.setImageResource(R.drawable.village)
        } else if (p0.title == "Perloth Residence Hall") {
            imageView.setImageResource(R.drawable.perloth)
        } else if (p0.title == "Larson Residence Hall"){
            imageView.setImageResource(R.drawable.larson)
        } else if (p0.title == "Troup Residence Hall") {
            imageView.setImageResource(R.drawable.troup)
        } else if (p0.title == "The Complex") {
            imageView.setImageResource(R.drawable.complex)
        } else if (p0.title == "The Ledges"){
            imageView.setImageResource(R.drawable.ledges)
        } else if (p0.title == "Mountainview Residence Hall") {
            imageView.setImageResource(R.drawable.mountainveiw)
        }

        //Academic Buildings
        else if (p0.title == "Center for Communications and Engineering"){
            imageView.setImageResource(R.drawable.cce)
        } else if (p0.title == "Echlin Center") {
            imageView.setImageResource(R.drawable.echlin)
        } else if (p0.title == "Clarice L. Buckman Center/Theater" || p0.title == "Tator Hall") {
            imageView.setImageResource(R.drawable.buckman)
        } else if (p0.title == "Arnold Bernhard Library") {
            imageView.setImageResource(R.drawable.abl)
        } else if (p0.title == "Lender School of Business Center") {
            imageView.setImageResource(R.drawable.lender)
        } else if (p0.title == "Ed McMahon Communications Center") {
            imageView.setImageResource(R.drawable.ed)
        } else if (p0.title == "College of Arts and Sciences") {
            imageView.setImageResource(R.drawable.cas)
        }

        //Dining Halls
        else if (p0.title == "Mount Carmel Dining Hall") {
            imageView.setImageResource(R.drawable.mcdining)
        } else if (p0.title == "Bobcat Den") {
            imageView.setImageResource(R.drawable.bobcatden)
        }

        //Recreation Buildings
        else if (p0.title == "Carl Hansen Student Center") {
            imageView.setImageResource(R.drawable.chsc)
        } else if (p0.title == "Recreation and Wellness Center") {
            imageView.setImageResource(R.drawable.recandwell)
        } else if (p0.title == "Health and Wellness Center") {
            imageView.setImageResource(R.drawable.healthandwell)
        } else if (p0.title == "Catholic Chapel/Center for Religion") {
            imageView.setImageResource(R.drawable.catholic)
        }

        //Other
        else if (p0.title == "Harwood Gate") {
            imageView.setImageResource(R.drawable.harwood)
        } else if (p0.title == "Quinnipiac's Main Entrance") {
            imageView.setImageResource(R.drawable.main)
        } else if (p0.title == "Service Entrance") {
            imageView.setImageResource(R.drawable.newroad)
        } else if (p0.title == "New Road Entrance") {
            imageView.setImageResource(R.drawable.newroad)
        } else if (p0.title == "Faculty Office Building") {
            imageView.setImageResource(R.drawable.abl)
        } else if (p0.title == "Student Affairs Center") {
            imageView.setImageResource(R.drawable.sac)
        } else if (p0.title == "Pat Abbate '58 Alumni House and Gardens") {
            imageView.setImageResource(R.drawable.pat)
        } else if (p0.title == "Development Building") {
            imageView.setImageResource(R.drawable.devandpub)
        } else if (p0.title == "Mail Services Center") {
            imageView.setImageResource(R.drawable.mail)
        } else if (p0.title == "Peter C. Herald House for Jewish Life") {
            imageView.setImageResource(R.drawable.peter)
        } else if (p0.title == "Albert Schweitzer Institute") {
            imageView.setImageResource(R.drawable.albert)
        } else if (p0.title == "Office of Human Resources") {
            imageView.setImageResource(R.drawable.hr)
        }

        return view
    }
}