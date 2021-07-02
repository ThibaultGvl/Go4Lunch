package com.example.go4lunch.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityDetailsBinding;
import com.example.go4lunch.model.User;
import com.example.go4lunch.model.details.RestaurantDetails;
import com.example.go4lunch.model.details.Result;
import com.example.go4lunch.view.adapter.DetailsRecyclerViewAdapter;
import com.example.go4lunch.viewmodel.places.NearbyInjection;
import com.example.go4lunch.viewmodel.places.NearbyRestaurantViewModel;
import com.example.go4lunch.viewmodel.places.NearbyViewModelFactory;
import com.example.go4lunch.viewmodel.users.UserInjection;
import com.example.go4lunch.viewmodel.users.UserViewModel;
import com.example.go4lunch.viewmodel.users.UserViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding mActivityBinding;
    private TextView mName;
    private TextView mAddress;
    private ImageView mImage;
    private RatingBar mRatingBar;
    private ImageButton mPhoneButton;
    private ImageButton mWebButton;
    private ImageButton mLikeButton;
    private ImageButton mReturnButton;
    private FloatingActionButton mFab;
    private UserViewModel mUserViewModel;
    private NearbyRestaurantViewModel mRestaurantViewModel;
    private final String currentUserId = FirebaseAuth.getInstance().getUid();
    private String placeId;
    private final List<User> mUsers = new ArrayList<>();
    private final DetailsRecyclerViewAdapter mAdapter = new DetailsRecyclerViewAdapter(mUsers);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = mActivityBinding.getRoot();
        Intent getIntent = getIntent();
        placeId = getIntent.getStringExtra("placeId");
        configureUI();
        configureUserViewModel();
        configureNearbyRestaurantViewModel();
        mRestaurantViewModel.getRestaurantDetails(placeId,
                "AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4").observe(this, this::setRestaurant);
        configureUsers();
        RecyclerView recyclerView = mActivityBinding.userRvDetails;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        setContentView(view);
    }

    private void setRestaurant(RestaurantDetails restaurant) {
            Result mRestaurant = restaurant.getResult();
            updateWithRestaurant(mRestaurant);
    }

    private void updateWithRestaurant(Result mRestaurant) {
        mReturnButton.setOnClickListener(v -> onBackPressed());
        if (mRestaurant !=  null) {
            String phone = "tel:" + mRestaurant.getInternationalPhoneNumber();
            String name = mRestaurant.getName();
            if (mRestaurant.getName() != null) {
                mName.setText(name);
            } else {
                mName.setText(R.string.info_no_username_found);
            }
            mAddress.setText(mRestaurant.getVicinity());
            if (mRestaurant.getPhotos().get(0) != null) {
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + mRestaurant.getPhotos().get(0).getPhotoReference() + "&key=AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4";
            Glide.with(mImage).load(photoUrl).into(mImage);
            }
            if (mRestaurant.getRating() != null) {
                double rating = ((mRestaurant.getRating() / 5) * 3);
                mRatingBar.setRating((float) rating);
            }
            mPhoneButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phone));
                startActivity(intent);
            });
            mLikeButton.setOnClickListener(v -> { });
            mWebButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRestaurant.getWebsite()));
                startActivity(intent);
            });
            mFab.setOnClickListener(v -> updateRestaurantChoose(mRestaurant));
        }
    }

    private void configureUI() {
        mName = mActivityBinding.restaurantNameDetails;
        mAddress = mActivityBinding.restaurantType;
        mImage = mActivityBinding.restaurantImageView;
        mPhoneButton = mActivityBinding.phoneButton;
        mLikeButton = mActivityBinding.likeButton;
        mWebButton = mActivityBinding.websiteButton;
        mReturnButton = mActivityBinding.arrowBack;
        mFab = mActivityBinding.fab;
        mRatingBar = mActivityBinding.ratingDetail;
    }

    private void updateRestaurantChoose(Result mRestaurant) {
        mUserViewModel.updateUserRestaurant(currentUserId, placeId, this);
        mUserViewModel.updateUserRestaurantName(currentUserId, mRestaurant.getName(), this);
        mUserViewModel.updateUserRestaurantAddress(currentUserId, mRestaurant.getAdrAddress(),
                this);
    }

    private void configureUsers() {
        mUserViewModel.getUserByPlaceId(placeId, this).observe(this, this::setUsersList);
    }

    private void setUsersList(List<User> users) {
        mUsers.clear();
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    private void configureUserViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider(this, mViewModelFactory)
                .get(UserViewModel.class);
        this.mUserViewModel.initUsers(this);
    }

    private void configureNearbyRestaurantViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestaurantViewModel.class);
    }
}