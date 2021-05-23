package com.example.go4lunch.restaurant;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.ViewHolder> {

    private final List<Restaurant> restaurants;

    public RestaurantRecyclerViewAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mRestaurant = restaurants.get(position);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Restaurant mRestaurant;

        public ViewHolder(View view) {
            super(view);
            mView = view;
        }

        @Override
        public String toString() {
            return super.toString() ;
        }
    }
}