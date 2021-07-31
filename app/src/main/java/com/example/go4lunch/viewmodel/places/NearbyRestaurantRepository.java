package com.example.go4lunch.viewmodel.places;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.details.RestaurantDetails;
import com.example.go4lunch.model.restaurant.RestaurantOutputs;
import com.example.go4lunch.utils.RetrofitService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearbyRestaurantRepository {
    private static PlacesApiService placesApiService;
    private final MutableLiveData<RestaurantOutputs> sRestaurants = new MutableLiveData<>();

    private static NearbyRestaurantRepository sNearbyRestaurantRepository;

    public static NearbyRestaurantRepository getInstance() {
        if (sNearbyRestaurantRepository == null) {
            sNearbyRestaurantRepository = new NearbyRestaurantRepository();
        }
        return sNearbyRestaurantRepository;
    }

    public NearbyRestaurantRepository() {
        String baseUrlForNearbySearch = "https://maps.googleapis.com/maps/api/place/";
        placesApiService = RetrofitService.getPlacesInterface(baseUrlForNearbySearch);
    }

    public MutableLiveData<RestaurantOutputs> getRestaurants(String location, String radius, String key) {
        Call<RestaurantOutputs> restaurantsList = placesApiService.getFollowingPlaces(location, radius, "restaurant", key);
        restaurantsList.enqueue(new Callback<RestaurantOutputs>() {
            @Override
            public void onResponse(@NotNull Call<RestaurantOutputs> call, @NotNull Response<RestaurantOutputs> response) {
                sRestaurants.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<RestaurantOutputs> call, @NotNull Throwable t) {
                sRestaurants.postValue(null);
            }
        });
        return sRestaurants;
    }

    public MutableLiveData<RestaurantDetails> getRestaurant(String placeId, String key) {
        MutableLiveData<RestaurantDetails> restaurantOutputsMutableLiveData = new MutableLiveData<>();
        Call<RestaurantDetails> restaurantOutputsCall = placesApiService.getFollowingDetails(placeId, key);
        restaurantOutputsCall.enqueue(new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(@NotNull Call<RestaurantDetails> call, @NotNull Response<RestaurantDetails> response) {
                restaurantOutputsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<RestaurantDetails> call, @NotNull Throwable t) {
                restaurantOutputsMutableLiveData.postValue(null);
            }
        });
        return restaurantOutputsMutableLiveData;
    }
}
