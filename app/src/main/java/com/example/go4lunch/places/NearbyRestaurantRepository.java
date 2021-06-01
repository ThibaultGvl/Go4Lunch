package com.example.go4lunch.places;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.restaurant.RestaurantOutputs;
import com.example.go4lunch.utils.RetrofitService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearbyRestaurantRepository {
    private static PlacesApiService placesApiService;
    private final MutableLiveData<RestaurantOutputs> sRestaurants = new MutableLiveData<>();
    private final String mBaseUrlForNearbySearch = "https://maps.googleapis.com/maps/api/place/";

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

    public MutableLiveData<RestaurantOutputs> getRestaurants(String location, String radius, String key) {
        Call<RestaurantOutputs> restaurantsList = placesApiService.getFollowingPlaces(location, radius,"restaurant", key);
        restaurantsList.enqueue(new Callback<RestaurantOutputs>() {
            @Override
            public void onResponse(@NotNull Call<RestaurantOutputs> call, @NotNull Response<RestaurantOutputs> response) {
                sRestaurants.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<RestaurantOutputs> call, @NotNull Throwable t) {
                sRestaurants.postValue(null);
            }
        });
        return sRestaurants;
    }

    public MutableLiveData<RestaurantOutputs> getRestaurant(String placeId, String key) {
        MutableLiveData<RestaurantOutputs> restaurantOutputsMutableLiveData = new MutableLiveData<>();
        Call<RestaurantOutputs> restaurantOutputsCall = placesApiService.getFollowingDetails(placeId, key);
        restaurantOutputsCall.enqueue(new Callback<RestaurantOutputs>() {
            @Override
            public void onResponse(Call<RestaurantOutputs> call, Response<RestaurantOutputs> response) {
                restaurantOutputsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RestaurantOutputs> call, Throwable t) {
                restaurantOutputsMutableLiveData.postValue(null);
            }
        });
        return restaurantOutputsMutableLiveData;
    }
}
