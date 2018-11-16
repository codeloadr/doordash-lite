package com.nsoni.doordash;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.nsoni.doordash.model.Restaurant;
import com.nsoni.doordash.network.DoorDashRepository;

import java.util.List;

public class RestaurantsListViewModel extends AndroidViewModel {

    public static final String DOORDASH_HQ_LATITUDE = "37.422740";
    public static final String DOORDASH_HQ_LONGITUDE = "-122.139956";
    public static final int PAGE_SIZE = 20;
    private final DoorDashRepository mDoorDashRepository;
    private int mOffset;
    private LiveData<List<Restaurant>> mListLiveData;

    public RestaurantsListViewModel(Application application) {
        super(application);
        mDoorDashRepository = DoorDashRepository.getInstance(application);
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        mListLiveData = mDoorDashRepository.getRestaurants(DOORDASH_HQ_LATITUDE, DOORDASH_HQ_LONGITUDE, mOffset, PAGE_SIZE);
        return mListLiveData;
    }

    public LiveData<List<Restaurant>> next() {
        mOffset = mOffset + PAGE_SIZE;
        return getRestaurants();
    }


    public LiveData<List<Restaurant>> previous() {
        mOffset = mOffset - PAGE_SIZE;
        if (mOffset < 0) {
            mOffset = 0;
        }
        return getRestaurants();
    }

}