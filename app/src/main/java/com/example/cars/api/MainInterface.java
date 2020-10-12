package com.example.cars.api;

import com.example.cars.model.CarsList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainInterface {

    @GET("api/mobile/public/availablecars")
    Call<List<CarsList>> getCarsList(
            @Query("page") int page,
            @Query("limit") int limit
    );



}
