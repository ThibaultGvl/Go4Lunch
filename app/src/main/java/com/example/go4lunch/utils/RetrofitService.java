package com.example.go4lunch.utils;

import com.example.go4lunch.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("restaurants/{name,opening_hours,photos,place_id,formatted_address,rating}/following")
    Call<List<Restaurant>> getFollowing(@Path("name,schedules,picture,id,address") String name, String schedules, long picture, String id, String address, int rank);

    public static final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
}
