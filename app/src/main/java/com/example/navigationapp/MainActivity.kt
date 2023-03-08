package com.example.navigationapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.navigation.NavigationView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import java.security.AccessController.getContext

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val pERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var mMap: GoogleMap
    var currentLocation: LatLng = LatLng(41.41907904309516, -72.89360638665737)
    var mapType = ""
    lateinit var context: Context
    lateinit var toggle: ActionBarDrawerToggle
    var isShowing1: Boolean = true
    var isShowing2: Boolean = true
    var isShowing3: Boolean = true
    var isShowing4: Boolean = true
    var isShowing5: Boolean = true
    data class MyObject(val title: String, val lat: Double, val long: Double, val category: String, val showing: Boolean) {
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val TAG = MainActivity::class.java.simpleName
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navView)
        currShowing(isShowing1, isShowing2, isShowing3, isShowing4, isShowing5)
        context = this.applicationContext
        // Fetching API_KEY which we wrapped
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData["com.google.android.geo.API_KEY"]
        val apiKey = value.toString()

        // Initializing the Places API with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        val btn = findViewById<Button>(R.id.currentLoc)
        btn.setOnClickListener {
            getLastLocation(mMap)
        }

        val mapFragment = supportFragmentManager.findFragmentById((R.id.maps)) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initializing fused location client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
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
                R.id.miOther -> if (isShowing5) {
                    isShowing5 = false
                    createPins(mMap)
                } else {
                    isShowing5 = true
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
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

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

    private fun currShowing(Res: Boolean, Aca: Boolean, Din: Boolean, Rec: Boolean, Oth: Boolean) {
        val showingView = findViewById<TextView>(R.id.showing)
        showingView.text = "Currently Showing: " + if (Res) {
            "Residential"
        } else {
            ""
        } + if (Aca) {
            ", Academic"
        } else {
            ""
        } + if (Din) {
            ", Dining"
        } else {
            ""
        } + if (Rec) {
            ", Recreation"
        } else {
            ""
        } + if (Oth) {
            ", Other"
        } else {
            ""
        } + if(!Res && !Aca && !Din && !Rec && !Oth) {
            "None"
        } else {
            ""
        }
    }

    private fun createPins(mMap: GoogleMap) {
        val Residencemarkers = mutableListOf<MyObject>()
        val Academicmarkers = mutableListOf<MyObject>()
        val Diningmarkers = mutableListOf<MyObject>()
        val Recreationmarkers = mutableListOf<MyObject>()
        val Othermarkers = mutableListOf<MyObject>()
        mMap.clear()
        Residencemarkers.add(MyObject("The Commons", 41.41735437231641,-72.89309639489136, "Residence Halls", isShowing1))
        Residencemarkers.add(MyObject("The Hill", 41.41805075394168, -72.89230888456365, "Residence Halls", isShowing1))
        Residencemarkers.add(MyObject("Irmagarde Tator Residence Hall", 41.418844634444994, -72.89259038684314, "Residence Halls", isShowing1))
        Residencemarkers.add(MyObject("Dana English Residence Hall", 41.41910528134656, -72.89200880413638, "Residence Halls", isShowing1))
        Residencemarkers.add(MyObject("The Village", 41.41820589567165, -72.89117583700015, "Residence Halls", isShowing1))
        Residencemarkers.add(MyObject("Perloth Residence Hall", 41.41920237904667, -72.89087212637034, "Residence Halls", isShowing1))
        Residencemarkers.add(MyObject("Larson Residence Hall", 41.41947180215361, -72.89134331922317, "Residence Halls", isShowing1))
        Residencemarkers.add(MyObject("Troup Residence Hall", 41.41973239065013, -72.89040093351448, "Residence Halls", isShowing1))
        Residencemarkers.add(MyObject("The Complex", 41.41906935535658, -72.89013527108283, "Residence Halls", isShowing1))
        Residencemarkers.add(MyObject("The Ledges", 41.41961821563668, -72.88922685442634, "Residence Halls", isShowing1))
        Residencemarkers.add(MyObject("Mountainview Residence Hall", 41.41998775505592, -72.8888125986298, "Residence Halls", isShowing1))
        //academic
        Academicmarkers.add(MyObject("Center for Communications and Engineering", 41.42018449724887, -72.89757839057904, "Academic Building", isShowing2))
        Academicmarkers.add(MyObject("Echlin Center", 41.41833564400074, -72.89702159337257, "Academic Building", isShowing2))
        Academicmarkers.add(MyObject("Clarice L. Buckman Center/Theater", 41.417924344634756, -72.89652244284237, "Academic Building", isShowing2))
        Academicmarkers.add(MyObject("Tator Hall", 41.418001961998385, -72.89575136217536, "Academic Building", isShowing2))
        Academicmarkers.add(MyObject("Arnold Bernhard Library", 41.41887706277957, -72.89410166186688, "Academic Building", isShowing2))
        Academicmarkers.add(MyObject("Lender School of Business Center", 41.41976342725668, -72.89491840378152, "Academic Building", isShowing2))
        Academicmarkers.add(MyObject("Ed McMahon Communications Center", 41.419420113830185, -72.89563506973184, "Academic Building", isShowing2))
        Academicmarkers.add(MyObject("College of Arts and Sciences", 41.41568615134177, -72.89520349124456, "Academic Building", isShowing2))
        //Dining
        Diningmarkers.add(MyObject("Mount Carmel Dining Hall",41.41807526994349, -72.89446708811674, "Dining Hall", isShowing3))
        Diningmarkers.add(MyObject("Bobcat Den",41.41881571604852, -72.89173436537993, "Dining Hall", isShowing3))
        //Recreation
        Recreationmarkers.add(MyObject("Carl Hansen Student Center",41.41815750775577, -72.89496353668893, "Recreation Building", isShowing4))
        Recreationmarkers.add(MyObject("Recreation and Wellness Center",41.42003562734498, -72.89321581989024, "Recreation Building", isShowing4))
        Recreationmarkers.add(MyObject("Health and Wellness Center",41.42019168861404, -72.89378204596598, "Recreation Building", isShowing4))
        Recreationmarkers.add(MyObject("Catholic Chapel/Center for Religion",41.41556706288183, -72.89468611288991, "Recreation Building", isShowing4))
        //Other
        Othermarkers.add(MyObject("Harwood Gate",1.420708302992026, -72.89868449379156, "Other", isShowing5))
        Othermarkers.add(MyObject("Quinnipiac's Main Entrance",41.42137047881362, -72.89541744137112, "Other", isShowing5))
        Othermarkers.add(MyObject("Service Entrance",41.41442402168098, -72.89526743818342, "Other", isShowing5))
        Othermarkers.add(MyObject("New Road Entrance",41.416927924927904, -72.89697228449522, "Other", isShowing5))
        Othermarkers.add(MyObject("Faculty Office Building",41.42023910892058, -72.89495497785633, "Other", isShowing5))
        Othermarkers.add(MyObject("Student Affairs Center",41.41863655473415, -72.89161337302575, "Other", isShowing5))
        Othermarkers.add(MyObject("Pat Abbate '58 Alumni House and Gardens",41.421994305596186, -72.88987044057757, "Other", isShowing5))
        Othermarkers.add(MyObject("Development Building",41.422017094742266, -72.8897136473952, "Other", isShowing5))
        Othermarkers.add(MyObject("Mail Services Center",41.41461287382314, -72.894347741008, "Other", isShowing5))
        Othermarkers.add(MyObject("Peter C. Herald House for Jewish Life",41.41881984126079, -72.89884454565268, "Other", isShowing5))
        Othermarkers.add(MyObject("Albert Schweitzer Institute",41.42017146912345, -72.90045387106935, "Other", isShowing5))
        Othermarkers.add(MyObject("Office of Human Resources",41.42386365823833, -72.88696847038837, "Other", isShowing5))
        for (MyObject in Residencemarkers.indices) {
            val currObject1 = Residencemarkers.get(MyObject)
            if(currObject1.showing) {
                val lat = currObject1.lat
                val lon = currObject1.long
                val location = LatLng(lat, lon)
                mMap.addMarker(MarkerOptions().position(location).title(currObject1.title).icon(BitmapDescriptorFactory.defaultMarker(41.0F)))
            }
        }
        for (MyObject in Academicmarkers.indices) {
            val currObject2 = Academicmarkers.get(MyObject)
            if(currObject2.showing) {
                val lat = currObject2.lat
                val lon = currObject2.long
                val location = LatLng(lat, lon)
                mMap.addMarker(MarkerOptions().position(location).title(currObject2.title).icon(BitmapDescriptorFactory.defaultMarker(240.0F)))
            }
        }
        for (MyObject in Diningmarkers.indices) {
            val currObject3 = Diningmarkers.get(MyObject)
            if(currObject3.showing) {
                val lat = currObject3.lat
                val lon = currObject3.long
                val location = LatLng(lat, lon)
                mMap.addMarker(MarkerOptions().position(location).title(currObject3.title).icon(BitmapDescriptorFactory.defaultMarker(170.0F)))
            }
        }
        for (MyObject in Recreationmarkers.indices) {
            val currObject4 = Recreationmarkers.get(MyObject)
            if(currObject4.showing) {
                val lat = currObject4.lat
                val lon = currObject4.long
                val location = LatLng(lat, lon)
                mMap.addMarker(MarkerOptions().position(location).title(currObject4.title).icon(BitmapDescriptorFactory.defaultMarker(210.0F)))
            }
        }
        for (MyObject in Othermarkers.indices) {
            val currObject5 = Othermarkers.get(MyObject)
            if(currObject5.showing) {
                val lat = currObject5.lat
                val lon = currObject5.long
                val location = LatLng(lat, lon)
                mMap.addMarker(MarkerOptions().position(location).title(currObject5.title).icon(BitmapDescriptorFactory.defaultMarker(351.0F)))
            }
        }
        currShowing(isShowing1, isShowing2, isShowing3, isShowing4, isShowing5)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        getLastLocation(mMap)
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
                val mainCampus = LatLng(41.4189, -72.8936)
                createPins(mMap)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mainCampus, 16f))
            }
        }
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
                        currentLocation = LatLng(location.latitude, location.longitude)
                        mMap.clear()
                        mMap.addMarker(MarkerOptions().position(currentLocation))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16F))
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
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

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
            currentLocation = LatLng(mLastLocation.latitude, mLastLocation.longitude)
        }
    }

    // function to check if GPS is on
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // Check if location permissions are
    // granted to the application
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    // Request permissions if not granted before
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            pERMISSION_ID
        )
    }

    // What must happen when permission is granted
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == pERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation(mMap)
            }
        }
    }
}

