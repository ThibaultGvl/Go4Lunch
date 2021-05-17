package com.example.go4lunch.utils;

import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserCRUD {

    private static final String COLLECTION_NAME = "users";

    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserCRUD.getUsersCollection().document(uid).get();
    }

    public static Task<Void> createUser(String uid, String username, String email, String userImage, String restaurantId) {
        User userToCreate = new User(uid, username, email, userImage, restaurantId);
        return UserCRUD.getUsersCollection().document().set(userToCreate);
    }

    public static Task<Void> updateUsername(String uid, String username) {
        return UserCRUD.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateUserEmail(String uid, String email) {
        return UserCRUD.getUsersCollection().document(uid).update("email", email);
    }

    public static Task<Void> updateUserImage(String uid, String userImage) {
        return UserCRUD.getUsersCollection().document(uid).update("image", userImage);
    }

    public static Task<Void> updateUserRestaurant(String uid, String restaurantId) {
        return UserCRUD.getUsersCollection().document(uid).update("restaurant_id", restaurantId);
    }

    public static Task<Void> deleteUser(String uid) {
        return UserCRUD.getUsersCollection().document(uid).delete();
    }
}