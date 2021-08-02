package com.example.go4lunch.utils;

import com.example.go4lunch.model.details.RestaurantDetails;
import com.example.go4lunch.model.restaurant.RestaurantOutputs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesApiService {

    @GET("nearbysearch/json?")
    Call<RestaurantOutputs> getFollowingPlaces(@Query("location") String location,
                                               @Query("radius") String radius,
                                               @Query("type") String type,
                                               @Query("key") String key);

    @GET("details/json?")
    Call<RestaurantDetails> getFollowingDetails(@Query("place_id") String placeId,
                                                @Query("key") String key);
}