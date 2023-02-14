package com.example.navigationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    var mapType = ""
    //var mMap = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById((R.id.maps)) as SupportMapFragment
        mapFragment.getMapAsync(this)
//        val spinner = findViewById<Spinner>(R.id.spinner)
//        val typeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.MapTypes))
//        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = typeAdapter
//        fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            when (view?.id) {
//                1 -> mapType = "Regular"
//                2 -> mapType = "Satellite"
//                3 -> mapType = "Hybrid"
//            }
//        }
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                mapType = "Satellite"
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                1; mMap
//                1; mapType = "Regular"
//                2; mapType = "Satellite"
//                3; mapType = "Hybrid"
//            }
//        }
    }

//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//
//        when (view?.id) {
//            1 -> showToast(message = "Spinner 2 Position:${position} and language: ${languages[position]}")
//            else -> {
//                showToast(message = "Spinner 1 Position:${position} and language: ${languages[position]}")
//            }
//        }
//    }

    override fun onMapReady(p0: GoogleMap) {
        var mMap = p0
        val spinner = findViewById<Spinner>(R.id.spinner)
        val typeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.MapTypes))
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = typeAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected_val: Long = spinner.selectedItemId
                println(selected_val)
                if(selected_val == 0L) {
                    mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                } else if(selected_val == 1L) {
                    mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                } else if(selected_val == 2L) {
                    mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                }
//                0; mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//                1; mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
//                2; mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
//                1; mapType = "Regular"
//                2; mapType = "Satellite"
//                3; mapType = "Hybrid"
            }
        }
//        if (mapType == "Regular") {
//            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//        } else if (mapType == "Satellite") {
//            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
//        } else if (mapType == "Hybrid") {
//            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
//        }
        //mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        val mainCampus = LatLng(41.4189, -72.8936)
        val CCE = LatLng(41.419641,-72.897404)
        val tator = LatLng(41.417941,-72.895722)
        val lender = LatLng(41.4195,-72.8951)
        val arnold = LatLng(41.418705,-72.894269)
        val recCenter = LatLng(41.420105,-72.893743)
        mMap.addMarker(MarkerOptions().position(CCE).title("CCE"))
        mMap.addMarker(MarkerOptions().position(tator).title("Tator Hall").snippet("Dining Hall").icon(
            BitmapDescriptorFactory.defaultMarker(229.0F)
        ))
        mMap.addMarker(MarkerOptions().position(lender).title("Lender School of Business"))
        mMap.addMarker(MarkerOptions().position(arnold).title("Arnold Library"))
        mMap.addMarker(MarkerOptions().position(recCenter).title("Recreation Center"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainCampus, 16f))

    }
}

