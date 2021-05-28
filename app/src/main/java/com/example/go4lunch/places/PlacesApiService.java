package com.example.go4lunch.places;

import com.example.go4lunch.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlacesApiService {

    @GET("restaurants/{address,radius}/following")
    Call<List<Restaurant>> getFollowingPlaces(@Path("address") String address, @Path("radius") String radius, @Query("key") String key);
}