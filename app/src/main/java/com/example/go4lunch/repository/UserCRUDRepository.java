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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class UserCRUDRepository {

    private final UserCRUD mUserCRUD;

    private User userToGet;

    private User currentUser;

    public UserCRUDRepository(UserCRUD userCRUD) {
        this.mUserCRUD = userCRUD;
    }

    public LiveData<List<User>> getUsers(Context context) {
        LiveData<List<User>> result;
        UserCRUD.getUsersCollection().get().addOnFailureListener(onFailureListener(context)).addOnSuccessListener(
                (OnSuccessListener<? super QuerySnapshot>) (result = (LiveData<List<User>>) Objects.requireNonNull(UserCRUD.getUsersCollection().get().getResult()).toObjects(User.class))
        );
        return result;
    }

    public void getCurrentUserFirestore() {
        UserCRUD.getUser(getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                documentSnapshot = UserCRUD.getUser(getCurrentUser().getUid()).getResult();
            }
        });
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
                if(documentSnapshot != null) {
                    documentSnapshot = UserCRUD.getUser(uid).getResult();
                }
         }
        });
    }

    public void updateUserUsername(String uid, String username, Context context) {
        UserCRUD.updateUsername(uid, username).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUserEmail(String uid, String email, Context context) {
        UserCRUD.updateUserEmail(uid, email).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUserImage(String uid, String image, Context context) {
        UserCRUD.updateUserImage(uid, image).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
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

    public void deleteUser(String uid, Context context) {
        UserCRUD.deleteUser(uid).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, R.string.user_delete, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }


    private OnFailureListener onFailureListener(Context context) {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, R.string.fui_error_unknown, Toast.LENGTH_LONG).show();
            }
        };
    }
}