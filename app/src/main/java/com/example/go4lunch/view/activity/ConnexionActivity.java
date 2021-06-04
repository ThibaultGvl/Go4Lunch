package com.example.go4lunch.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.go4lunch.databinding.ActivityConnexionBinding;
import com.facebook.CallbackManager;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class ConnexionActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    private CallbackManager callbackManager;

    private ActivityConnexionBinding binding;

    private Button fbBtn;

    private Button googleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConnexionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        callbackManager = CallbackManager.Factory.create();
        googleBtn = binding.connexionGoogle;
        fbBtn = binding.connexionFb;
        setContentView(view);
        googleBtn.setOnClickListener(v -> {
            if (isCurrentUserLogged()){
                startMainActivity();
            } else {
                startGoogleSignInActivity();
            }
        });
        fbBtn.setOnClickListener(v -> {
            if (isCurrentUserLogged()){
                startMainActivity();
            } else {
                startFacebookSignInActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        startMainActivity();
    }

    private void startGoogleSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startFacebookSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }
}