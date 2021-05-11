package com.example.go4lunch.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.example.go4lunch.utils.UserCRUD;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class UserCRUDRepository {

    private final UserCRUD mUserCRUD;

    public UserCRUDRepository(UserCRUD userCRUD) {
        this.mUserCRUD = userCRUD;
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    public void getCurrentUserFirestore() {
        UserCRUD.getUser(getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User currentUser = documentSnapshot.toObject(User.class);
            }
        });
    }

    public void getUsers() {
        UserCRUD.getUsersCollection();
    }

    public void createUser(Context context) {

        if(getCurrentUser() != null) {

            String uid = this.getCurrentUser().getUid();
            String username = this.getCurrentUser().getDisplayName();
            String email = this.getCurrentUser().getEmail();
            String imageUrl = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            Restaurant restaurant = null;

            UserCRUD.createUser(uid, username, email, imageUrl, restaurant).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, R.string.user_created, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getUser(String uid, Context context) {
        UserCRUD.getUser(uid).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User userToGet = documentSnapshot.toObject(User.class);
            }
        });
    }

    public void updateUserRestaurant(String uid, Restaurant restaurant, Context context) {
        UserCRUD.updateUserRestaurant(uid, restaurant).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, R.string.restaurant_choice, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteUser(String uid,Context context) {
        UserCRUD.deleteUser(uid).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, R.string.user_delete, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private OnFailureListener onFailureListener(Context context) {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, R.string.fui_error_unknown, Toast.LENGTH_LONG).show();
            }
        };
    }
}
