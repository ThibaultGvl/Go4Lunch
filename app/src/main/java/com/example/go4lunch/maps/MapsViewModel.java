package com.example.go4lunch.maps;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.Executor;

public class MapsViewModel extends ViewModel {

    private final MapsRepository mMapsRepository;
    private final Executor mExecutor;

    public MapsViewModel(MapsRepository mapsRepository, Executor executor) {
        this.mMapsRepository = mapsRepository;
        this.mExecutor = executor;
    }

    public void getPosition(Context context) {
        mExecutor.execute(() -> MapsRepository.getUserLocation(context));
    }

}
