package com.example.go4lunch.ui;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.databinding.ActivityNavHeaderBinding;
import com.example.go4lunch.model.Restaurant;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.User;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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
    }

    private void configureNavigationView() {
        this.navigationView = binding.navView;
        this.navigationView.setNavigationItemSelectedListener(this);
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this);
        Intent intent = new Intent(this, ConnexionActivity.class);
        startActivity(intent);
    }

    private void configureNavDrawer() {
        TextView currentUserName = mNavHeaderBinding.nameProfile;
        TextView currentUserEmail = mNavHeaderBinding.emailProfile;
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
}
