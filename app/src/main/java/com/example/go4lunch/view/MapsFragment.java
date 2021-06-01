package com.example.go4lunch.view;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentMapsBinding;
import com.example.go4lunch.location.LocationInjection;
import com.example.go4lunch.location.LocationViewModel;
import com.example.go4lunch.location.LocationViewModelFactory;
import com.example.go4lunch.model.restaurant.RestaurantOutputs;
import com.example.go4lunch.model.restaurant.ResultRestaurant;
import com.example.go4lunch.places.NearbyInjection;
import com.example.go4lunch.places.NearbyRestaurantViewModel;
import com.example.go4lunch.places.NearbyViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private LocationViewModel mLocationViewModel;
    private NearbyRestaurantViewModel mNearbyRestaurantViewModel;

    public static MapsFragment newInstance() {
        return (new MapsFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mMapsBinding = FragmentMapsBinding.inflate(inflater, container, false);
        configureLocationViewModel();
        configureNearbyRestaurantViewModel();
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

    /*public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }*/

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (mLocationPermission) {
            /*mLocationViewModel.getLocation().observe(getViewLifecycleOwner(), this::configureLocation);
            LatLng latLng = new LatLng(Objects.requireNonNull(mLastKnownLocation).getLatitude(), mLastKnownLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))*/
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

    private void configureLocation(Location location) {
        mLastKnownLocation = location;
    }

    private void getLocationByViewModel() {
        mLocationViewModel.getLocation(requireContext(), requireActivity());
    }

    private void getPlaces() {
        //mLastKnownLocation == null
        String location = mLastKnownLocation.getLatitude() + "," + mLastKnownLocation.getLongitude();
        mNearbyRestaurantViewModel.getRestaurantsList(location, "10000", "AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4").observe(getViewLifecycleOwner(), this::getRestaurants);
    }

    private void getRestaurants(RestaurantOutputs restaurants) {

        if (restaurants != null) {
            for (ResultRestaurant restaurant : restaurants.getResults()) {
                LatLng restaurantLatLng = new LatLng(restaurant.getGeometry().getLocation().getLat(), restaurant.getGeometry().getLocation().getLng());
                mMap.addMarker(new MarkerOptions().position(restaurantLatLng));
            }
        }
        else {
            Toast.makeText(this.requireContext(), getString(R.string.no_restaurant_found), Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", "view"));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocationPermission) {
            getLastLocation();
        }
    }

    private void configureLocationViewModel() {
        LocationViewModelFactory locationViewModelFactory = LocationInjection.provideMapsViewModelFactory();
        mLocationViewModel = new ViewModelProvider(this, locationViewModelFactory)
                .get(LocationViewModel.class);
        mLocationViewModel.initLocation(requireContext(), requireActivity());
    }

    private void configureNearbyRestaurantViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mNearbyRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestaurantViewModel.class);
    }

}