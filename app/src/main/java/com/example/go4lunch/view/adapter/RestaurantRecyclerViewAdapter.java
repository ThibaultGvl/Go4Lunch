package com.example.go4lunch.view.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentRestaurantBinding;
import com.example.go4lunch.model.restaurant.ResultRestaurant;
import com.example.go4lunch.view.activity.DetailsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.ViewHolder> {

    private final List<ResultRestaurant> restaurants;

    public RestaurantRecyclerViewAdapter(List<ResultRestaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        com.example.go4lunch.databinding.FragmentRestaurantBinding fragmentRestaurantBinding = (FragmentRestaurantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return new ViewHolder(fragmentRestaurantBinding);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        ResultRestaurant mRestaurant = restaurants.get(position);
        double rating = ((mRestaurant.getRating()/5)*3);
        String placeId = mRestaurant.getPlaceId();
        if (mRestaurant.getPhotos() != null) {
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + mRestaurant.getPhotos().get(0).getPhotoReference() + "&key=AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4";
            Glide.with(holder.mRestaurantImage.getContext()).load(photoUrl).into(holder.mRestaurantImage);
        }
        else {
            holder.mRestaurantImage.setColorFilter(R.color.black);
        }
        if (mRestaurant.getName() != null) {
            holder.mRestaurantName.setText(mRestaurant.getName());
        }
        else {
            holder.mRestaurantName.setText(R.string.no_name_found);
        }
        if (mRestaurant.getVicinity() != null) {
            String address = mRestaurant.getVicinity().split(",")[0];
            holder.mRestaurantAddress.setText(address);
        }
        else {
            holder.mRestaurantAddress.setText(R.string.unknown);
        }
        if (mRestaurant.getOpeningHours() != null) {
            if (mRestaurant.getOpeningHours().getOpenNow()) {
                holder.mRestaurantSchedules.setText(R.string.open);
            } else {
                holder.mRestaurantSchedules.setText(R.string.close);
            }
        }
        else {
            holder.mRestaurantSchedules.setText(R.string.unknown);
        }
        holder.mRestaurantRank.setRating((float) rating);
        holder.mFragmentRestaurantBinding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            intent.putExtra("placeId", placeId);
            startActivity(v.getContext(), intent, null);
        });
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
        private final RatingBar mRestaurantRank;
        private final ImageView mRestaurantImage;

        public ViewHolder(FragmentRestaurantBinding fragmentRestaurantBinding) {
            super(fragmentRestaurantBinding.getRoot());
            mFragmentRestaurantBinding = fragmentRestaurantBinding;
            mRestaurantName = fragmentRestaurantBinding.restaurantName;
            mRestaurantAddress = fragmentRestaurantBinding.restaurantAddress;
            mRestaurantSchedules = fragmentRestaurantBinding.restaurantSchedules;
            mRestaurantDistance = fragmentRestaurantBinding.restaurantDistance;
            mRestaurantWorkmates = fragmentRestaurantBinding.restaurantWorkmate;
            mRestaurantRank = fragmentRestaurantBinding.restaurantRank;
            mRestaurantImage = fragmentRestaurantBinding.restaurantImage;
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}