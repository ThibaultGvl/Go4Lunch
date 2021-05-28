package com.example.go4lunch.location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.location.LocationRepository;
import com.example.go4lunch.location.LocationViewModel;

import java.util.concurrent.Executor;

public class LocationViewModelFactory implements ViewModelProvider.Factory {

    private final LocationRepository mLocationRepository;

    private final Executor mExecutor;

    public LocationViewModelFactory(LocationRepository mapsRepository, Executor executor) {
        mLocationRepository = mapsRepository;
        mExecutor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LocationViewModel.class)) {
            return (T) new LocationViewModel(mLocationRepository, mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
