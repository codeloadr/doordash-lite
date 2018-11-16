package com.nsoni.doordash;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.nsoni.doordash.model.RestaurantDetail;
import com.nsoni.doordash.network.DoorDashRepository;

public class RestaurantDetailViewModel extends AndroidViewModel {


    private final DoorDashRepository mDoorDashRepository;

    public RestaurantDetailViewModel(@NonNull Application application) {
        super(application);
        mDoorDashRepository = DoorDashRepository.getInstance(application);
    }

    public LiveData<RestaurantDetail> getRestaurantDetail(int id) {
        return mDoorDashRepository.getRestaurantDetail(id);
    }
}
