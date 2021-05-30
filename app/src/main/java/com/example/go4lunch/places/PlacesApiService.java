package com.example.go4lunch.places;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.restaurant.ResultRestaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlacesApiService {

    @GET("restaurants/{}/following")
    Call<List<ResultRestaurant>> getFollowingPlaces(@Query("address") String address, @Query("radius") String radius, @Query("key") String key);
}