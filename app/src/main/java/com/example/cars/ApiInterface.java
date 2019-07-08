package com.example.cars;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("api/mobile/public/availablecars")
    Call<List<Car>> getCars();
}
