package com.example.googledirectionapiapplication.retrofit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.googledirectionapiapplication.models.PostData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(private val repository: Repository):ViewModel() {
    var routeList=MutableLiveData<PostData.DrawRouteDirection>()


    fun drawDirection(origin: String, destination: String, key: String) {
        val response= repository.drawDirection(origin,destination,key)
        response.enqueue(object :Callback<PostData.DrawRouteDirection>{
            override fun onResponse(
                call: Call<PostData.DrawRouteDirection>,
                response: Response<PostData.DrawRouteDirection>
            ) {
                if (response.isSuccessful){
                    Log.v("onResponse","succesful"+response.body())
                    routeList.postValue(response.body())
                }else
                {
                    Log.v("onResponse","on failure"+response.body())
                }
            }

            override fun onFailure(call: Call<PostData.DrawRouteDirection>, t: Throwable) {
                Log.v("onResponse","on failure"+ t.message)
            }
        })


    }
}

