package com.example.go4lunch.places;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.places.NearbyRestaurantRepository;
import com.example.go4lunch.places.NearbyRestaurantViewModel;

public class NearbyViewModelFactory implements ViewModelProvider.Factory {

    private final NearbyRestaurantRepository mNearbyRestaurantRepository;

    public NearbyViewModelFactory(NearbyRestaurantRepository nearbyRestaurantRepository) {
        mNearbyRestaurantRepository = NearbyRestaurantRepository.getInstance();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NearbyRestaurantRepository.class)) {
            return (T) new NearbyRestaurantViewModel(mNearbyRestaurantRepository);
        }
        return (T) new NearbyRestaurantViewModel(mNearbyRestaurantRepository);
    }
}
