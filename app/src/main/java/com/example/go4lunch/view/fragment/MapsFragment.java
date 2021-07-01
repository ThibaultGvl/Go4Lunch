package com.example.go4lunch.view.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentMapsBinding;
import com.example.go4lunch.model.restaurant.RestaurantOutputs;
import com.example.go4lunch.model.restaurant.ResultRestaurant;
import com.example.go4lunch.view.activity.DetailsActivity;
import com.example.go4lunch.viewmodel.places.NearbyInjection;
import com.example.go4lunch.viewmodel.places.NearbyRestaurantViewModel;
import com.example.go4lunch.viewmodel.places.NearbyViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FragmentMapsBinding mMapsBinding;
    private final int apiKey = R.string.google_maps_key;
    private final int PERMISSION_ID = 26;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final int DEFAULT_ZOOM = 15;
    private Location mLastKnownLocation;
    private boolean mLocationPermission;
    private NearbyRestaurantViewModel mNearbyRestaurantViewModel;
    private double placeLatitude;
    private double placeLongitude;
    private LatLng placePosition;
    private String placeIdFromSearch;

    public static MapsFragment newInstance() {
        return (new MapsFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mMapsBinding = FragmentMapsBinding.inflate(inflater, container, false);
        configureNearbyRestaurantViewModel();
        if (savedInstanceState != null) {
            placeLatitude = requireArguments().getDouble("placeLatitude");
            placeLongitude = requireArguments().getDouble("placeLongitude");
            placeIdFromSearch = requireArguments().getString("placeId");
            placePosition = new LatLng(placeLatitude, placeLongitude);
        }
        return mMapsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureNearbyRestaurantViewModel();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
               (this.requireContext());
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
            getLastLocation();
        mMapsBinding.position.setOnClickListener(v -> getLastLocation());
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getLastLocation();
        if (placePosition != null) {
            getPlaceSearched();
        }
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnownLocation = task.getResult();
                LatLng mLastKnownLocationLatLng = new
                        LatLng(Objects.requireNonNull(mLastKnownLocation).getLatitude(), mLastKnownLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        mLastKnownLocationLatLng, DEFAULT_ZOOM));
                getPlaces();
            });
        }else {
            getLocationPermission(requireContext(), requireActivity());
        }
    }

    private void getPlaceSearched() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placePosition, DEFAULT_ZOOM));
        mMap.addMarker(new MarkerOptions().position(placePosition));
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) marker -> {
            Intent intent = new Intent(this.getContext(), DetailsActivity.class);
            intent.putExtra("placeId", placeIdFromSearch);
            startActivity(intent);
            return false;
        });
    }

    private void getPlaces() {
        String location = mLastKnownLocation.getLatitude() + "," + mLastKnownLocation.getLongitude();
        mNearbyRestaurantViewModel.getRestaurantsList(location, "1000", "AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4").observe(getViewLifecycleOwner(), this::getRestaurants);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getRestaurants(RestaurantOutputs restaurants) {

        if (restaurants != null) {
            for (ResultRestaurant restaurant : restaurants.getResults()) {
                String placeId = restaurant.getPlaceId();
                LatLng restaurantLatLng = new LatLng(restaurant.getGeometry().getLocation().getLat(), restaurant.getGeometry().getLocation().getLng());
                mMap.addMarker(new MarkerOptions().position(restaurantLatLng).icon(getBitmapDescriptor(R.drawable.marker_orange)));
                //Marker markerDefault = mMap.addMarker(new MarkerOptions().position(restaurantLatLng).icon(getBitmapDescriptor(R.drawable.marker_orange)));
                mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) marker -> {
                    Intent intent = new Intent(this.getContext(), DetailsActivity.class);
                    intent.putExtra("placeId", placeId);
                    startActivity(intent);
                    return false;
                });
            }
        }
        else {
            Toast.makeText(this.requireContext(), getString(R.string.no_restaurant_found), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), id, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, 75, 100);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocationPermission && placePosition == null) {
            getLastLocation();
        }
        else if (placePosition != null) {
            getPlaceSearched();
        }
    }

    private void configureNearbyRestaurantViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mNearbyRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestaurantViewModel.class);
    }

}