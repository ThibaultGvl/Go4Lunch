package com.example.go4lunch.places;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.utils.RetrofitService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearbyRestaurantRepository {
    private static PlacesApiService placesApiService;
    private final MutableLiveData<List<Restaurant>> sRestaurants = new MutableLiveData<>();
    private final String mBaseUrlForNearbySearch = "https://maps.googleapis.com/maps/api/place/nearbysearch/";

    private static NearbyRestaurantRepository sNearbyRestaurantRepository;

    public static NearbyRestaurantRepository getInstance() {
        if (sNearbyRestaurantRepository == null) {
            sNearbyRestaurantRepository = new NearbyRestaurantRepository();
        }
        return sNearbyRestaurantRepository;
    }

    public NearbyRestaurantRepository() {
        placesApiService = RetrofitService.getPlacesInterface(mBaseUrlForNearbySearch);
    }

    public MutableLiveData<List<Restaurant>> getRestaurants(String address, String radius, String key) {
        Call<List<Restaurant>> restaurantsList = placesApiService.getFollowingPlaces(address, radius, key);
        restaurantsList.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(@NotNull Call<List<Restaurant>> call, @NotNull Response<List<Restaurant>> response) {
                sRestaurants.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<Restaurant>> call, @NotNull Throwable t) {
                sRestaurants.postValue(null);
            }
        });
        return sRestaurants;
    }
}
