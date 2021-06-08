package com.example.go4lunch.view.activity.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityDetailsBinding;
import com.example.go4lunch.model.User;
import com.example.go4lunch.model.restaurant.details.PhotoDetails;
import com.example.go4lunch.model.restaurant.details.RestaurantDetails;
import com.example.go4lunch.model.restaurant.details.ResultDetails;
import com.example.go4lunch.model.restaurant.nearby.Photo;
import com.example.go4lunch.places.NearbyInjection;
import com.example.go4lunch.places.NearbyRestaurantViewModel;
import com.example.go4lunch.places.NearbyViewModelFactory;
import com.example.go4lunch.users.UserInjection;
import com.example.go4lunch.users.UserViewModel;
import com.example.go4lunch.users.UserViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding mActivityDetailsBinding;
    private NearbyRestaurantViewModel mNearbyRestaurantViewModel;
    private UserViewModel mUserViewModel;
    private final int apiKey = R.string.google_maps_key;
    private ResultDetails mResultRestaurant = new ResultDetails();
    private TextView mRestaurantName;
    private TextView mRestaurantInfo;
    private ImageView mRestaurantImage;
    private ImageButton mPhone;
    private ImageButton mLike;
    private ImageButton mWeb;
    private FloatingActionButton mRestaurantToEat;
    private String placeId;
    private final List<User> users = new ArrayList<>();
    private final DetailsRecyclerView mDetailsRecyclerView = new DetailsRecyclerView(users);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDetailsBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = mActivityDetailsBinding.getRoot();
        Intent getIntent = getIntent();
        placeId = getIntent.getStringExtra("placeId");
        configureView();
        configureNearbyRestaurantViewModel();
        configureUserViewModel();
        configureUsers(this);
        mNearbyRestaurantViewModel.getRestaurantDetails(placeId, "AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4").observe(this, this::setRestaurant);
        RecyclerView recyclerView = mActivityDetailsBinding.userRvDetails;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mDetailsRecyclerView);
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
        if (mResultRestaurant.getPhotos() != null) {
            PhotoDetails photo = mResultRestaurant.getPhotos().get(0);
            if (photo != null && !photo.getPhotoReference().isEmpty() && !photo.getPhotoReference().equals("null")) {
                String photoString = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" + photo.getPhotoReference() + "&sensor=false&key=" + "AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4";
                Glide.with(mRestaurantImage.getContext())
                        .load(photoString)
                        .apply(RequestOptions.overrideOf(1000, 1000))
                        .error(R.drawable.ic_logo_go4lunch)
                        .into(mRestaurantImage);
            }
        }
        if (mResultRestaurant.getName() != null) {
            mRestaurantName.setText(mResultRestaurant.getName());
        }
        else {
            mRestaurantName.setText(R.string.unknown);
        }
        mRestaurantInfo.setText(mResultRestaurant.getVicinity());
        mWeb.setOnClickListener(v -> {
            if (mResultRestaurant.getWebsite() != null) {
                Uri uri = Uri.parse(mResultRestaurant.getWebsite());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            else {
                Toast.makeText(v.getContext(), R.string.no_website, Toast.LENGTH_SHORT).show();
            }
        });
        mPhone.setOnClickListener(v -> {
            if (mResultRestaurant.getInternationalPhoneNumber() != null) {
                String number = mResultRestaurant.getInternationalPhoneNumber();
                String phoneNumber = "tel:" + number;
                Uri uri = Uri.parse(phoneNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
            else {
                Toast.makeText(v.getContext(), R.string.no_number, Toast.LENGTH_SHORT).show();
            }
        });
        mRestaurantToEat.setOnClickListener(v -> chooseRestaurant(placeId, mResultRestaurant.getName(), mResultRestaurant.getAdrAddress(), this));
        mLike.setOnClickListener(v -> updateRestaurantsFavorites(placeId, this));
    }

    private void configureUsers(Context context) {
        mUserViewModel.getUsersByPlaceId(context, placeId);
    }

    private void updateRestaurantsFavorites(String placeId, Context context) {
        mUserViewModel.updateRestaurantsLiked(placeId, context);
    }

    private void chooseRestaurant(String placeId, String restaurantName, String restaurantAddress,Context context) {
        mUserViewModel.updateUserRestaurant(placeId, context);
        mUserViewModel.updateUserRestaurantName(restaurantName, context);
        mUserViewModel.updateUserRestaurantAddress(restaurantAddress, context);
    }

    private void configureNearbyRestaurantViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mNearbyRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestaurantViewModel.class);
    }

    private void configureUserViewModel() {
        UserViewModelFactory userViewModelFactory = UserInjection.provideViewModelFactory();
        mUserViewModel = new ViewModelProvider(this, userViewModelFactory)
                .get(UserViewModel.class);
    }
}