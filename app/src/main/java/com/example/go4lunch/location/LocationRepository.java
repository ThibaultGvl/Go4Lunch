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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class LocationRepository {

    private static GoogleMap mMap;
    private static final int PERMISSION_ID = 26;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private boolean mLocationPermission;


    @SuppressLint("MissingPermission")
    public MutableLiveData<Location> getLastLocation(Context context, Activity activity) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                (context);
        MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
        if (mLocationPermission) {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnownLocation = task.getResult();
                locationMutableLiveData.postValue(mLastKnownLocation);
            });
        }
        else {
            getLocationPermission(context, activity);
        }
            return locationMutableLiveData;
        }

    public void getLocationPermission(Context context, Activity activity) {
        mLocationPermission = false;
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermission = true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ID);
        }
    }
}
