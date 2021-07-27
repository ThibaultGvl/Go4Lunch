package com.example.go4lunch.viewmodel.users;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.R;
import com.example.go4lunch.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserCRUDRepository {

    MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<User>> getUsers(Context context) {
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        UserCRUD.getUsers().addOnFailureListener(onFailureListener(context)).addOnSuccessListener
                (documentSnapshots -> {
            List<User> users = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                if(documentSnapshot != null && !documentSnapshot.getId().equals(getCurrentUser()
                        .getUid())) {
                    User user = documentSnapshot.toObject(User.class);
                    users.add(user);
                }
            }
            result.setValue(users);
        });
        return result;
    }

    public MutableLiveData<List<User>> getUserByPlaceId(String placeId, Context context) {
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        UserCRUD.getUsers().addOnFailureListener(onFailureListener(context)).addOnSuccessListener
                (documentSnapshots -> {
           List<User> users =  new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : documentSnapshots.getDocuments()) {
                if (Objects.equals(documentSnapshot.get("restaurant"), placeId) && !documentSnapshot
                        .getId().equals(getCurrentUser().getUid())) {
                    User user = documentSnapshot.toObject(User.class);
                    users.add(user);
                }
            }
            result.setValue(users);
        });
        return result;
    }

    public MutableLiveData<User> getCurrentUserFirestore(Context context) {
        MutableLiveData<User> result = new MutableLiveData<>();
        String uid = getCurrentUser().getUid();
        UserCRUD.getUser(uid).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null) {
                User user = documentSnapshot.toObject(User.class);
                result.setValue(user);
            }
        });
        return result;
    }

    public MutableLiveData<User> getUser(String uid, Context context) {
        MutableLiveData<User> result = new MutableLiveData<>();
        UserCRUD.getUser(uid).addOnFailureListener(onFailureListener(context)).addOnSuccessListener
                (documentSnapshot -> {
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
            String imageUrl = (user.getPhotoUrl() != null) ? Objects.requireNonNull
                    (this.getCurrentUser().getPhotoUrl()).toString() : null;

            UserCRUD.createUser(uid, username, email, imageUrl, "", new ArrayList<>(),
                    null, null).addOnFailureListener
                    (onFailureListener(context)).addOnSuccessListener
                    (aVoid -> result.setValue(user));
        }
    }

    public void updateUserUsername(String uid, String username, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();

        UserCRUD.updateUsername(uid, username).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            result.setValue(username);
        });
    }

    public void updateUserEmail(String uid, String email, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();

        UserCRUD.updateUserEmail(uid, email).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            result.setValue(email);
        });
    }

    public void updateUserImage(String uid, String picture, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.updateUserImage(uid, picture).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
            result.setValue(picture);
        });
    }

    public void updateUserRestaurant(String uid, String restaurantId, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.updateUserRestaurant(uid, restaurantId).addOnFailureListener(onFailureListener
                (context)).addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.restaurant_choice, Toast.LENGTH_SHORT).show();
            result.setValue(restaurantId);
        });
    }

    public void updateUserRestaurantName(String uid, String restaurantName, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.updateRestaurantName(uid, restaurantName).addOnFailureListener(onFailureListener
                (context)).addOnSuccessListener(
                aVoid -> result.setValue(restaurantName));
    }

    public void updateUserRestaurantAddress(String uid, String restaurantAddress, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.updateRestaurantAddress(uid, restaurantAddress).addOnFailureListener
                (onFailureListener(context)).addOnSuccessListener(
                aVoid -> result.setValue(restaurantAddress));
    }

    public MutableLiveData<List<String>> getRestaurantsFavorites(String uid, Context context) {
        UserCRUD.getUser(uid).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(documentsSnapshot -> {
                    List<String> restaurantsLikedFirestore;
                    if (documentsSnapshot != null) {
                        restaurantsLikedFirestore =
                                (List<String>) documentsSnapshot.get("restaurantsLiked");
                        }
                    else {
                        restaurantsLikedFirestore = new ArrayList<>();
                    }
                    mutableLiveData.setValue(restaurantsLikedFirestore);
                });
        return mutableLiveData;
    }

    public void updateRestaurantsLiked(String uid, List<String> restaurantsLiked, String restaurantLike, Context context) {
        if (restaurantsLiked != null) {
            if (restaurantsLiked.contains(restaurantLike)) {
                restaurantsLiked.remove(restaurantLike);
            }
            else {
                restaurantsLiked.add(restaurantLike);
            }
            UserCRUD.updateRestaurantsLiked(uid, restaurantsLiked).addOnFailureListener(onFailureListener(context));
        }
    }

    public void deleteUser(String uid, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();
        UserCRUD.deleteUser(uid).addOnFailureListener(onFailureListener(context))
                .addOnSuccessListener(aVoid -> {
            Toast.makeText(context, R.string.user_delete, Toast.LENGTH_SHORT).show();
            result.setValue(uid);
        });
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser();}

    private OnFailureListener onFailureListener(Context context) {
        return e -> Toast.makeText(context, R.string.fui_error_unknown, Toast.LENGTH_LONG).show();
    }
}