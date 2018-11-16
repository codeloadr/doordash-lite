package com.nsoni.doordash.network;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.nsoni.doordash.model.Restaurant;
import com.nsoni.doordash.model.RestaurantDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoorDashRepository {
    private static volatile DoorDashRepository doorDashRepositorySingleInstance;
    private final DoorDashClient mDoorDashClient;
    private MutableLiveData<List<Restaurant>> mRestaurants;

    private MutableLiveData<RestaurantDetail> mRestaurantDetail;

    private DoorDashRepository(Application application) {
        mRestaurantDetail = new MutableLiveData<>();
        mRestaurants = new MutableLiveData<>();
        mDoorDashClient = ServiceGenerator
                .getInstance(application)
                .createService(DoorDashClient.class);

    }

    public static DoorDashRepository getInstance(Application application) {
        if (doorDashRepositorySingleInstance == null) {
            synchronized (DoorDashRepository.class) {
                if (doorDashRepositorySingleInstance == null) {
                    doorDashRepositorySingleInstance = new DoorDashRepository(application);
                }
            }
        }
        return doorDashRepositorySingleInstance;
    }

    public LiveData<List<Restaurant>> getRestaurants(String latitude, String longitude, int offset, int limit) {

        Call<List<Restaurant>> listCall = mDoorDashClient.getRestaurants(latitude, longitude, offset, limit);

        listCall.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                List<Restaurant> body = response.body();
                if (body != null && !body.isEmpty()) {
                    mRestaurants.setValue(body);
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                // reset the list with empty elements
                mRestaurants.setValue(new ArrayList<Restaurant>());
            }
        });
        return mRestaurants;
    }

    public LiveData<RestaurantDetail> getRestaurantDetail(int id) {

        Call<RestaurantDetail> listCall = mDoorDashClient.getRestaurantDetail(id);

        listCall.enqueue(new Callback<RestaurantDetail>() {
            @Override
            public void onResponse(Call<RestaurantDetail> call, Response<RestaurantDetail> response) {
                RestaurantDetail body = response.body();
                if (body != null) {
                    mRestaurantDetail.setValue(body);
                }
            }

            @Override
            public void onFailure(Call<RestaurantDetail> call, Throwable t) {
                // reset the list with empty elements
                mRestaurantDetail.setValue(new RestaurantDetail());
            }
        });
        return mRestaurantDetail;
    }

}
