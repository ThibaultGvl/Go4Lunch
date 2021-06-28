package com.example.go4lunch.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityPlaceAutoCompleteBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import static com.facebook.referrals.ReferralFragment.TAG;

public class PlaceAutoCompleteActivity extends AppCompatActivity {

    ActivityPlaceAutoCompleteBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPlaceAutoCompleteBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        Places.initialize(this, "AIzaSyA8fqLfJRcp8jVraX7TatTFkykuTHJUzt4");
        PlacesClient placesClient = Places.createClient(this);
        setContentView(view);
    }

}
