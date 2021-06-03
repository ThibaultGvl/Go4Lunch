package com.example.go4lunch.users;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.model.User;

import java.util.List;
import java.util.concurrent.Executor;

public class UserViewModel extends androidx.lifecycle.ViewModel {

    private final UserCRUDRepository mUserCRUDRepository;

    private final Executor executor;

    private LiveData<List<User>> users;

    public UserViewModel(UserCRUDRepository userCRUDRepository, Executor executor){
        this.mUserCRUDRepository = userCRUDRepository;
        this.executor = executor;
    }

    public void initUsers(Context context) {
        if (this.users != null) {
            return;
        }
        users = mUserCRUDRepository.getUsers(context);
    }

    public LiveData<List<User>> getUsers() {return users;}

    public void getUser(String uid, Context context) {
        executor.execute(() -> mUserCRUDRepository.getUser(uid, context));
    }

    public void createCurrentUser(Context context) {
        executor.execute(() -> mUserCRUDRepository.createUser(context));
    }

    public void updateUsername(String username, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserUsername(username, context));
    }

    public void updateUserEmail(String email, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserEmail(email, context));
    }

    public void updateUserImage(String image, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserImage(image, context));
    }

    public void updateUserRestaurant(String restaurantId, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserRestaurant(restaurantId, context));
    }

    public void updateRestaurantsLiked(String restaurantId, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserRestaurantsLiked(restaurantId, context));
    }

    public void getCurrentUser() {
        executor.execute(mUserCRUDRepository::getCurrentUserFirestore);
    }

    public void deleteUser(Context context) {
        executor.execute(() -> mUserCRUDRepository.deleteUser(context));
    }

}
