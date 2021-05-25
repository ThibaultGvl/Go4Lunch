package com.example.go4lunch.maps;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

public class MapsViewModel extends ViewModel {

    private final MapsRepository mMapsRepository;
    private final Executor mExecutor;

    public MapsViewModel(MapsRepository mapsRepository, Executor executor) {
        this.mMapsRepository = mapsRepository;
        this.mExecutor = executor;
    }

    public void getLocationPermission() {
        mExecutor.execute(MapsRepository::getLastLatLng);
    }

    public Location getLocation(Context context) {
        mExecutor.execute(() -> MapsRepository.getLastLocation(context));
        return null;
    }
}
