package com.example.go4lunch.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int PERMISSION_ID = 26;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final int DEFAULT_ZOOM = 15;
    private Location mLastKnownLocation;
    private boolean mLocationPermission;

    public static MapsFragment newInstance() {
        return (new MapsFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int apiKey = R.string.google_maps_key;
        Places.initialize(this.requireContext(), String.valueOf(apiKey));
        Places.createClient(this.requireContext());
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext());
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        getLastLocation();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (mLocationPermission) {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnownLocation = task.getResult();
                LatLng mLastKnownLocationLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        mLastKnownLocationLatLng, DEFAULT_ZOOM));
                mMap.addMarker(new MarkerOptions().position(mLastKnownLocationLatLng));
            });
        }
        else {
            getLocationPermission(requireContext(), requireActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocationPermission) {
            getLastLocation();
        }
    }

    private void configureViewModel() {
        MapsViewModelFactory mapsViewModelFactory = MapsInjection.provideMapsViewModelFactory();
        MapsViewModel viewModel = new ViewModelProvider(this, mapsViewModelFactory).get(MapsViewModel.class);
    }

}