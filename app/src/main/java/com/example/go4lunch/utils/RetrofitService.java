package com.example.go4lunch.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static Retrofit client;

    private static Retrofit getInstance(String baseUrl) {

        if (client == null) {
            client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return client;

    }
}
