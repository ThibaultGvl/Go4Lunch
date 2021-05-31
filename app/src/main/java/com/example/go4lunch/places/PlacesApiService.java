package com.example.go4lunch.places;

import com.example.go4lunch.model.restaurant.ResultRestaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {

    @GET("json?")
    Call<List<ResultRestaurant>> getFollowingPlaces(@Query("location") String location, @Query("radius") String radius, @Query("type") String type, @Query("key") String key);
}