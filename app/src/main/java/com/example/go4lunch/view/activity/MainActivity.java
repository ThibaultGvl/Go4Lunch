package com.example.go4lunch.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.databinding.ActivityNavHeaderBinding;
import com.example.go4lunch.location.LocationInjection;
import com.example.go4lunch.location.LocationViewModel;
import com.example.go4lunch.location.LocationViewModelFactory;
import com.example.go4lunch.model.User;
import com.example.go4lunch.places.NearbyInjection;
import com.example.go4lunch.places.NearbyRestaurantViewModel;
import com.example.go4lunch.places.NearbyViewModelFactory;
import com.example.go4lunch.users.UserInjection;
import com.example.go4lunch.users.UserViewModelFactory;
import com.example.go4lunch.users.UserViewModel;
import com.example.go4lunch.view.activity.details.DetailsActivity;
import com.example.go4lunch.view.fragments.MapsFragment;
import com.example.go4lunch.view.fragments.SettingsFragment;
import com.example.go4lunch.view.fragments.restaurant.RestaurantFragment;
import com.example.go4lunch.view.fragments.user.UserFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private Fragment mapsFragment;
    private Fragment restaurantFragment;
    private Fragment userFragment;
    private Fragment settingsFragment;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView currentUserName;
    private TextView currentUserEmail;
    private ImageView currentUserImage;
    private UserViewModel mUserViewModel;
    private LocationViewModel mLocationViewModel;
    private NearbyRestaurantViewModel mNearbyRestaurantViewModel;
    private User currentUser = new User();

    private static final int MAPS_FRAGMENT = 0;
    private static final int RESTAURANT_FRAGMENT = 1;
    private static final int USER_FRAGMENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.configureUserViewModel();
        this.configureLocationViewModel();
        this.configureNearbyRestaurantViewModel();
        this.showFragment(MAPS_FRAGMENT);
        this.configureToolBar();
        this.configureBottomView();
        this.configureNavigationView();
        this.configureDrawerLayout();
        this.updateUIWhenCreating();
        setContentView(view);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationBottomItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.maps_view :
                this.showFragment(MAPS_FRAGMENT);
                break;
            case R.id.list_view :
                this.showFragment(RESTAURANT_FRAGMENT);
                break;
            case R.id.workmate :
                this.showFragment(USER_FRAGMENT);
                break;
        }
        return true;
    }

    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case MAPS_FRAGMENT :
                this.showMapsFragment();
                break;
            case RESTAURANT_FRAGMENT :
                this.showRestaurantFragment();
                break;
            case USER_FRAGMENT :
                this.showUserFragment();
                break;
        }
    }

    private void showMapsFragment() {
        if (this.mapsFragment == null) this.mapsFragment = MapsFragment.newInstance();
        binding.bannerTitle.setText(R.string.default_banner);
        this.startTransactionFragment(this.mapsFragment);
    }

    private void showRestaurantFragment() {
        if (this.restaurantFragment == null) this.restaurantFragment =
                RestaurantFragment.newInstance(1);
        binding.bannerTitle.setText(R.string.default_banner);
        this.startTransactionFragment(restaurantFragment);
    }

    private void showUserFragment() {
        if (this.userFragment == null) this.userFragment = UserFragment.newInstance(1);
        binding.bannerTitle.setText(R.string.user_fragment_banner);
        this.startTransactionFragment(userFragment);
    }

    private void showSettingsFragment() {
        if (this.settingsFragment == null) this.settingsFragment = SettingsFragment.newInstance();
        binding.bannerTitle.setText(R.string.preferences);
        this.startTransactionFragment(settingsFragment);
    }

    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, fragment).commit();
        }
    }

    private void configureToolBar() {
        this.toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
    }

    private void configureBottomView() {
        BottomNavigationView bottomNavigationView = binding.navBottomBar;
        bottomNavigationView.setOnNavigationItemSelectedListener
                (this::onNavigationBottomItemSelected);
    }

    private void configureDrawerLayout() {
        this.drawerLayout = binding.drawer;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
    }

    private void configureNavigationView() {
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        com.example.go4lunch.databinding.ActivityNavHeaderBinding navHeaderBinding =
                ActivityNavHeaderBinding.inflate(LayoutInflater.from(navigationView.getContext()));
        navigationView.addHeaderView(navHeaderBinding.getRoot());
        this.currentUserName = navHeaderBinding.nameProfile;
        this.currentUserEmail = navHeaderBinding.emailProfile;
        this.currentUserImage = navHeaderBinding.imageProfile;
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this);
        Intent intent = new Intent(this, ConnexionActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.your_lunch :
                getYourLunch();
                break;
            case R.id.settings :
                showSettingsFragment();
                break;
            case R.id.logout :
                signOut();
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    private void updateUIWhenCreating() {

        mUserViewModel.getUser(getCurrentUser().getUid(), this).observe(this, this::getUser);

        if (isCurrentUserLogged()) {

            if (Objects.requireNonNull(this.getCurrentUser()).getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(currentUserImage);
            }

            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ?
                    getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ?
                    getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();
            currentUserName.setText(username);
            currentUserEmail.setText(email);

            if (currentUser.getUid() == null) {
                this.mUserViewModel.createCurrentUser(this);
            }
        }
    }

    private void getYourLunch() {
        mUserViewModel.getUser(Objects.requireNonNull(getCurrentUser()).getUid(), this).observe(this, this::getUser);
        if (currentUser.getRestaurant() != null) {
            String placeId = currentUser.getRestaurant();
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("placeId", placeId);
        }
        else {
            Toast.makeText(getApplicationContext(), "You don't have choose a restaurant !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUser(User user) {
        this.currentUser = user;
    }

    private void configureLocationViewModel () {
        LocationViewModelFactory locationViewModelFactory = LocationInjection.provideMapsViewModelFactory();
        mLocationViewModel = new ViewModelProvider(this, locationViewModelFactory)
                .get(LocationViewModel.class);
    }

    private void configureUserViewModel () {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider(this, mViewModelFactory)
                .get(UserViewModel.class);
        this.mUserViewModel.initUsers(this);
    }

    private void configureNearbyRestaurantViewModel () {
        NearbyViewModelFactory nearbyViewModelFactory = NearbyInjection.provideRestaurantViewModel();
        mNearbyRestaurantViewModel = new ViewModelProvider(this, nearbyViewModelFactory)
                .get(NearbyRestaurantViewModel.class);
    }
}
