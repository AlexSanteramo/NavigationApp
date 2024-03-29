package com.example.navigationapp
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.*
import android.app.Activity;
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val pERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var mMap: GoogleMap
    var currentLocation: LatLng = LatLng(41.42018449724887,
        -72.89757839057904)
    var mapType = ""
    lateinit var context: Context
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var apiKey: String
    var tracking: Boolean = false
    var isShowing1: Boolean = true
    var isShowing2: Boolean = true
    var isShowing3: Boolean = true
    var isShowing4: Boolean = true
    var isShowing5: Boolean = true
    var isShowing6: Boolean = true
    var isShowing7: Boolean = true
    val mainCampus = LatLng(41.4189, -72.8936)
    val yorkCampus = LatLng(41.415272,-72.911818)
    val northCampus = LatLng(41.414606,-72.833923)
    var targetLocation = LatLng(41.41395397603173, -72.91115249680632)
    lateinit var currMarker: Marker
    var count: Int = 0
    var polylines = ArrayList<Polyline>()
    var polyCount: Int = 0
    lateinit var mLocationRequest: LocationRequest
    var stop: Boolean = false

    data class MyObject(
        val title: String,
        val lat: Double,
        val long: Double,
        val category: String,
        val showing: Boolean
    ) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val TAG = MainActivity::class.java.simpleName
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)
        context = this.applicationContext
        // Fetching API_KEY which we wrapped
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData["com.google.android.geo.API_KEY"]
        apiKey = value.toString()
        val stopBtn = findViewById<Button>(R.id.stopBtn)
        stopBtn.setOnClickListener {
            stop = true
            stopBtn.visibility = View.INVISIBLE
        }

        // Initializing the Places API with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        val mapFragment = supportFragmentManager.findFragmentById((R.id.maps)) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Initializing fused location client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //initialize side menu
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        //initializes the location requests for directions
        mLocationRequest = LocationRequest()
        //updates every five seconds
        mLocationRequest.interval = 5000
        mLocationRequest.fastestInterval = 5000
        //updates every 15 feet
        //mLocationRequest.smallestDisplacement = 4.572F
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miResidence -> if (isShowing1) {
                    isShowing1 = false
                    createPins(mMap)
                } else {
                    isShowing1 = true
                    createPins(mMap)
                }
                R.id.miAcademic -> if (isShowing2) {
                    isShowing2 = false
                    createPins(mMap)
                } else {
                    isShowing2 = true
                    createPins(mMap)
                }
                R.id.miDining -> if (isShowing3) {
                    isShowing3 = false
                    createPins(mMap)
                } else {
                    isShowing3 = true
                    createPins(mMap)
                }
                R.id.miRecreation -> if (isShowing4) {
                    isShowing4 = false
                    createPins(mMap)
                } else {
                    isShowing4 = true
                    createPins(mMap)
                }
                R.id.miEntrance -> if (isShowing5) {
                    isShowing5 = false
                    createPins(mMap)
                } else {
                    isShowing5 = true
                    createPins(mMap)
                }
                R.id.miParking -> if (isShowing6) {
                    isShowing6 = false
                    createPins(mMap)
                } else {
                    isShowing6 = true
                    createPins(mMap)
                }
                R.id.miOther -> if (isShowing7) {
                    isShowing7 = false
                    createPins(mMap)
                } else {
                    isShowing7 = true
                    createPins(mMap)
                }
                R.id.miAll -> {
                    isShowing1 = true
                    isShowing2 = true
                    isShowing3 = true
                    isShowing4 = true
                    isShowing5 = true
                    createPins(mMap)
                }
                R.id.miNone -> {
                    isShowing1 = false
                    isShowing2 = false
                    isShowing3 = false
                    isShowing4 = false
                    isShowing5 = false
                    createPins(mMap)
                }
                R.id.miMain -> {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainCampus, 16f))
                }
                R.id.miYork -> {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yorkCampus, 16f))
                }
                R.id.miNorth -> {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(northCampus, 16f))
                }
            }
            true
        }
    }
    //registers that a option in side menu was clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    //function called when map style needs to be changed
    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
                println("Style not Generated")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }
    //creates all pins on the campuses
    private fun createPins(mMap: GoogleMap) {
        mMap.clear()
        val Residencemarkers = mutableListOf<MyObject>()
        val Academicmarkers = mutableListOf<MyObject>()
        val Diningmarkers = mutableListOf<MyObject>()
        val Recreationmarkers = mutableListOf<MyObject>()
        val Entrancemarkers = mutableListOf<MyObject>()
        val Parkingmarkers = mutableListOf<MyObject>()
        val Othermarkers = mutableListOf<MyObject>()
        Residencemarkers.add(
            MyObject(
                "The Commons",
                41.41735437231641,
                -72.89309639489136,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "The Hill",
                41.41805075394168,
                -72.89230888456365,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Irmagarde Tator Residence Hall",
                41.418844634444994,
                -72.89259038684314,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Dana English Residence Hall",
                41.41910528134656,
                -72.89200880413638,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "The Village",
                41.41820589567165,
                -72.89117583700015,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Perloth Residence Hall",
                41.41920237904667,
                -72.89087212637034,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Larson Residence Hall",
                41.41947180215361,
                -72.89134331922317,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Troup Residence Hall",
                41.41973239065013,
                -72.89040093351448,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "The Complex",
                41.41906935535658,
                -72.89013527108283,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "The Ledges",
                41.41961821563668,
                -72.88922685442634,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Mountainview Residence Hall",
                41.41998775505592,
                -72.8888125986298,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Eastview",
                41.4158708508581,
                -72.91136188652469,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Westview",
                41.41475551617654,
                -72.9137978279457,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Crecent",
                41.41563984578765,
                -72.91338022354601,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Townhouse 1",
                41.41454601583827,
                -72.91295169589237,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Townhouse 2",
                41.414367433866204,
                -72.91320976465882,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Townhouse 3",
                41.41529655016443,
                -72.9140306892031,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Townhouse 4",
                41.41559653465686,
                -72.91403421825439,
                "Residence Halls",
                isShowing1
            )
        )
        Residencemarkers.add(
            MyObject(
                "Townhouse 5",
                41.41566261419077,
                -72.91371509119,
                "Residence Halls",
                isShowing1
            )
        )
        //academic
        Academicmarkers.add(
            MyObject(
                "Center for Communications and Engineering",
                41.41934490735028,
                -72.89724691398834,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "Echlin Center",
                41.41833564400074,
                -72.89702159337257,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "Clarice L. Buckman Center/Theater",
                41.417924344634756,
                -72.89652244284237,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "Tator Hall",
                41.418001961998385,
                -72.89575136217536,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "Arnold Bernhard Library",
                41.41887706277957,
                -72.89410166186688,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "Lender School of Business Center",
                41.41976342725668,
                -72.89491840378152,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "Ed McMahon Communications Center",
                41.419420113830185,
                -72.89563506973184,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "College of Arts and Sciences",
                41.41568615134177,
                -72.89520349124456,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "Theater Arts Center",
                41.40854530521948,
                -72.91092573117255,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "Lynne L. Pantalena Law Library",
                41.41368568162904,
                -72.83283120193238,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "School of Education",
                41.414822198671494,
                -72.83499643323219,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "School of Nursing",
                41.41404842584082,
                -72.83424423659599,
                "Academic Building",
                isShowing2
            )
        )
        Academicmarkers.add(
            MyObject(
                "School of Law",
                41.41406121180846,
                -72.83307491629463,
                "Academic Building",
                isShowing2
            )
        )
        //Dining
        Diningmarkers.add(
            MyObject(
                "Mount Carmel Dining Hall",
                41.41807526994349,
                -72.89446708811674,
                "Dining Hall",
                isShowing3
            )
        )
        Diningmarkers.add(
            MyObject(
                "Bobcat Den",
                41.41881571604852,
                -72.89173436537993,
                "Dining Hall",
                isShowing3
            )
        )
        Diningmarkers.add(
            MyObject(
                "North Haven Dining Hall",
                41.413681045971146,
                -72.83381794113113,
                "Dining Hall",
                isShowing3
            )
        )
        //Recreation
        Recreationmarkers.add(
            MyObject(
                "Carl Hansen Student Center",
                41.41815750775577,
                -72.89496353668893,
                "Recreation Building",
                isShowing4
            )
        )
        Recreationmarkers.add(
            MyObject(
                "Recreation and Wellness Center",
                41.42003562734498,
                -72.89321581989024,
                "Recreation Building",
                isShowing4
            )
        )
        Recreationmarkers.add(
            MyObject(
                "Health and Wellness Center",
                41.42019168861404,
                -72.89378204596598,
                "Recreation Building",
                isShowing4
            )
        )
        Recreationmarkers.add(
            MyObject(
                "Catholic Chapel/Center for Religion",
                41.41556706288183,
                -72.89468611288991,
                "Recreation Building",
                isShowing4
            )
        )
        Recreationmarkers.add(
            MyObject(
                "Rocky Top Student Center",
                41.41541545917649,
                -72.91245033531078,
                "Recreation Building",
                isShowing4
            )
        )
        Recreationmarkers.add(MyObject("Rocky Top Student Center", 41.41541545917649,-72.91245033531078, "Recreation Building", isShowing4))

        //Entrances
        Entrancemarkers.add(
            MyObject(
                "Quinnipiac's Main Entrance",
                41.42137047881362,
                -72.89541744137112,
                "Entrance",
                isShowing5
            )
        )
        Entrancemarkers.add(
            MyObject(
                "Harwood Gate",
                41.420708302992026,
                -72.89868449379156,
                "Entrance",
                isShowing5
            )
        )
        Entrancemarkers.add(
            MyObject(
                "Service Entrance",
                41.41442402168098,
                -72.89526743818342,
                "Entrance",
                isShowing5
            )
        )
        Entrancemarkers.add(
            MyObject(
                "New Road Entrance",
                41.416927924927904,
                -72.89697228449522,
                "Entrance",
                isShowing5
            )
        )

        //Parking Lots
        Parkingmarkers.add(
            MyObject(
                "Harwood Gate Lot",
                41.41939554615611,
                -72.89830965372295,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "CCE Lot",
                41.420304671831005,
                -72.89707583756757,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "FOB Lot",
                41.420254388664326,
                -72.89576691956741,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "North Lot",
                41.42100066687344,
                -72.89405740791054,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "Development Lot",
                41.42237224541304,
                -72.88987845256857,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "Buckman/Tator Lot",
                41.4177712811312,
                -72.89600244051901,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "Hogan Lot",
                41.42050324020075,
                -72.88743625315078,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "Hilltop Lot",
                41.41749729926375,
                -72.89078785026854,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "CAS Lot",
                41.41589446677894,
                -72.8943123466846,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "Front Lot: Students",
                41.413897045785035,
                -72.83742236870552,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "Front Lot: Faculty & Staff",
                41.41404000674213,
                -72.83649499234879,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "Visitor Parking",
                41.41498009570924,
                -72.83594934665517,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "Special Events Lot",
                41.41566771499255,
                -72.83513230151645,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "General Surface Lot",
                41.41552134120051,
                -72.83406560369647,
                "Parking",
                isShowing6
            )
        )
        Parkingmarkers.add(
            MyObject(
                "North Haven Parking Garage",
                41.41477305706591,
                -72.83224712022313,
                "Parking",
                isShowing6
            )
        )

        //Other
        Othermarkers.add(
            MyObject(
                "Faculty Office Building",
                41.42023910892058,
                -72.89495497785633,
                "Other",
                isShowing7
            )
        )
        Othermarkers.add(
            MyObject(
                "Student Affairs Center",
                41.41863655473415,
                -72.89161337302575,
                "Other",
                isShowing7
            )
        )
        Othermarkers.add(
            MyObject(
                "Pat Abbate '58 Alumni House and Gardens",
                41.421994305596186,
                -72.88987044057757,
                "Other",
                isShowing7
            )
        )
        Othermarkers.add(
            MyObject(
                "Development Building",
                41.422017094742266,
                -72.8897136473952,
                "Other",
                isShowing7
            )
        )
        Othermarkers.add(
            MyObject(
                "Mail Services Center",
                41.41461287382314,
                -72.894347741008,
                "Other",
                isShowing7
            )
        )
        Othermarkers.add(
            MyObject(
                "Peter C. Herald House for Jewish Life",
                41.41881984126079,
                -72.89884454565268,
                "Other",
                isShowing7
            )
        )
        Othermarkers.add(
            MyObject(
                "Albert Schweitzer Institute",
                41.42017146912345,
                -72.90045387106935,
                "Other",
                isShowing7
            )
        )
        Othermarkers.add(
            MyObject(
                "Office of Human Resources",
                41.42386365823833,
                -72.88696847038837,
                "Other",
                isShowing7
            )
        )
        Othermarkers.add(MyObject("M&T Bank Stadium", 41.41395397603173, -72.91115249680632, "Other", isShowing7))
        Parkingmarkers.add(MyObject("Parking Garage", 41.41671534178106,-72.90928641281137, "Parking", isShowing6))
        Parkingmarkers.add(MyObject("Eastview Lot", 41.41581623986003,-72.91056084834204, "Parking", isShowing6))
        Parkingmarkers.add(MyObject("Westview Lot", 41.41380269873312,-72.91323166794936, "Parking", isShowing6))
        Parkingmarkers.add(MyObject("M&T Bank Stadium Lot", 41.41484474376903,-72.91102858745744, "Parking", isShowing6))
        Parkingmarkers.add(MyObject("Crecent Lot", 41.4154189641527,-72.91325127335199, "Parking", isShowing6))
        for (MyObject in Residencemarkers.indices) {
            val currObject1 = Residencemarkers.get(MyObject)
            if (currObject1.showing) {
                val lat = currObject1.lat
                val lon = currObject1.long
                val location = LatLng(lat, lon)
                mMap.addMarker(
                    MarkerOptions().position(location).title(currObject1.title)
                        .icon(
                            bitmapDescriptorFromVector(
                                this@MainActivity,
                                R.drawable.residence_hall
                            )
                        )
                )
            }
        }
        for (MyObject in Academicmarkers.indices) {
            val currObject2 = Academicmarkers.get(MyObject)
            if (currObject2.showing) {
                val lat = currObject2.lat
                val lon = currObject2.long
                val location = LatLng(lat, lon)
                if (currObject2.title.equals("Arnold Bernhard Library")) {
                    mMap.addMarker(
                        MarkerOptions().position(location).title(currObject2.title)
                            .icon(
                                bitmapDescriptorFromVector(
                                    this@MainActivity,
                                    R.drawable.library_book
                                )
                            )
                    )
                } else if (currObject2.title.equals("Lynne L. Pantalena Law Library")) {
                        mMap.addMarker(
                            MarkerOptions().position(location).title(currObject2.title)
                                .icon(
                                    bitmapDescriptorFromVector(
                                        this@MainActivity,
                                        R.drawable.library_book
                                    )
                                )
                        )
                } else {
                    mMap.addMarker(
                        MarkerOptions().position(location).title(currObject2.title)
                            .icon(
                                bitmapDescriptorFromVector(
                                    this@MainActivity,
                                    R.drawable.academic_building
                                )
                            )
                    )
                }
            }
        }
        for (MyObject in Diningmarkers.indices) {
            val currObject3 = Diningmarkers.get(MyObject)
            if (currObject3.showing) {
                val lat = currObject3.lat
                val lon = currObject3.long
                val location = LatLng(lat, lon)
                mMap.addMarker(
                    MarkerOptions().position(location).title(currObject3.title)
                        .icon(bitmapDescriptorFromVector(this@MainActivity, R.drawable.dining_hall))
                )

            }
        }
        for (MyObject in Recreationmarkers.indices) {
            val currObject4 = Recreationmarkers.get(MyObject)
            if (currObject4.showing) {
                val lat = currObject4.lat
                val lon = currObject4.long
                val location = LatLng(lat, lon)
                if (currObject4.title.equals("Catholic Chapel/Center for Religion")) {
                    mMap.addMarker(
                        MarkerOptions().position(location).title(currObject4.title)
                            .icon(
                                bitmapDescriptorFromVector(
                                    this@MainActivity,
                                    R.drawable.religious_center
                                )
                            )
                    )
                } else if (currObject4.title.equals("Health and Wellness Center")) {
                    mMap.addMarker(
                        MarkerOptions().position(location).title(currObject4.title)
                            .icon(
                                bitmapDescriptorFromVector(
                                    this@MainActivity,
                                    R.drawable.health_center
                                )
                            )
                    )
                } else if (currObject4.title.equals("Recreation and Wellness Center")) {
                    mMap.addMarker(
                        MarkerOptions().position(location).title(currObject4.title)
                            .icon(
                                bitmapDescriptorFromVector(
                                    this@MainActivity,
                                    R.drawable.fitness_center
                                )
                            )
                    )
                } else {
                    mMap.addMarker(
                        MarkerOptions().position(location).title(currObject4.title)
                            .icon(
                                bitmapDescriptorFromVector(
                                    this@MainActivity,
                                    R.drawable.student_center
                                )
                            )
                    )
                }
            }
        }
        for (MyObject in Entrancemarkers.indices) {
            val currObject5 = Entrancemarkers.get(MyObject)
            if (currObject5.showing) {
                val lat = currObject5.lat
                val lon = currObject5.long
                val location = LatLng(lat, lon)
                mMap.addMarker(
                    MarkerOptions().position(location).title(currObject5.title)
                        .icon(bitmapDescriptorFromVector(this@MainActivity, R.drawable.campus_entrance)))

            }
        }
        for (MyObject in Parkingmarkers.indices) {
            val currObject6 = Parkingmarkers.get(MyObject)
            if (currObject6.showing) {
                val lat = currObject6.lat
                val lon = currObject6.long
                val location = LatLng(lat, lon)
                mMap.addMarker(
                    MarkerOptions().position(location).title(currObject6.title)
                        .icon(bitmapDescriptorFromVector(this@MainActivity, R.drawable.parking_lot)))

            }
        }
        for (MyObject in Othermarkers.indices) {
            val currObject7 = Othermarkers.get(MyObject)
            if (currObject7.showing) {
                val lat = currObject7.lat
                val lon = currObject7.long
                val location = LatLng(lat, lon)
                mMap.addMarker(
                    MarkerOptions().position(location).title(currObject7.title)
                        .icon(bitmapDescriptorFromVector(this@MainActivity, R.drawable.other_building)))
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
        //default map style
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        //creates drop down for map styles
        val spinner = findViewById<Spinner>(R.id.spinner)
        val typeAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.MapTypes)
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = typeAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                setMapStyle(mMap)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selected_val: Long = spinner.selectedItemId
                if (selected_val == 0L) {
                    mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                    setMapStyle(mMap)
                } else if (selected_val == 1L) {
                    mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
                createPins(mMap)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainCampus, 16f))
            }
        }
        mMap.setOnInfoWindowClickListener {marker ->
            // Handle info window click event
            tracking = true
            val stopBtn = findViewById<Button>(R.id.stopBtn)
            stopBtn.visibility = View.VISIBLE
            val latLng = marker.position
            val latitude = latLng.latitude
            val longitude = latLng.longitude
            targetLocation = LatLng(latitude, longitude)
            getLastLocation(mMap)
            Log.d("TAG", "Button clicked!")
        }
    }

    private fun getDirectionURL(origin: LatLng, dest: LatLng, secret: String): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=walking" +
                "&key=$secret"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url: String) :
        AsyncTask<Void, Void, List<List<LatLng>>>() {
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()
            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices) {
                lineoption.addAll(result[i])
                lineoption.width(14f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
            val _poly = mMap.addPolyline(lineoption)
            polylines.add(_poly)
            if(!tracking) {
                polylines.get(polyCount).remove()
            }
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    // Get current location
    @SuppressLint("MissingPermission")
    private fun getLastLocation(mMap: GoogleMap) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        //uncomment for live
                        currentLocation = LatLng(location.latitude, location.longitude)

                        //test location
                        //currentLocation = LatLng(41.41946257232867,-72.89732149009491)

                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                        mFusedLocationClient.requestLocationUpdates(
                            mLocationRequest, mLocationCallback,
                            Looper.getMainLooper()
                        )
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    // Get current location, if shifted
    // from previous location
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    // If current location could not be located, use last location
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            if(count > 0) {
                currMarker.remove()
            }
            //For Live
            currentLocation = LatLng(mLastLocation.latitude, mLastLocation.longitude)

            //Test
            //currentLocation = LatLng(currentLocation.latitude-.0001734906315058987, currentLocation.longitude+.0006939625260235947)

            Log.d("TAG", "got new location")
            println(polylines.toString())
            var MarkerOptions = MarkerOptions().position(currentLocation).icon(bitmapDescriptorFromVector(this@MainActivity, R.drawable.current))
            currMarker = mMap.addMarker(MarkerOptions)!!
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16F))
            val urll = getDirectionURL(currentLocation, targetLocation, apiKey)
            GetDirection(urll).execute()
            println(polylines)
            //If user is within 300 feet of destination tracking stops
            if((currentLocation.latitude < targetLocation.latitude + .0010409437890354 && currentLocation.latitude > targetLocation.latitude - .0010409437890354) && (currentLocation.longitude < targetLocation.longitude + .0010409437890354 && currentLocation.longitude > targetLocation.longitude - .0010409437890354) || stop == true) {
                Log.d("TAG", "Reached Destination")
                polyCount++
                polylines.get(polyCount-1).remove()
                currMarker.remove()
                mFusedLocationClient.removeLocationUpdates(this)
                tracking = false
            }
            if(tracking) {
                if (polylines.size >= 1) {
                    polyCount++
                    polylines.get(polyCount-1).remove()
                    println(polyCount)
                }
            }
            count++
        }
    }

    // function to check if GPS is on
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // Check if location permissions are
    // granted to the application
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    // Request permissions if not granted before
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            pERMISSION_ID
        )
    }

    // What must happen when permission is granted
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == pERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation(mMap)
            }
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}
