package com.example.go4lunch.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class LocationRepository {

    private static GoogleMap mMap;
    private static final int PERMISSION_ID = 26;
    @SuppressLint("StaticFieldLeak")
    private static FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;


    @SuppressLint("MissingPermission")
    public MutableLiveData<Location> getLastLocation(Context context) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnownLocation = task.getResult();
                locationMutableLiveData.setValue(mLastKnownLocation);
            });
            return locationMutableLiveData;
        }

}
