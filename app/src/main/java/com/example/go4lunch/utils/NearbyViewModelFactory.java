package com.example.go4lunch.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NearbyViewModelFactory implements ViewModelProvider.Factory {

    private final NearbyRestaurantRepository mNearbyRestaurantRepository;

    public NearbyViewModelFactory(NearbyRestaurantRepository nearbyRestaurantRepository) {
        mNearbyRestaurantRepository = nearbyRestaurantRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NearbyRestaurantRepository.class)) {
            return (T) new NearbyRestaurantViewModel(mNearbyRestaurantRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
