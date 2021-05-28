package com.example.go4lunch.location;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LocationInjection {

    public static LocationRepository provideLocationRepository() {return new LocationRepository();}

    public static Executor provideExecutor() {return Executors.newSingleThreadExecutor();}

    public static LocationViewModelFactory provideMapsViewModelFactory() {
        LocationRepository mapsRepository = provideLocationRepository();
        Executor executor = provideExecutor();
        return new LocationViewModelFactory(mapsRepository, executor);
    }
}
