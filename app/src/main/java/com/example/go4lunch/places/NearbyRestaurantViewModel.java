package com.example.go4lunch.places;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.details.RestaurantDetails;
import com.example.go4lunch.model.restaurant.NearbyRestaurantOutputs;

public class NearbyRestaurantViewModel extends ViewModel {
    private final NearbyRestaurantRepository mNearbyRestaurantRepository;

    public NearbyRestaurantViewModel(NearbyRestaurantRepository nearbyRestaurantRepository) {
        mNearbyRestaurantRepository = nearbyRestaurantRepository;
    }

    public MutableLiveData<NearbyRestaurantOutputs> getRestaurantsList(String location, String radius, String key) {
        MutableLiveData<NearbyRestaurantOutputs> restaurantsList = mNearbyRestaurantRepository.getRestaurants(location, radius, key);
        return restaurantsList;
    }

    public MutableLiveData<RestaurantDetails> getRestaurantDetails(String placeId, String key) {
        MutableLiveData<RestaurantDetails> restaurant = mNearbyRestaurantRepository.getRestaurant(placeId, key);
        return restaurant;
    }
}
