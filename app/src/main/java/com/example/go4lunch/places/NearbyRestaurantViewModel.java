package com.example.go4lunch.places;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.restaurant.RestaurantOutputs;

public class NearbyRestaurantViewModel extends ViewModel {
    private final NearbyRestaurantRepository mNearbyRestaurantRepository;

    public NearbyRestaurantViewModel(NearbyRestaurantRepository nearbyRestaurantRepository) {
        mNearbyRestaurantRepository = nearbyRestaurantRepository;
    }

    public MutableLiveData<RestaurantOutputs> getRestaurantsList(String location, String radius, String key) {
        MutableLiveData<RestaurantOutputs> restaurantsList = mNearbyRestaurantRepository.getRestaurants(location, radius, key);
        return restaurantsList;
    }

    private MutableLiveData<RestaurantOutputs> getRestaurantDetails(String placeId, String key) {
        MutableLiveData<RestaurantOutputs> restaurant = mNearbyRestaurantRepository.getRestaurant(placeId, key);
        return restaurant;
    }
}
