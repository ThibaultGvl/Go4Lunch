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
import com.example.go4lunch.view.activity.MainActivity;
import com.example.go4lunch.viewmodel.users.UserInjection;
import com.example.go4lunch.viewmodel.users.UserViewModel;
import com.example.go4lunch.viewmodel.users.UserViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.Objects;

public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        return (new SettingsFragment());
    }

    private FragmentSettingsBinding mFragmentSettingsBinding;

    private UserViewModel mUserViewModel;

    private AutoCompleteTextView mLanguage;

    private SwitchCompat mNotificationsAlert;

    private Button mSuppressButton;

    private Button mValidationButton;

    private String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    private String mLanguageChoose;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);
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
        mLanguage = mFragmentSettingsBinding.languageActv;
        mNotificationsAlert = mFragmentSettingsBinding.notificationSwitch;
        mSuppressButton = mFragmentSettingsBinding.suppressButton;
        mValidationButton = mFragmentSettingsBinding.validationButton;
    }

    public void parametersListeners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_dropdown_item_1line, mLanguages);
        mLanguage.setAdapter(adapter);
        if (mLanguage.getText().toString().equals(String.valueOf(R.string.english))){
            mLanguageChoose = "en";
        }
        else {
            mLanguageChoose = "fr";
        }
        mSuppressButton.setOnClickListener(v -> mUserViewModel.deleteUser(currentUserId, this.getContext()));
        mValidationButton.setOnClickListener(v -> {
            if (mLanguageChoose != null) {
                setLanguage(this.requireContext(), mLanguageChoose);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("mLanguageChoose", mLanguageChoose);
                startActivity(intent);
            }
        });
    }

    private static final String[] mLanguages = new String[] {
            "English", "French"
    };

    private void setLanguage(Context context, String languageToLoad) {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.createConfigurationContext(config);
        context.getResources().updateConfiguration(config,getResources().getDisplayMetrics());
    }

    private void configureUserViewModel() {
        UserViewModelFactory mViewModelFactory = UserInjection.provideViewModelFactory();
        this.mUserViewModel = new ViewModelProvider(this, mViewModelFactory)
                .get(UserViewModel.class);
        this.mUserViewModel.initUsers(this.requireContext());
    }
}