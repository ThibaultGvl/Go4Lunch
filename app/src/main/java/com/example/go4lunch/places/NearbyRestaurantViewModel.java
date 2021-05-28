package com.example.go4lunch.places;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.places.NearbyRestaurantRepository;

import java.util.List;

public class NearbyRestaurantViewModel extends ViewModel {
    private final NearbyRestaurantRepository mNearbyRestaurantRepository;

    private MutableLiveData<List<Restaurant>>  restaurantsList = new MutableLiveData<>();

    public NearbyRestaurantViewModel(NearbyRestaurantRepository nearbyRestaurantRepository) {
        mNearbyRestaurantRepository = nearbyRestaurantRepository;
    }

    public MutableLiveData<List<Restaurant>> getRestaurantsList(String address, String radius, String key) {
        restaurantsList = getRestaurantsListData(address, radius, key);
        return restaurantsList;
    }

    private MutableLiveData<List<Restaurant>> getRestaurantsListData(String address, String radius, String key) {
        return mNearbyRestaurantRepository.getRestaurants(address, radius, key);
    }
}
