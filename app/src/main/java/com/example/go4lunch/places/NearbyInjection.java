package com.example.go4lunch.places;

public class NearbyInjection {

    public static NearbyRestaurantRepository provideNearbyRepository() {
        return new NearbyRestaurantRepository();
    }

    public static NearbyViewModelFactory provideRestaurantViewModel() {
        NearbyRestaurantRepository nearbyRestaurantRepository = provideNearbyRepository();
        return new NearbyViewModelFactory(nearbyRestaurantRepository);
    }
}
