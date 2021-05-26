package com.example.go4lunch.maps;

import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

public class MapsViewModel extends ViewModel {

    private final MapsRepository mMapsRepository;
    private final Executor mExecutor;

    private MutableLiveData<Location> mLocationLiveData;

    public MapsViewModel(MapsRepository mapsRepository, Executor executor) {
        this.mMapsRepository = mapsRepository;
        this.mExecutor = executor;
    }

    public void initLocation(Context context) {
        if (mLocationLiveData != null) {
            return;
        }
        mLocationLiveData = mMapsRepository.getLastLocation(context);
    }

    public MutableLiveData<Location> getLocation(Context context) {
        return mLocationLiveData;
    }

}
