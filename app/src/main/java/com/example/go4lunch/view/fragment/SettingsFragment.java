package com.example.go4lunch.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentSettingsBinding;
import com.example.go4lunch.view.activity.ConnexionActivity;
import com.example.go4lunch.viewmodel.users.UserInjection;
import com.example.go4lunch.viewmodel.users.UserViewModel;
import com.example.go4lunch.viewmodel.users.UserViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
            mUserViewModel.deleteUser(currentUserId, this.getContext());
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this.getActivity(), ConnexionActivity.class);
            startActivity(intent);
        });
    }

    private void configureUserViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider(this, mViewModelFactory)
                .get(UserViewModel.class);
        this.mUserViewModel.initUsers(this.requireContext());
    }
}