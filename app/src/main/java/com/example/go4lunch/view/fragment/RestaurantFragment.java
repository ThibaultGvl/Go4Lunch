package com.example.go4lunch.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentRestaurantListBinding;
import com.example.go4lunch.model.restaurant.RestaurantOutputs;
import com.example.go4lunch.model.restaurant.ResultRestaurant;
import com.example.go4lunch.view.adapter.RestaurantRecyclerViewAdapter;
import com.example.go4lunch.viewmodel.places.NearbyInjection;
import com.example.go4lunch.viewmodel.places.NearbyRestaurantViewModel;
import com.example.go4lunch.viewmodel.places.NearbyViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class RestaurantFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private NearbyRestaurantViewModel mNearbyRestaurantViewModel;

    private final List<ResultRestaurant> mRestaurants = new ArrayList<>();

    private final RestaurantRecyclerViewAdapter mAdapter = new RestaurantRecyclerViewAdapter
            (mRestaurants);

    private Location mLastKnownLocation;

    private boolean mLocationPermission;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RestaurantFragment() {
    }

    @SuppressWarnings("unused")
    public static RestaurantFragment newInstance(int columnCount) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                (this.requireContext());
        this.getPlaces();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.go4lunch.databinding.FragmentRestaurantListBinding fragmentRestaurantListBinding
                = FragmentRestaurantListBinding.inflate(inflater, container, false);
        RecyclerView view = fragmentRestaurantListBinding.getRoot();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                (this.requireContext());
        Context context = view.getContext();
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setAdapter(mAdapter);
        this.configureNearbyRestaurantViewModel();
            this.getPlaces();
        return view;
    }

    @SuppressLint("MissingPermission")
    private void getPlaces() {
        if (mLocationPermission) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                    (requireContext());
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnownLocation = task.getResult();
                LatLng mLastKnownLocationLatLng = new LatLng(mLastKnownLocation.getLatitude(),
                        mLastKnownLocation.getLongitude());
                String mLastKnownLocationString = mLastKnownLocationLatLng.toString();
                mNearbyRestaurantViewModel.getRestaurantsList(mLastKnownLocationString,"1000"
                        , BuildConfig.API_KEY)
                        .observe(getViewLifecycleOwner(), this::getRestaurants);
            });
        }
        else {
            getLocationPermission(requireContext(), requireActivity());
        }
    }

    private void getRestaurants(RestaurantOutputs restaurants) {
        if (restaurants != null) {
            if (!mRestaurants.containsAll(restaurants.getResults())) {
                mRestaurants.addAll(restaurants.getResults());
                mAdapter.notifyDataSetChanged();
            }
        }
        else {
            Toast.makeText(this.requireContext(), getString(R.string.no_restaurant_found),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        configureNearbyRestaurantViewModel();
        getPlaces();
    }

    public void getLocationPermission(Context context, Activity activity) {
        mLocationPermission = false;
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermission = true;
        } else {
            int PERMISSION_ID = 26;
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ID);
        }
    }

    private void configureNearbyRestaurantViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory =
                NearbyInjection.provideRestaurantViewModel();
        mNearbyRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestaurantViewModel.class);
    }
}