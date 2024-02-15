package com.example.airapp.LogIn;

import com.example.airapp.RepondMap;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IntergfaceMap {

    @GET("api/master/map")
    Call<RepondMap> getMap();
}
