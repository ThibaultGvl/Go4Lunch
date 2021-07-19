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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.go4lunch.BuildConfig;
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
    private final String currentUserId = FirebaseAuth.getInstance().getUid();
    private UserViewModel mUserViewModel;
    private NearbyRestaurantViewModel mRestaurantViewModel;
    private String placeId;
    private final List<User> mUsers = new ArrayList<>();
    private List<String> mRestaurantsLiked = new ArrayList<>();
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
                BuildConfig.API_KEY)
                .observe(this, this::setRestaurant);
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
            if (mRestaurant.getPhotos() != null) {
                if (mRestaurant.getPhotos().get(0) != null) {
                    String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth" +
                            "=400&photoreference=" + mRestaurant.getPhotos().get(0)
                            .getPhotoReference() + "&key=" + BuildConfig.API_KEY;
                    Glide.with(mImage).load(photoUrl).into(mImage);
                }
            }
            else {
                Glide.with(mImage).load(R.drawable.ic_logo_go4lunch).into(mImage);
            }
            if (mRestaurant.getRating() != null) {
                double rating = ((mRestaurant.getRating() / 5) * 3);
                mRatingBar.setRating((float) rating);
            }
            mPhoneButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phone));
                startActivity(intent);
            });
            mWebButton.setOnClickListener(v -> {
                if (mRestaurant.getWebsite() != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(mRestaurant.getWebsite()));
                    startActivity(intent);
                }
            });
            mFab.setOnClickListener(v -> updateRestaurantChoose(mRestaurant));
            mLikeButton.setOnClickListener(v -> setRestaurantsLiked(mRestaurant.getPlaceId()));
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
        mUserViewModel.updateUserRestaurantAddress(currentUserId, mRestaurant.getFormattedAddress(),
                this);
    }

    private void configureUsers() {
        mUserViewModel.getUserByPlaceId(placeId, this)
                .observe(this, this::setUsersList);
    }

    private void setRestaurantsLiked(String restaurantLiked) {
        mUserViewModel.getRestaurantsLiked().observe(this, restaurantsLiked ->
                configureRestaurantsLiked(restaurantsLiked, restaurantLiked));
    }

    private void configureRestaurantsLiked(List<String> restaurantsLiked, String restaurantLike) {
        if (restaurantsLiked != null) {
            mRestaurantsLiked.clear();
            this.mRestaurantsLiked.addAll(restaurantsLiked);
        }
        else {
            this.mRestaurantsLiked = new ArrayList<>();
        }
        mUserViewModel.updateRestaurantsLiked(currentUserId, mRestaurantsLiked, restaurantLike,
                getBaseContext());
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
        this.mUserViewModel.initRestaurantsLiked(currentUserId, this);
    }

    private void configureNearbyRestaurantViewModel() {
        NearbyViewModelFactory nearbyViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestaurantViewModel.class);
    }
}