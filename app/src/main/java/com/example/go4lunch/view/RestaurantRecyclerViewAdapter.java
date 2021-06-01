package com.example.go4lunch.view;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentRestaurantBinding;
import com.example.go4lunch.model.restaurant.ResultRestaurant;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        String ratingString = Double.toString(rating);
        if (mRestaurant.getPhotos() != null) {
            Glide.with(holder.mRestaurantImage.getContext()).load(mRestaurant.getPhotos()).into(holder.mRestaurantImage);
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
            holder.mRestaurantAddress.setText(mRestaurant.getVicinity());
        }
        else {
            holder.mRestaurantAddress.setText(R.string.unknown);
        }
        holder.mRestaurantRank.setText(ratingString);
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
        private final TextView mRestaurantRank;
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