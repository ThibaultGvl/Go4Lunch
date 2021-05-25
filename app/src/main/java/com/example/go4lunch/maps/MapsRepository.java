package com.example.go4lunch.maps;

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

public class MapsRepository {

    private static GoogleMap mMap;
    private static final int PERMISSION_ID = 26;
    @SuppressLint("StaticFieldLeak")
    private static FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15;
    private static Location mLastKnownLocation;
    private static boolean mLocationPermission;

    public static void getLocationPermission(Context context, Activity activity) {
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

    @SuppressLint("MissingPermission")
    public static MutableLiveData<Location> getLastLocation(Context context) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnownLocation = task.getResult();
                locationMutableLiveData.setValue(mLastKnownLocation);
            });
            return locationMutableLiveData;
        }

     public static MutableLiveData<LatLng> getLastLatLng()   {
        MutableLiveData<LatLng> latLngMutableLiveData = new MutableLiveData<>();
         LatLng mLastLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
         latLngMutableLiveData.setValue(mLastLatLng);
         return latLngMutableLiveData;
     }
}
