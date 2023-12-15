package com.example.googledirectionapiapplication

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.blue
import androidx.core.graphics.red
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.googledirectionapiapplication.databinding.ActivityMapsBinding
import com.example.googledirectionapiapplication.extensionclasses.UtilFunction
import com.example.googledirectionapiapplication.extensionclasses.UtilFunction.Companion.apiKey
import com.example.googledirectionapiapplication.models.PostData
import com.example.googledirectionapiapplication.retrofit.MainViewModel
import com.example.googledirectionapiapplication.retrofit.MyViewModelFactory
import com.example.googledirectionapiapplication.retrofit.Repository
import com.example.googledirectionapiapplication.retrofit.RetrofitClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var permissionCode = 101
    lateinit var currentLocation: Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var arrList = ArrayList<LatLng>()
    lateinit var lat11: String
    lateinit var lng11: String
    lateinit var lat22: String
    lateinit var lng22: String
    lateinit var latLngOrigin:LatLng
    lateinit var latLngDestination:LatLng
    lateinit var viewModel: MainViewModel
    lateinit var origin:String
    lateinit var destination:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initialised fusedlocationproviderclient
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        //iniatilized view model
        /*viewModel=ViewModelProvider(this,MyViewModelFactory(Repository(RetrofitClient)))
            .get(MainViewModel::class.java)
        */
        viewModel=ViewModelProvider(this,MyViewModelFactory(Repository(RetrofitClient))).
        get(MainViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getCurrentLocationUser()
        mMap.setOnMapClickListener {
            if (arrList.size < 2) {
                arrList.add(LatLng(it.latitude, it.longitude))

                if (arrList.size == 1) {
                    lat11 = arrList[0].latitude.toString()
                    lng11 = arrList[0].longitude.toString()
                     origin=lat11+ "," +lng11

                    latLngOrigin = LatLng(arrList[0].latitude,arrList[0].longitude)
                    mMap.addMarker(MarkerOptions().position(latLngOrigin).title("Ayala"))


                }
                if (arrList.size == 2) {
                    lat22 = arrList[1].latitude.toString()
                    lng22 = arrList[1].longitude.toString()
                    destination= lat22+ "," +lng22

                    latLngDestination = LatLng(arrList[1].latitude,arrList[1].longitude)
                    mMap.addMarker(MarkerOptions().position(latLngDestination).title("Ayala"))
                    drawDirection(origin,destination)



                }
            }
            else{
                mMap.clear()
                arrList.clear()
                getCurrentLocationUser()
            }
        }
    }


    private fun getCurrentLocationUser() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), permissionCode
            )
            return
        }
        val getLocation =
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                Log.v("currentlocation", "$location")
                if (location != null) {
                    Log.v("location","my current location")
                    currentLocation = location
                    Toast.makeText(
                        this, currentLocation.latitude.toString() +
                                "" + currentLocation.longitude.toString(), Toast.LENGTH_SHORT
                    ).show()
                    val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                    val markerOptions = MarkerOptions().position(latLng).title("current location")
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
                    mMap.setMyLocationEnabled(true)

                }
            }
    }
    private fun drawDirection(origin: String, destination: String) {
        Log.v("drawfun","draw funstion"+origin+ " " +destination)
        //var key:String="AIzaSyBKduijD07b_KXcN9MuegadU8rCIfM8nkw"
      //  val postData:PostData.DrawRouteDirection = PostData.DrawRouteDirection()
        viewModel.drawDirection(origin,destination, apiKey)
        viewModel.routeList.observeForever(){routeList->
            Log.v("drawDirection","$routeList")
            val responseRouteList : List<PostData.RouteClass> = routeList.routes
            val path: MutableList<List<LatLng>> = ArrayList()

            for (i in 0 until responseRouteList.size){

                path.add(PolyUtil.decode(responseRouteList[i].overview_polyline.points))
            }



            var legsArrayList= ArrayList<PostData.LegsClass>()
            var stepsArrayList = ArrayList<PostData.StepsClass>()

            if (responseRouteList.size > 0){
                legsArrayList  = responseRouteList[0].legs
                stepsArrayList = legsArrayList[0].steps
            }
            for(i in 0 until path.size){
                mMap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
            }


            }
    }

    }