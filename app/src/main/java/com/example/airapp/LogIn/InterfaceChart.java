package com.example.airapp.LogIn;

import com.example.airapp.ReponseChart;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InterfaceChart {
    @POST("/api/master/asset/datapoint/{assetId}/attribute/{attributeName}")
    Call<List<ReponseChart>> getChart(
            @Path("assetId") String assetId,
            @Path("attributeName") String attributeName,
            @Header("Authorization") String authorization,
            @Body JsonObject jsonObject
            );
}
