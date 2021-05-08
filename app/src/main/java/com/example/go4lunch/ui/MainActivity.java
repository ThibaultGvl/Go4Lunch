package com.example.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.databinding.ActivityNavHeaderBinding;
import com.example.go4lunch.model.Restaurant;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.User;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private ActivityNavHeaderBinding mNavHeaderBinding;
    private Fragment mapsFragment;
    private Fragment restaurantFragment;
    private Fragment userFragment;
    private Fragment settingsFragment;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView currentUserName;
    private TextView currentUserEmail;
    private ImageView currentUserImage;
    private User user;

    private static final int MAPS_FRAGMENT = 0;
    private static final int RESTAURANT_FRAGMENT = 1;
    private static final int USER_FRAGMENT = 2;
    private static final int SETTINGS_FRAGMENT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.showFragment(MAPS_FRAGMENT);
        this.configureToolBar();
        this.configureBottomView();
        this.configureNavigationView();
        this.configureDrawerLayout();
        this.updateUIWhenCreating();
        setContentView(view);
    }

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
        this.startTransactionFragment(this.mapsFragment);
    }

    private void showRestaurantFragment() {
        if (this.restaurantFragment == null) this.restaurantFragment = RestaurantFragment.newInstance(1);
        this.startTransactionFragment(restaurantFragment);
    }

    private void showUserFragment() {
        if (this.userFragment == null) this.userFragment = UserFragment.newInstance(1);
        this.startTransactionFragment(userFragment);
    }

    private void showSettingsFragment() {
        if (this.settingsFragment == null) this.settingsFragment = SettingsFragment.newInstance();
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
        this.bottomNavigationView = binding.navBottomBar;
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationBottomItemSelected);
    }

    private void configureDrawerLayout() {
        this.drawerLayout = binding.drawer;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
    }

    private void configureNavigationView() {
        this.navigationView = binding.navView;
        this.navigationView.setNavigationItemSelectedListener(this);
        this.mNavHeaderBinding = ActivityNavHeaderBinding.inflate(LayoutInflater.from(navigationView.getContext()));
        this.navigationView.addHeaderView(mNavHeaderBinding.getRoot());
        this.currentUserName = mNavHeaderBinding.nameProfile;
        this.currentUserEmail = mNavHeaderBinding.emailProfile;
        this.currentUserImage = mNavHeaderBinding.imageProfile;
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this);
        Intent intent = new Intent(this, ConnexionActivity.class);
        startActivity(intent);
    }

    private void configureHeadNavDrawer() {
        this.mNavHeaderBinding = ActivityNavHeaderBinding.inflate(getLayoutInflater());
        this.currentUserName = mNavHeaderBinding.nameProfile;
        this.currentUserEmail = mNavHeaderBinding.emailProfile;
        this.currentUserImage = mNavHeaderBinding.imageProfile;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.your_lunch :
                
            case R.id.settings :
                showSettingsFragment();
            case R.id.logout :
                signOut();
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    private void updateUIWhenCreating(){

        if (isCurrentUserLogged()){

            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(currentUserImage);
            }

            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();

            currentUserName.setText(username);
            currentUserEmail.setText(email);
        }
    }
}
