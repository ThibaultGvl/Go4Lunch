package com.example.go4lunch.view.fragments.restaurant;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentRestaurantBinding;
import com.example.go4lunch.model.restaurant.nearby.Photo;
import com.example.go4lunch.model.restaurant.nearby.ResultNearbyRestaurant;
import com.example.go4lunch.view.activity.details.DetailsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.ViewHolder> {

    private final List<ResultNearbyRestaurant> restaurants;

    public RestaurantRecyclerViewAdapter(List<ResultNearbyRestaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        com.example.go4lunch.databinding.FragmentRestaurantBinding fragmentRestaurantBinding =
                (FragmentRestaurantBinding.inflate
                        (LayoutInflater.from(parent.getContext()), parent, false));
        return new ViewHolder(fragmentRestaurantBinding, parent.getContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        holder.configureRestaurant(holder, position, restaurants, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentRestaurantBinding mFragmentRestaurantBinding;
        private final TextView mRestaurantName;
        private final TextView mRestaurantAddress;
        private final TextView mRestaurantSchedules;
        private final TextView mRestaurantDistance;
        private final ImageView mRestaurantWorkmates;
        private final ImageView mRestaurantRank1;
        private final ImageView mRestaurantRank2;
        private final ImageView mRestaurantRank3;
        private final ImageView mRestaurantImage;
        private final FusedLocationProviderClient mFusedLocationProviderClient;
        private Location mLastKnownLocation;

        public ViewHolder(FragmentRestaurantBinding fragmentRestaurantBinding, Context context) {
            super(fragmentRestaurantBinding.getRoot());
            mFragmentRestaurantBinding = fragmentRestaurantBinding;
            mRestaurantName = fragmentRestaurantBinding.restaurantName;
            mRestaurantAddress = fragmentRestaurantBinding.restaurantAddress;
            mRestaurantSchedules = fragmentRestaurantBinding.restaurantSchedules;
            mRestaurantDistance = fragmentRestaurantBinding.restaurantDistance;
            mRestaurantWorkmates = fragmentRestaurantBinding.restaurantWorkmate;
            mRestaurantRank1 = fragmentRestaurantBinding.restaurantRank1;
            mRestaurantRank2 = fragmentRestaurantBinding.restaurantRank2;
            mRestaurantRank3 = fragmentRestaurantBinding.restaurantRank3;
            mRestaurantImage = fragmentRestaurantBinding.restaurantImage;
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }

        private void configureRestaurant(@NotNull final ViewHolder holder, int position, List<ResultNearbyRestaurant> restaurants, Context context) {
            ResultNearbyRestaurant mRestaurant = restaurants.get(position);
            double rating = ((mRestaurant.getRating() / 5) * 3);
            String placeId = mRestaurant.getPlaceId();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                    mLastKnownLocation = task.getResult();
                    com.example.go4lunch.model.restaurant.nearby.Location mRestaurantLatLng = mRestaurant.getGeometry().getLocation();
                    double distance = meterDistanceBetweenPoints(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), mRestaurantLatLng.getLat(), mRestaurantLatLng.getLng());
                    String distanceString = Double.toString(distance);
                    mRestaurantDistance.setText(distanceString);
                });
            }

                if (mRestaurant.getPhotos() != null) {
                    Photo photo = mRestaurant.getPhotos().get(0);
                    if (photo != null && !photo.getPhotoReference().isEmpty() && !photo.getPhotoReference().equals("null")) {
                        String photoString = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + photo.getPhotoReference() + "&sensor=false&key=" + "AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4";
                        Glide.with(holder.mRestaurantImage.getContext())
                                .load(photoString)
                                .error(R.drawable.ic_logo_go4lunch)
                                .into(holder.mRestaurantImage);
                    }
                } else {
                    holder.mRestaurantImage.setColorFilter(R.color.primary_color);
                }
                if (mRestaurant.getName() != null) {
                    holder.mRestaurantName.setText(mRestaurant.getName());
                } else {
                    holder.mRestaurantName.setText(R.string.no_name_found);
                }
                if (mRestaurant.getVicinity() != null) {
                    holder.mRestaurantAddress.setText(mRestaurant.getVicinity());
                } else {
                    holder.mRestaurantAddress.setText(R.string.unknown);
                }
                if (rating <= 1) {
                    holder.mRestaurantRank1.setColorFilter(R.color.white);
                    holder.mRestaurantRank2.setColorFilter(R.color.white);
                    holder.mRestaurantRank3.getDisplay();
                } else if (rating <= 2 && rating > 1) {
                    holder.mRestaurantRank1.setColorFilter(R.color.white);
                    holder.mRestaurantRank2.getDisplay();
                    holder.mRestaurantRank3.getDisplay();
                } else {
                    holder.mRestaurantRank1.getDisplay();
                    holder.mRestaurantRank2.getDisplay();
                    holder.mRestaurantRank3.getDisplay();
                }
                holder.mFragmentRestaurantBinding.getRoot().setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                    intent.putExtra("placeId", placeId);
                    v.getContext().startActivity(intent);
                });
            }

            private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
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

        @NotNull
        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}