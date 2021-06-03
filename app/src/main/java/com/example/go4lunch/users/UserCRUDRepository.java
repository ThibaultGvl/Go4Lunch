package com.example.go4lunch.users;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.R;
import com.example.go4lunch.model.User;
import com.example.go4lunch.users.UserCRUD;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserCRUDRepository {

    public MutableLiveData<List<User>> getUsers(Context context) {
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        UserCRUD.getUsers().addOnFailureListener(onFailureListener(context)).addOnSuccessListener(documentSnapshots -> {
            List<User> users = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                if(documentSnapshot != null && !documentSnapshot.getId().equals(getCurrentUser().getUid())) {
                    User user = documentSnapshot.toObject(User.class);
                    users.add(user);
                }
            }
            result.setValue(users);
        });
        return result;
    }

    public MutableLiveData<User> getCurrentUserFirestore() {
        MutableLiveData<User> result = new MutableLiveData<>();
        UserCRUD.getUser(getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                User user = documentSnapshot.toObject(User.class);
                result.setValue(user);
            }
        });
        return result;
    }

    public MutableLiveData<User> getUser(String uid, Context context) {
        MutableLiveData<User> result = new MutableLiveData<>();
        UserCRUD.getUser(uid).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot != null) {
                User user = documentSnapshot.toObject(User.class);
                result.setValue(user);
            }
        });
        return result;
    }

    public void createUser(Context context) {
        MutableLiveData<FirebaseUser> result = new MutableLiveData<>();

        FirebaseUser user = this.getCurrentUser();

        if(user != null) {

            String uid = user.getUid();
            String username = user.getDisplayName();
            String email = user.getEmail();
            String imageUrl = (user.getPhotoUrl() != null) ? Objects.requireNonNull(this.getCurrentUser().getPhotoUrl()).toString() : null;

            UserCRUD.createUser(uid, username, email, imageUrl, null, null).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
                result.setValue(user);
            });
        }
    }

    public void updateUserUsername(String username, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        String uid = getCurrentUser().getUid();
        UserCRUD.updateUsername(uid, username).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            result.setValue(username);
        });
    }

    public void updateUserEmail(String email, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        String uid = getCurrentUser().getUid();
        UserCRUD.updateUserEmail(uid, email).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            result.setValue(email);
        });
    }

    public void updateUserImage(String picture, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        String uid = getCurrentUser().getUid();
        UserCRUD.updateUserImage(uid, picture).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            result.setValue(picture);
        });
    }

    public void updateUserRestaurant(String restaurantId, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        String uid = getCurrentUser().getUid();
        UserCRUD.updateUserRestaurant(uid, restaurantId).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.restaurant_choice, Toast.LENGTH_SHORT).show();
            result.setValue(restaurantId);
        });
    }

    public void updateUserRestaurantsLiked(String restaurantId, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        String uid = getCurrentUser().getUid();
        UserCRUD.updateUserRestaurantsLike(uid, restaurantId).addOnFailureListener(onFailureListener(context)).addOnCompleteListener(aVoid -> {
            Toast.makeText(context, R.string.restaurant_choice, Toast.LENGTH_SHORT).show();
            result.setValue(restaurantId);
        });
    }

    public void deleteUser(Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        String uid = getCurrentUser().getUid();
        UserCRUD.deleteUser(uid).addOnFailureListener(onFailureListener(context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.user_delete, Toast.LENGTH_SHORT).show();
            result.setValue(uid);
        });
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    private OnFailureListener onFailureListener(Context context) {
        return e -> Toast.makeText(context, R.string.fui_error_unknown, Toast.LENGTH_LONG).show();
    }
}