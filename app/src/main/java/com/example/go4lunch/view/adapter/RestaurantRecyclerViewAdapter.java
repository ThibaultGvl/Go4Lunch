package com.example.go4lunch.view.adapter;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentRestaurantBinding;
import com.example.go4lunch.model.User;
import com.example.go4lunch.model.restaurant.ResultRestaurant;
import com.example.go4lunch.view.activity.DetailsActivity;
import com.example.go4lunch.viewmodel.users.UserInjection;
import com.example.go4lunch.viewmodel.users.UserViewModel;
import com.example.go4lunch.viewmodel.users.UserViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class RestaurantRecyclerViewAdapter
        extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.ViewHolder> {

    private final List<ResultRestaurant> restaurants;

    private UserViewModel mUserViewModel;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation;

    private final List<User> mUsers = new ArrayList<>();

    public RestaurantRecyclerViewAdapter(List<ResultRestaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        com.example.go4lunch.databinding.FragmentRestaurantBinding fragmentRestaurantBinding =
                (FragmentRestaurantBinding.inflate(LayoutInflater.from(parent.getContext()),
                        parent, false));
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                (parent.getContext());
        configureUserViewModel(parent.getContext());
        return new ViewHolder(fragmentRestaurantBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        ResultRestaurant restaurant = restaurants.get(position);
        Context context = holder.mFragmentRestaurantBinding.getRoot().getContext();
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                mLastKnownLocation = task.getResult();
        if (restaurant.getGeometry() != null) {
            double distanceDouble = meterDistanceBetweenPoints(restaurant.getGeometry()
                    .getLocation().getLat(), restaurant.getGeometry().getLocation().getLng(),
                    mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            String distance = (int) distanceDouble + "m";
            holder.mRestaurantDistance.setText(distance);
        }
            });
        }
        double rating = ((restaurant.getRating()/5)*3);
        String placeId = restaurant.getPlaceId();
        if (restaurant.getPhotos() != null) {
            String photoUrl =
                    "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                            + restaurant.getPhotos().get(0).getPhotoReference() +
                            "&key=" + BuildConfig.API_KEY;
            Glide.with(holder.mRestaurantImage.getContext()).load(photoUrl)
                    .into(holder.mRestaurantImage);
        }
        else {
            holder.mRestaurantImage.setColorFilter(R.color.black);
        }
        if (restaurant.getName() != null) {
            holder.mRestaurantName.setText(restaurant.getName());
        }
        else {
            holder.mRestaurantName.setText(R.string.no_name_found);
        }
        if (restaurant.getVicinity() != null) {
            String address = restaurant.getVicinity().split(",")[0];
            holder.mRestaurantAddress.setText(address);
        }
        else {
            holder.mRestaurantAddress.setText(R.string.unknown);
        }
        if (restaurant.getOpeningHours() != null) {
            if (restaurant.getOpeningHours().getOpenNow()) {
                holder.mRestaurantSchedules.setText(R.string.open);
            } else {
                holder.mRestaurantSchedules.setText(R.string.close);
            }
        }
        else {
            holder.mRestaurantSchedules.setText(R.string.unknown);
        }
        holder.mRestaurantRank.setRating((float) rating);
        mUserViewModel.getUsers()
                    .observe((LifecycleOwner) context, users -> {setUsers(users, restaurant, holder);});
        holder.mFragmentRestaurantBinding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            intent.putExtra("placeId", placeId);
            startActivity(v.getContext(), intent, null);
        });
    }

    private void setUsers(List<User> users, ResultRestaurant restaurant, ViewHolder holder) {
           for (User user : users) {
               if (user.getRestaurant() != null && user.getRestaurant().equals(restaurant.getPlaceId())) {
                   mUsers.add(user);
               } else {
                   mUsers.remove(user);
               }
           }
           if (mUsers.size() != 0) {
               holder.mRestaurantWorkmatesImage.getDisplay();
               String workmates = "(" + mUsers.size() + ")";
               holder.mRestaurantWorkmates.setText(workmates);
           }
        }

    private void configureUserViewModel(Context context) {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider((ViewModelStoreOwner) context,
                mViewModelFactory)
                .get(UserViewModel.class);
        this.mUserViewModel.initUsers(context);
    }

    private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b,
                                              double lng_b) {
        float pk = (float) (180.f/Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentRestaurantBinding mFragmentRestaurantBinding;
        private final TextView mRestaurantName;
        private final TextView mRestaurantAddress;
        private final TextView mRestaurantSchedules;
        private final TextView mRestaurantDistance;
        private final TextView mRestaurantWorkmates;
        private final ImageView mRestaurantWorkmatesImage;
        private final ImageView mRestaurantImage;
        private final RatingBar mRestaurantRank;

        public ViewHolder(FragmentRestaurantBinding fragmentRestaurantBinding) {
            super(fragmentRestaurantBinding.getRoot());
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient
                    (fragmentRestaurantBinding.getRoot().getContext());
            mFragmentRestaurantBinding = fragmentRestaurantBinding;
            mRestaurantName = fragmentRestaurantBinding.restaurantName;
            mRestaurantAddress = fragmentRestaurantBinding.restaurantAddress;
            mRestaurantSchedules = fragmentRestaurantBinding.restaurantSchedules;
            mRestaurantDistance = fragmentRestaurantBinding.restaurantDistance;
            mRestaurantWorkmates = fragmentRestaurantBinding.restaurantWorkmate;
            mRestaurantWorkmatesImage = fragmentRestaurantBinding.restaurantWorkmateImage;
            mRestaurantImage = fragmentRestaurantBinding.restaurantImage;
            mRestaurantRank = fragmentRestaurantBinding.restaurantRank;
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}