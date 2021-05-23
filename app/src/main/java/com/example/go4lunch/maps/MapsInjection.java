package com.example.go4lunch.maps;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MapsInjection {

    public static MapsRepository provideMapsRepository() {return new MapsRepository();}

    public static Executor provideExecutor() {return Executors.newSingleThreadExecutor();}

    public static MapsViewModelFactory provideMapsViewModelFactory() {
        MapsRepository mapsRepository = provideMapsRepository();
        Executor executor = provideExecutor();
        return new MapsViewModelFactory(mapsRepository, executor);
    }
}
