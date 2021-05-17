package com.example.go4lunch.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.R;
import com.example.go4lunch.model.User;
import com.example.go4lunch.utils.UserCRUD;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserCRUDRepository {

    public LiveData<List<User>> getUsers(Context context) {
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        UserCRUD.getUsersCollection().get().addOnFailureListener(onFailureListener(context)).addOnSuccessListener(documentSnapshots -> {
            List<User> users = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                if(documentSnapshot != null) {
                    User user = documentSnapshot.toObject(User.class);
                    user.setUid(documentSnapshot.getId());
                    users.add(user);
                }
            }
            result.setValue(users);
        });
        return result;
    }

    public void getCurrentUserFirestore() {
        MutableLiveData<User> result = new MutableLiveData<>();
        UserCRUD.getUser(getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                User user = documentSnapshot.toObject(User.class);
                user.setUid(documentSnapshot.getId());
                result.setValue(user);
            }
        });
    }

    public void getUser(String uid, Context context) {
        MutableLiveData<User> result = new MutableLiveData<>();
        UserCRUD.getUser(uid).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot != null) {
                User user = documentSnapshot.toObject(User.class);
                result.setValue(user);
            }
        });
    }

    public void createUser(Context context) {
        MutableLiveData<Void> result = new MutableLiveData<>();

        FirebaseUser user = this.getCurrentUser();

        if(user != null) {

            String uid = user.getUid();
            String username = user.getDisplayName();
            String email = user.getEmail();
            String imageUrl = (user.getPhotoUrl() != null) ? Objects.requireNonNull(this.getCurrentUser().getPhotoUrl()).toString() : null;

            UserCRUD.createUser(uid, username, email, imageUrl, null).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
                Toast.makeText(context, R.string.user_created, Toast.LENGTH_SHORT).show();
                result.setValue(aVoid);
            });
        }
    }

    public void updateUserUsername(String uid, String username, Context context) {
        MutableLiveData<Void> result = new MutableLiveData<>();
        UserCRUD.updateUsername(uid, username).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            result.setValue(aVoid);
        });
    }

    public void updateUserEmail(String uid, String email, Context context) {
        MutableLiveData<Void> result = new MutableLiveData<>();
        UserCRUD.updateUserEmail(uid, email).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            result.setValue(aVoid);
        });
    }

    public void updateUserImage(String uid, String image, Context context) {
        MutableLiveData<Void> result = new MutableLiveData<>();
        UserCRUD.updateUserImage(uid, image).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            result.setValue(aVoid);
        });
    }

    public void updateUserRestaurant(String uid, String restaurantId, Context context) {
        MutableLiveData<Void> result = new MutableLiveData<>();
        UserCRUD.updateUserRestaurant(uid, restaurantId).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.restaurant_choice, Toast.LENGTH_SHORT).show();
            result.setValue(aVoid);
        });
    }

    public void deleteUser(String uid, Context context) {
        MutableLiveData<Void> result = new MutableLiveData<>();
        UserCRUD.deleteUser(uid).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.user_delete, Toast.LENGTH_SHORT).show();
            result.setValue(aVoid);
        });
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    private OnFailureListener onFailureListener(Context context) {
        return e -> Toast.makeText(context, R.string.fui_error_unknown, Toast.LENGTH_LONG).show();
    }
}