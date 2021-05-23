package com.example.go4lunch.users;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.users.UserCRUDRepository;
import com.example.go4lunch.users.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class UserViewModelFactory implements ViewModelProvider.Factory{

    private final UserCRUDRepository mUserCRUDRepository;
    private final Executor mExecutor;


    public UserViewModelFactory(UserCRUDRepository userCRUDRepository, Executor executor){
        mUserCRUDRepository = userCRUDRepository;
        mExecutor = executor;
    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(mUserCRUDRepository, mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
