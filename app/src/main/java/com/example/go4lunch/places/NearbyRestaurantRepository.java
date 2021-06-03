package com.example.go4lunch.places;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.details.RestaurantDetails;
import com.example.go4lunch.model.restaurant.NearbyRestaurantOutputs;
import com.example.go4lunch.utils.RetrofitService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearbyRestaurantRepository {
    private static PlacesApiService placesApiService;
    private final MutableLiveData<NearbyRestaurantOutputs> sRestaurants = new MutableLiveData<>();
    private final MutableLiveData<RestaurantDetails> restaurantOutputsMutableLiveData = new MutableLiveData<>();
    private final String mBaseUrlForNearbySearch = "https://maps.googleapis.com/maps/api/place/";

    private static NearbyRestaurantRepository sNearbyRestaurantRepository;

    public static NearbyRestaurantRepository getInstance() {
        if (sNearbyRestaurantRepository == null) {
            sNearbyRestaurantRepository = new NearbyRestaurantRepository();
        }
        return sNearbyRestaurantRepository;
    }

    public NearbyRestaurantRepository() {
        placesApiService = RetrofitService.getPlacesInterface(mBaseUrlForNearbySearch);
    }

    public MutableLiveData<NearbyRestaurantOutputs> getRestaurants(String location, String radius, String key) {
        Call<NearbyRestaurantOutputs> restaurantsList = placesApiService.getFollowingPlaces(location, radius,"restaurant", key);
        restaurantsList.enqueue(new Callback<NearbyRestaurantOutputs>() {
            @Override
            public void onResponse(@NotNull Call<NearbyRestaurantOutputs> call, @NotNull Response<NearbyRestaurantOutputs> response) {
                sRestaurants.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<NearbyRestaurantOutputs> call, @NotNull Throwable t) {
                sRestaurants.postValue(null);
            }
        });
        return sRestaurants;
    }

    public MutableLiveData<RestaurantDetails> getRestaurant(String placeId, String key) {
        Call<RestaurantDetails> restaurantOutputsCall = placesApiService.getFollowingDetails(placeId, key);
        restaurantOutputsCall.enqueue(new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(@NotNull Call<RestaurantDetails> call, @NotNull Response<RestaurantDetails> response) {
                restaurantOutputsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<RestaurantDetails> call, Throwable t) {
                restaurantOutputsMutableLiveData.postValue(null);
            }
        });
        return restaurantOutputsMutableLiveData;
    }
}
