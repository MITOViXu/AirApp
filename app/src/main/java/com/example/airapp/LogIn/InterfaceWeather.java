package com.example.airapp.LogIn;


import com.example.airapp.RepondWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
public interface InterfaceWeather {
        @GET("api/master/asset/{assetId}")
    Call<RepondWeather> getWeather(
            @Path("assetId") String assetId,
            @Header("Authorization") String authorization);
}
