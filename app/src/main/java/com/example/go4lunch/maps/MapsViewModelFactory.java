package com.example.go4lunch.maps;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

public class MapsViewModelFactory implements ViewModelProvider.Factory {

    private final MapsRepository mMapsRepository;

    private final Executor mExecutor;

    public MapsViewModelFactory(MapsRepository mapsRepository, Executor executor) {
        mMapsRepository = mapsRepository;
        mExecutor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapsViewModel.class)) {
            return (T) new MapsViewModel(mMapsRepository, mExecutor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
