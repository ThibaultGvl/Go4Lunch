package com.example.go4lunch.maps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.widget.ListView;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;


public class MapsRepository {

    private static final String TAG = "MapsActivity";
    @SuppressLint("StaticFieldLeak")
    private static ListView lastPlaces;
    private static PlacesClient mPlacesClient;
    private static GoogleMap mMap;
    @SuppressLint("StaticFieldLeak")
    private static FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int PERMISSION_ID = 26;
    private static final int M_MAX_ENTRIES = 5;
    private static final int DEFAULT_ZOOM = 15;
    private static String[] mLikelyPlaceNames;
    private static String[] mLikelyPlaceAddresses;
    private static String[] mLikelyPlaceAttributions;
    private static LatLng[] mLikelyPlaceLatLngs;
    private static Location mLastKnownLocation;
    private static final LatLng mDefaultLocation = new LatLng(48.85341, 2.3488);
    private static LocationCallback mCallback;
    private static boolean mLocationPermission;

    @SuppressLint("MissingPermission")
    public static MutableLiveData<LatLng> getUserLocation(Context context) {
        MutableLiveData<LatLng> latLngMutableLiveData = new MutableLiveData<>();
        if (mLocationPermission) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnownLocation = task.getResult();
                LatLng mLastKnownLocationLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        mLastKnownLocationLatLng, DEFAULT_ZOOM));
                mMap.addMarker(new MarkerOptions().position(mLastKnownLocationLatLng));
                latLngMutableLiveData.setValue(mLastKnownLocationLatLng);
            });
        }
        return latLngMutableLiveData;
    }
}
