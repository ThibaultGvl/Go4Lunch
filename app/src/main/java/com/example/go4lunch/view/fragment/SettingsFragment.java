package com.example.go4lunch.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentSettingsBinding;
import com.example.go4lunch.view.activity.ConnexionActivity;
import com.example.go4lunch.view.activity.MainActivity;
import com.example.go4lunch.viewmodel.users.UserInjection;
import com.example.go4lunch.viewmodel.users.UserViewModel;
import com.example.go4lunch.viewmodel.users.UserViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import java.util.Objects;

public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        return (new SettingsFragment());
    }

    private FragmentSettingsBinding mFragmentSettingsBinding;

    private UserViewModel mUserViewModel;

    private Button mSuppressButton;

    private final FirebaseUser currentUser = Objects.requireNonNull(FirebaseAuth.getInstance()
            .getCurrentUser());



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container,
                false);
        createUI();
        parametersListeners();
        return mFragmentSettingsBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureUserViewModel();
    }

    private void createUI() {
        mSuppressButton = mFragmentSettingsBinding.suppressButton;
    }

    public void parametersListeners() {
        String currentUserId = currentUser.getUid();
        mSuppressButton.setOnClickListener(v -> {
            currentUser.delete();
            mUserViewModel.deleteUser(currentUserId, this.getContext());
            Intent intent = new Intent(this.getActivity(), ConnexionActivity.class);
            startActivity(intent);
            FirebaseAuth.getInstance().signOut();
        });
    }

    private void configureUserViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider(this, mViewModelFactory)
                .get(UserViewModel.class);
        this.mUserViewModel.initUsers(this.requireContext());
    }
}