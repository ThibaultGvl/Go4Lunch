package com.example.go4lunch.viewmodel.users;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public LiveData<User> getUser(String uid, Context context) {
        LiveData<User> user = mUserCRUDRepository.getUser(uid, context);
        return user;
    }

    public MutableLiveData<User> getCurrentUser(Context context) {
        MutableLiveData<User> user = new MutableLiveData<>();
        user = mUserCRUDRepository.getCurrentUserFirestore(context);
        return user;
    }

    public LiveData<List<User>> getUserByPlaceId(String placeId, Context context) {
        LiveData<List<User>> users = mUserCRUDRepository.getUserByPlaceId(placeId, context);
        return users;
    }

    public void createCurrentUser(Context context) {
        executor.execute(() -> mUserCRUDRepository.createUser(context));
    }

    public void updateUsername(String uid, String username, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserUsername(uid, username, context));
    }

    public void updateUserEmail(String uid, String email, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserEmail(uid, email, context));
    }

    public void updateUserImage(String uid, String image, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserImage(uid, image, context));
    }

    public void updateUserRestaurant(String uid, String restaurantId, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserRestaurant(uid, restaurantId, context));
    }

    public void updateUserRestaurantAddress(String uid, String restaurantAddress, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserRestaurantAddress(uid, restaurantAddress, context));
    }

    public void updateRestaurantsFavouritesList(String uid, String restaurantToLike, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateRestaurantsFavouritesList(uid, restaurantToLike, context));
    }

    public void updateUserRestaurantName(String uid, String restaurantToLike, Context context) {
        executor.execute(() -> mUserCRUDRepository.updateUserRestaurantName(uid, restaurantToLike, context));
    }

    public void deleteUser(String uid, Context context) {
        executor.execute(() -> mUserCRUDRepository.deleteUser(uid, context));
    }

}
