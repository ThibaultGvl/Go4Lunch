package com.example.go4lunch.utils;

import com.example.go4lunch.places.PlacesApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
