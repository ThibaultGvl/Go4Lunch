package com.example.go4lunch.places;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.model.Autocomplete.RestaurantAutoComplete;
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

    public MutableLiveData<RestaurantOutputs> getRestaurants(String location, String radius, String key) {
        Call<RestaurantOutputs> restaurantsList = placesApiService.getFollowingPlaces(location, radius,"restaurant", key);
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
            public void onResponse(Call<RestaurantDetails> call, Response<RestaurantDetails> response) {
                restaurantOutputsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RestaurantDetails> call, Throwable t) {
                restaurantOutputsMutableLiveData.postValue(null);
            }
        });
        return restaurantOutputsMutableLiveData;
    }

    public MutableLiveData<RestaurantAutoComplete> getRestaurantBySearch(String input, String key) {
        MutableLiveData<RestaurantAutoComplete> restaurantAutoCompleteMutableLiveData = new MutableLiveData<>();
        Call<RestaurantAutoComplete> restaurantAutoCompleteCall = placesApiService.getFollowingAutocomplete(input, key);
        restaurantAutoCompleteCall.enqueue(new Callback<RestaurantAutoComplete>() {
            @Override
            public void onResponse(Call<RestaurantAutoComplete> call, Response<RestaurantAutoComplete> response) {
                restaurantAutoCompleteMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<RestaurantAutoComplete> call, Throwable t) {
                restaurantAutoCompleteMutableLiveData.setValue(null);
            }
        });
        return restaurantAutoCompleteMutableLiveData;
    }
}
