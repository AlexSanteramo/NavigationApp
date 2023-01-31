package com.example.navigationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById((R.id.maps)) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        var mMap = p0
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        val mainCampus = LatLng(41.4189, -72.8936)
        val CCE = LatLng(41.419641,-72.897404)
        val tator = LatLng(41.417941,-72.895722)
        val lender = LatLng(41.4195,-72.8951)
        val arnold = LatLng(41.418705,-72.894269)
        val recCenter = LatLng(41.420105,-72.893743)
        mMap.addMarker(MarkerOptions().position(CCE).title("CCE"))
        mMap.addMarker(MarkerOptions().position(tator).title("Tator Hall").snippet("Dining Hall"))
        mMap.addMarker(MarkerOptions().position(lender).title("Lender School of Business"))
        mMap.addMarker(MarkerOptions().position(arnold).title("Arnold Library"))
        mMap.addMarker(MarkerOptions().position(recCenter).title("Recreation Center"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainCampus, 16f))

    }
}