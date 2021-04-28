package com.example.go4lunch.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.go4lunch.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}