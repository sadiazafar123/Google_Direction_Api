package com.example.googledirectionapiapplication.retrofit

import com.example.googledirectionapiapplication.models.PostData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    //in @query both key and value append in url e.g url?origin=abc & destination=dce  & key=12345
    @GET("api/directions/json")
    fun getDirection(@Query("origin") origin:String, @Query("destination") destination:String,
    @Query("key") key:String): Call<PostData.DrawRouteDirection>
}