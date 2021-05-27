package com.example.go4lunch.utils;

import com.example.go4lunch.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PlacesApiService {
    @GET("restaurants/{formatted_address,name,opening_hours,photos,place_id,rating}/following")
    Call<List<Restaurant>> getFollowingPlaces(@Path("address, name") String address, String name);
}
