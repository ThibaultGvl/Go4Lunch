package com.example.go4lunch.places;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.restaurant.ResultRestaurant;
import com.example.go4lunch.places.NearbyRestaurantRepository;

import java.util.List;

public class NearbyRestaurantViewModel extends ViewModel {
    private final NearbyRestaurantRepository mNearbyRestaurantRepository;

    private MutableLiveData<List<ResultRestaurant>>  restaurantsList = new MutableLiveData<>();

    public NearbyRestaurantViewModel(NearbyRestaurantRepository nearbyRestaurantRepository) {
        mNearbyRestaurantRepository = nearbyRestaurantRepository;
    }

    public MutableLiveData<List<ResultRestaurant>> getRestaurantsList(String location, String radius, String key) {
        restaurantsList = getRestaurantsListData(location, radius, key);
        return restaurantsList;
    }

    private MutableLiveData<List<ResultRestaurant>> getRestaurantsListData(String location, String radius, String key) {
        return mNearbyRestaurantRepository.getRestaurants(location, radius, key);
    }
}
