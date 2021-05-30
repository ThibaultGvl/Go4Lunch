package com.example.go4lunch.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.CursorJoiner;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentRestaurantBinding;
import com.example.go4lunch.location.LocationInjection;
import com.example.go4lunch.location.LocationViewModel;
import com.example.go4lunch.location.LocationViewModelFactory;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.restaurant.ResultRestaurant;
import com.example.go4lunch.places.NearbyInjection;
import com.example.go4lunch.places.NearbyRestaurantViewModel;
import com.example.go4lunch.places.NearbyViewModelFactory;
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

    private int mColumnCount = 1;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private LocationViewModel mLocationViewModel;

    private NearbyRestaurantViewModel mNearbyRestaurantViewModel;

    private List<ResultRestaurant> mRestaurants = new ArrayList<>();

    private FragmentRestaurantBinding mFragmentRestaurantBinding;

    private RestaurantRecyclerViewAdapter mAdapter;

    private Location mLastKnownLocation;

    private boolean mLocationPermission;

    private final int PERMISSION_ID = 26;

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
        this.configureNearbyRestaurantViewModel();
        mFragmentRestaurantBinding = FragmentRestaurantBinding.inflate(getLayoutInflater());
        View view = mFragmentRestaurantBinding.getRoot();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentRestaurantBinding = FragmentRestaurantBinding.inflate(inflater, container, false);
        View view = mFragmentRestaurantBinding.getRoot();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext());

        this.getPlaces();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new RestaurantRecyclerViewAdapter(mRestaurants));
        }
        return view;
    }

    @SuppressLint("MissingPermission")
    private void getPlaces() {
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                    mLastKnownLocation = task.getResult();
            LatLng mLastKnownLocationLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            String mLastKnownLocationString = mLastKnownLocationLatLng.toString();
            List<ResultRestaurant> restaurants = mNearbyRestaurantViewModel.getRestaurantsList(mLastKnownLocationString, "1000m", "AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4").getValue();
            if (restaurants != null) {
                mRestaurants.addAll(restaurants);
                mAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(this.requireContext(), getString(R.string.no_restaurant_found), Toast.LENGTH_SHORT).show();
            }
                });
    }

    private void configureLocationViewModel() {
        LocationViewModelFactory mapsViewModelFactory =
                LocationInjection.provideMapsViewModelFactory();
        mLocationViewModel = new ViewModelProvider(this, mapsViewModelFactory)
                .get(LocationViewModel.class);
    }

    private void configureNearbyRestaurantViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory =
                NearbyInjection.provideRestaurantViewModel();
        mNearbyRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestaurantViewModel.class);
    }
}