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
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Residence Halls
        val commons = LatLng(41.41735437231641, -72.89309639489136)
        mMap.addMarker(MarkerOptions().position(commons).title("The Commons"))
        val hill = LatLng(41.41805075394168, -72.89230888456365 )
        mMap.addMarker(MarkerOptions().position(hill).title("The Hill"))
        val irma = LatLng(41.418844634444994, -72.89259038684314)
        mMap.addMarker(MarkerOptions().position(irma).title("Irmagarde Tator Residence Hall"))
        val dana = LatLng(41.41910528134656, -72.89200880413638)
        mMap.addMarker(MarkerOptions().position(dana).title("Dana English Residence Hall"))
        val village = LatLng(41.41820589567165, -72.89117583700015)
        mMap.addMarker(MarkerOptions().position(village).title("The Village"))
        val perloth = LatLng(41.41920237904667, -72.89087212637034)
        mMap.addMarker(MarkerOptions().position(perloth).title("Perloth Residence Hall"))
        val larson = LatLng(41.41947180215361, -72.89134331922317)
        mMap.addMarker(MarkerOptions().position(larson).title("Larson Residence Hall"))
        val troup = LatLng(41.41973239065013, -72.89040093351448)
        mMap.addMarker(MarkerOptions().position(troup).title("Troup Residence Hall"))
        val complex = LatLng(41.41906935535658, -72.89013527108283)
        mMap.addMarker(MarkerOptions().position(complex).title("The Complex"))
        val ledges = LatLng(41.41961821563668, -72.88922685442634)
        mMap.addMarker(MarkerOptions().position(ledges).title("The Ledges"))
        val mtnView = LatLng(41.41998775505592, -72.8888125986298 )
        mMap.addMarker(MarkerOptions().position(mtnView).title("Mountainview Residence Hall"))

        // Academic Buildings
        val CCE = LatLng(41.42018449724887, -72.89757839057904)
        mMap.addMarker(MarkerOptions().position(CCE).title("Center for Communications and Engineering"))
        val echlin = LatLng(41.41833564400074, -72.89702159337257)
        mMap.addMarker(MarkerOptions().position(echlin).title("Echlin Center"))
        val buckman = LatLng(41.417924344634756, -72.89652244284237)
        mMap.addMarker(MarkerOptions().position(buckman).title("Clarice L. Buckman Center/Theater"))
        val tator = LatLng(41.418001961998385, -72.89575136217536)
        mMap.addMarker(MarkerOptions().position(tator).title("Tator Hall"))
        val arnold = LatLng(41.41887706277957, -72.89410166186688)
        mMap.addMarker(MarkerOptions().position(arnold).title("Arnold Bernhard Library"))
        val lender = LatLng(41.41976342725668, -72.89491840378152)
        mMap.addMarker(MarkerOptions().position(lender).title("Lender School of Business Center"))
        val comCntr = LatLng(41.419420113830185, -72.89563506973184)
        mMap.addMarker(MarkerOptions().position(comCntr).title("Ed McMahon Communications Center"))
        val CAS = LatLng(41.41568615134177, -72.89520349124456)
        mMap.addMarker(MarkerOptions().position(CAS).title("College of Arts and Sciences"))

        // Dining Halls
        val dinHall = LatLng(41.41807526994349, -72.89446708811674)
        mMap.addMarker(MarkerOptions().position(dinHall).title("Mount Carmel Dining Hall"))
        val rat = LatLng(41.41881571604852, -72.89173436537993 )
        mMap.addMarker(MarkerOptions().position(rat).title("Bobcat Den"))

        // Recreation Buildings
        val studCntr = LatLng(41.41815750775577, -72.89496353668893)
        mMap.addMarker(MarkerOptions().position(studCntr).title("Carl Hansen Student Center"))
        val recCenter = LatLng(41.42003562734498, -72.89321581989024)
        mMap.addMarker(MarkerOptions().position(recCenter).title("Recreation and Wellness Center"))
        val health = LatLng(41.42019168861404, -72.89378204596598)
        mMap.addMarker(MarkerOptions().position(health).title("Health and Wellness Center"))
        val religion = LatLng(41.41556706288183, -72.89468611288991 )
        mMap.addMarker(MarkerOptions().position(religion).title("Catholic Chapel/Center for Religion"))

        // Other
        val harGate = LatLng(41.420708302992026, -72.89868449379156)
        mMap.addMarker(MarkerOptions().position(harGate).title("Harwood Gate"))
        val mainEnt = LatLng(41.42137047881362, -72.89541744137112)
        mMap.addMarker(MarkerOptions().position(mainEnt).title("Quinnipiac's Main Entrance"))
        val servEnt = LatLng(41.41442402168098, -72.89526743818342)
        mMap.addMarker(MarkerOptions().position(servEnt).title("Service Entrance"))
        val newEnt = LatLng(41.416927924927904, -72.89697228449522 )
        mMap.addMarker(MarkerOptions().position(newEnt).title("New Road Entrance"))
        val faculty = LatLng(41.42023910892058, -72.89495497785633)
        mMap.addMarker(MarkerOptions().position(faculty).title("Faculty Office Building"))
        val affairs = LatLng(41.41863655473415, -72.89161337302575)
        mMap.addMarker(MarkerOptions().position(affairs).title("Student Affairs Center"))
        val patAbbate = LatLng(41.421994305596186, -72.88987044057757)
        mMap.addMarker(MarkerOptions().position(patAbbate).title("Pat Abbate '58 Alumni House and Gardens"))
        val devBuild = LatLng(41.422017094742266, -72.8897136473952)
        mMap.addMarker(MarkerOptions().position(devBuild).title("Development Building"))
        val mail = LatLng(41.41461287382314, -72.894347741008 )
        mMap.addMarker(MarkerOptions().position(mail).title("Mail Services Center"))
        val jewLife = LatLng(41.41881984126079, -72.89884454565268)
        mMap.addMarker(MarkerOptions().position(jewLife).title("Peter C. Herald House for Jewish Life"))
        val alInst = LatLng(41.42017146912345, -72.90045387106935)
        mMap.addMarker(MarkerOptions().position(alInst).title("Albert Schweitzer Institute"))
        val offHR = LatLng(41.42386365823833, -72.88696847038837 )
        mMap.addMarker(MarkerOptions().position(offHR).title("Office of Human Resources"))

        val mainCampus = LatLng(41.4189, -72.8936)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainCampus, 16f))

    }
}