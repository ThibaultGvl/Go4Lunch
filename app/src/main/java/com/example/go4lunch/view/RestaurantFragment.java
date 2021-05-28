package com.example.go4lunch.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.databinding.FragmentRestaurantBinding;
import com.example.go4lunch.location.LocationInjection;
import com.example.go4lunch.location.LocationViewModel;
import com.example.go4lunch.location.LocationViewModelFactory;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.places.NearbyInjection;
import com.example.go4lunch.places.NearbyRestaurantViewModel;
import com.example.go4lunch.places.NearbyViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class RestaurantFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private LocationViewModel mLocationViewModel;

    private NearbyRestaurantViewModel mNearbyRestaurantViewModel;

    private List<Restaurant> mRestaurants = new ArrayList<>();

    private FragmentRestaurantBinding mFragmentRestaurantBinding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RestaurantFragment() {
    }

    // TODO: Customize parameter initialization
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
        mFragmentRestaurantBinding = FragmentRestaurantBinding.inflate(getLayoutInflater());
        View view = mFragmentRestaurantBinding.getRoot();
        configureMapsViewModel();
        configureNearbyRestaurantViewModel();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentRestaurantBinding = FragmentRestaurantBinding.inflate(inflater, container, false);
        View view = mFragmentRestaurantBinding.getRoot();

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

    private void configureMapsViewModel() {
        LocationViewModelFactory mapsViewModelFactory = LocationInjection.provideMapsViewModelFactory();
        mLocationViewModel = new ViewModelProvider(this, mapsViewModelFactory)
                .get(LocationViewModel.class);
    }

    private void configureNearbyRestaurantViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mNearbyRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestaurantViewModel.class);
    }
}