package com.example.go4lunch.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityDetailsBinding;
import com.example.go4lunch.model.details.RestaurantDetails;
import com.example.go4lunch.model.details.Result;
import com.example.go4lunch.places.NearbyInjection;
import com.example.go4lunch.places.NearbyRestaurantViewModel;
import com.example.go4lunch.places.NearbyViewModelFactory;
import com.example.go4lunch.users.UserInjection;
import com.example.go4lunch.users.UserViewModel;
import com.example.go4lunch.users.UserViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding mActivityBinding;
    private TextView mName;
    private TextView mAddress;
    private ImageView mImage;
    private RatingBar mRatingBar;
    private ImageButton mPhoneButton;
    private ImageButton mWebButton;
    private ImageButton mLikeButton;
    private FloatingActionButton mFab;
    private UserViewModel mUserViewModel;
    private NearbyRestaurantViewModel mRestaurantViewModel;
    private String placeId;
    private Result mRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityBinding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = mActivityBinding.getRoot();
        configureUI();
        configureUserViewModel();
        configureNearbyRestaurantViewModel();
        Intent getIntent = getIntent();
        placeId = getIntent.getStringExtra("placeId");
        mRestaurantViewModel.getRestaurantDetails(placeId,
                "AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4").observe(this, this::setRestaurant);
        updateWithRestaurant();
        setContentView(view);
    }

    private void setRestaurant(RestaurantDetails restaurant) {
        if(restaurant != null) {
            mRestaurant = restaurant.getResult();
        }
        else {
            Toast.makeText(this, (R.string.no_details), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateWithRestaurant() {
        String phone = "tel:" + mRestaurant.getInternationalPhoneNumber();
//        double rating = ((mRestaurant.getRating() / 5) * 3);
        String name = mRestaurant.getName();
        if (mRestaurant.getName() != null) {
            mName.setText(name);
        }
        else {
            mName.setText(R.string.info_no_username_found);
        }
        mAddress.setText(mRestaurant.getAdrAddress());
/*        if (restaurant.getPhotos().get(0) != null) {
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + mRestaurant.getPhotos().get(0).getPhotoReference() + "&key=AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4";
            Glide.with(mImage).load(photoUrl).into(mImage);
        }*/
        mPhoneButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phone));
            startActivity(intent);
        });
        mLikeButton.setOnClickListener(v -> { });
        mWebButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRestaurant.getUrl()));
            startActivity(intent);
        });
        mFab.setOnClickListener(v -> updateRestaurantChoose());
        //mRatingBar.setRating((float) rating);
    }

    private void configureUI() {
        mName = mActivityBinding.restaurantNameDetails;
        mAddress = mActivityBinding.restaurantNameDetails;
        mImage = mActivityBinding.restaurantImageView;
        mPhoneButton = mActivityBinding.phoneButton;
        mLikeButton = mActivityBinding.likeButton;
        mWebButton = mActivityBinding.websiteButton;
    }

    private void updateRestaurantChoose() {
        mUserViewModel.updateUserRestaurant(FirebaseAuth.getInstance().getUid(), placeId, this);
        mUserViewModel.updateUserRestaurantName(FirebaseAuth.getInstance().getUid(), mRestaurant.getName(), this);
        mUserViewModel.updateUserRestaurantAddress(FirebaseAuth.getInstance().getUid(), mRestaurant.getAdrAddress(), this);
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