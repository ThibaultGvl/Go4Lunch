package com.example.go4lunch.location;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.location.LocationRepository;

import java.util.concurrent.Executor;

public class LocationViewModel extends ViewModel {

    private final LocationRepository mLocationRepository;
    private final Executor mExecutor;

    private MutableLiveData<Location> mLocationLiveData;

    public LocationViewModel(LocationRepository mapsRepository, Executor executor) {
        this.mLocationRepository = mapsRepository;
        this.mExecutor = executor;
    }

    public void initLocation(Context context) {
        if (mLocationLiveData != null) {
            return;
        }
        mLocationLiveData = mLocationRepository.getLastLocation(context);
    }

    public MutableLiveData<Location> getLocation(Context context) {
        return mLocationLiveData;
    }

}
