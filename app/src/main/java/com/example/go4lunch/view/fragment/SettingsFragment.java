package com.example.go4lunch.view.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.go4lunch.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SettingsFragment newInstance() {
        return (new SettingsFragment());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}