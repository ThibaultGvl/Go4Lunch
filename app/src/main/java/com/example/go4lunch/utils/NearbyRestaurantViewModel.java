package com.example.go4lunch.utils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.Restaurant;

import java.util.List;

public class NearbyRestaurantViewModel extends ViewModel {
    private final NearbyRestaurantRepository mNearbyRestaurantRepository;

    private MutableLiveData<List<Restaurant>>  restaurantsList = new MutableLiveData<>();

    public NearbyRestaurantViewModel(NearbyRestaurantRepository nearbyRestaurantRepository) {
        mNearbyRestaurantRepository = nearbyRestaurantRepository;
    }

    public MutableLiveData<List<Restaurant>> getRestaurantsList(String address, String name) {
        restaurantsList = getRestaurantsListData(address, name);
        return restaurantsList;
    }

    private MutableLiveData<List<Restaurant>> getRestaurantsListData(String address, String name) {
        return mNearbyRestaurantRepository.getRestaurants(address, name);
    }
}
