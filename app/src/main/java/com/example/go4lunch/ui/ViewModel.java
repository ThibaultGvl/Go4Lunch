package com.example.go4lunch.ui;

import android.content.Context;
import android.location.GnssAntennaInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.repository.UserCRUDRepository;
import com.example.go4lunch.utils.UserCRUD;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ViewModel extends androidx.lifecycle.ViewModel {

    private final UserCRUDRepository mUserCRUDRepository;

    private final Executor executor;

    private LiveData<List<User>> users;

    public ViewModel(UserCRUDRepository userCRUDRepository, Executor executor){
        this.mUserCRUDRepository = userCRUDRepository;
        this.executor = executor;
    }

    public LiveData<List<User>> getUsers() {return users;}

    public void getUser(String uid, Context context) {
        executor.execute(() -> mUserCRUDRepository.getUser(uid, context));
    }

    public void createUser(Context context) {
        executor.execute(() -> mUserCRUDRepository.createUser(context));
    }

    public void updateUserRestaurant(String uid, Restaurant restaurant, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserRestaurant(uid, restaurant, context));
    }

    public void getCurrentUser() {
        executor.execute(mUserCRUDRepository::getCurrentUserFirestore);
    }

    public void deleteUser(String uid, Context context) {
        executor.execute(() -> mUserCRUDRepository.deleteUser(uid, context));
    }

}
