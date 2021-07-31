package com.example.go4lunch.viewmodel.places;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.details.RestaurantDetails;
import com.example.go4lunch.model.restaurant.RestaurantOutputs;

public class NearbyRestaurantViewModel extends ViewModel {
    private final NearbyRestaurantRepository mNearbyRestaurantRepository;

    public NearbyRestaurantViewModel(NearbyRestaurantRepository nearbyRestaurantRepository) {
        mNearbyRestaurantRepository = nearbyRestaurantRepository;
    }

    public MutableLiveData<RestaurantOutputs> getRestaurantsList(String location, String radius, String key) {
        return mNearbyRestaurantRepository.getRestaurants(location, radius, key);
    }

    public MutableLiveData<RestaurantDetails> getRestaurantDetails(String placeId, String key) {
        return mNearbyRestaurantRepository.getRestaurant(placeId, key);
    }
}
