package com.example.go4lunch.view.fragments.restaurant;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentRestaurantBinding;
import com.example.go4lunch.model.restaurant.nearby.ResultNearbyRestaurant;
import com.example.go4lunch.view.activity.details.DetailsActivity;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        return new ViewHolder(fragmentRestaurantBinding);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, int position) {
        ResultNearbyRestaurant mRestaurant = restaurants.get(position);
        double rating = ((mRestaurant.getRating()/5)*3);
        String placeId = mRestaurant.getPlaceId();
        LatLng restaurantLatLng = new LatLng(mRestaurant.getGeometry().getLocation().getLat(),
                mRestaurant.getGeometry().getLocation().getLng());
        /*String restaurantPhoto = mRestaurant.getPhotos().get(0).getPhotoReference();
        Glide.with(holder.mRestaurantImage.getContext())
                    .load(restaurantPhoto).apply(RequestOptions.overrideOf(1000,1000))
                    .error(R.drawable.ic_logo_go4lunch).into(holder.mRestaurantImage);*/
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
        if (rating <= 1) {
                holder.mRestaurantRank1.setColorFilter(R.color.white);
                holder.mRestaurantRank2.setColorFilter(R.color.white);
                holder.mRestaurantRank3.getDisplay();
        }
        else if (rating <= 2 && rating > 1) {
                holder.mRestaurantRank1.setColorFilter(R.color.white);
                holder.mRestaurantRank2.getDisplay();
                holder.mRestaurantRank3.getDisplay();
        }
        else {
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
        private final ImageView mRestaurantWorkmates;
        private final ImageView mRestaurantRank1;
        private final ImageView mRestaurantRank2;
        private final ImageView mRestaurantRank3;
        private final ImageView mRestaurantImage;

        public ViewHolder(FragmentRestaurantBinding fragmentRestaurantBinding) {
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
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}