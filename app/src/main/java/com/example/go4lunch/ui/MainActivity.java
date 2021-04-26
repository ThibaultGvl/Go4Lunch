package com.example.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.model.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment mapsFragment;
    private Fragment restaurantFragment;
    private Fragment userFragment;
    private BottomNavigationView bottomNavigationView;

    private static final int MAPS_FRAGMENT = 0;
    private static final int RESTAURANT_FRAGMENT = 1;
    private static final int USER_FRAGMENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.configureBottomView();
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

    private  void showUserFragment() {
        if (this.userFragment == null) this.userFragment = UserFragment.newInstance(1);
        this.startTransactionFragment(userFragment);
    }

    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, fragment).commit();
        }
    }

    private void configureBottomView() {
        this.bottomNavigationView = binding.navBottomBar;
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationBottomItemSelected);
    }
}
