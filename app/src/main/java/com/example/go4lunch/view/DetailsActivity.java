package com.example.go4lunch.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityDetailsBinding;
import com.example.go4lunch.model.User;
import com.example.go4lunch.model.details.RestaurantDetails;
import com.example.go4lunch.model.details.Result;
import com.example.go4lunch.places.NearbyInjection;
import com.example.go4lunch.places.NearbyRestaurantViewModel;
import com.example.go4lunch.places.NearbyViewModelFactory;
import com.example.go4lunch.users.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding mActivityDetailsBinding;
    private NearbyRestaurantViewModel mNearbyRestaurantViewModel;
    private UserViewModel mUserViewModel;
    private final int apiKey = R.string.google_maps_key;
    private Result mResultRestaurant = new Result();
    private User currentUser;
    private TextView mRestaurantName;
    private TextView mRestaurantInfo;
    private ImageView mRestaurantImage;
    private ImageButton mPhone;
    private ImageButton mLike;
    private ImageButton mWeb;
    private FloatingActionButton mRestaurantToEat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDetailsBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = mActivityDetailsBinding.getRoot();
        Intent getIntent = getIntent();
        String placeId = getIntent.getStringExtra("placeId");
        configureView();
        configureNearbyRestaurantViewModel();
        String apiKeyString = String.valueOf(apiKey);
        mNearbyRestaurantViewModel.getRestaurantDetails(placeId, "AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4").observe(this, this::setRestaurant);
        if (mResultRestaurant.getName() != null) {
            mRestaurantName.setText(mResultRestaurant.getName());
        }
        else {
            mRestaurantName.setText(R.string.unknown);
        }
        /*mRestaurantInfo.setText(mResultRestaurant.getVicinity());
        Glide.with(this).load(mResultRestaurant.getPhotos().get(0)).into(mRestaurantImage);
        mWeb.setOnClickListener(v -> mResultRestaurant.getWebsite());
        mLike.setOnClickListener(v -> addRestaurantToFavorites(placeId, this));
        mPhone.setOnClickListener(v -> mResultRestaurant.getInternationalPhoneNumber());
        mRestaurantToEat.setOnClickListener(v -> chooseRestaurant(placeId, this));*/
        setContentView(view);
    }

    private void configureView() {
        mRestaurantName = mActivityDetailsBinding.restaurantNameDetails;
        mRestaurantInfo = mActivityDetailsBinding.restaurantType;
        mRestaurantImage = mActivityDetailsBinding.restaurantImageView;
        mPhone = mActivityDetailsBinding.phoneButton;
        mLike = mActivityDetailsBinding.likeButton;
        mWeb = mActivityDetailsBinding.websiteButton;
        mRestaurantToEat = mActivityDetailsBinding.fab;
    }

    private void setRestaurant(RestaurantDetails restaurant) {
        mResultRestaurant = restaurant.getResult();
    }

    private void addRestaurantToFavorites(String placeId, Context context) {
        mUserViewModel.updateRestaurantsLiked(placeId, context);
    }

    private void chooseRestaurant(String placeId, Context context) {
        mUserViewModel.updateUserRestaurant(placeId, context);
    }

    private void configureNearbyRestaurantViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mNearbyRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestaurantViewModel.class);
    }
}