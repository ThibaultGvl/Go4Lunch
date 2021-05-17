package com.example.go4lunch.injections;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.repository.UserCRUDRepository;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory{

    private final UserCRUDRepository mUserCRUDRepository;
    private final Executor mExecutor;


    public ViewModelFactory(UserCRUDRepository userCRUDRepository, Executor executor){
        mUserCRUDRepository = userCRUDRepository;
        mExecutor = executor;
    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(com.example.go4lunch.ui.ViewModel.class)) {
            return (T) new com.example.go4lunch.ui.ViewModel(mUserCRUDRepository, mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
