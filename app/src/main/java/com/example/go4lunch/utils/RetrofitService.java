package com.example.go4lunch.utils;

import com.example.go4lunch.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class RetrofitService {


    private static Retrofit getRetrofit(String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

        public static PlacesApiService getPlacesInterface(String url) {
            return getRetrofit(url).create(PlacesApiService.class);
        }
}
