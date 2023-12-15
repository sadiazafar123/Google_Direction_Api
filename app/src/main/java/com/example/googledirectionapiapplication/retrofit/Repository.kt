package com.example.googledirectionapiapplication.retrofit

class Repository(private val retrofitService:RetrofitClient) {
    fun drawDirection(origin: String, destination: String, key: String)=retrofitService.getInstance().
    getDirection(origin,destination,key)


}