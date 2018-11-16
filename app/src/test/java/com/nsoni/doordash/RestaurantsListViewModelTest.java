package com.nsoni.doordash;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.nsoni.doordash.model.Business;
import com.nsoni.doordash.model.Restaurant;
import com.nsoni.doordash.network.DoorDashRepository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;


public class RestaurantsListViewModelTest {

    @Mock
    Application mContext;
    private RestaurantsListViewModel mViewModel;
    private DoorDashRepository mDoorDashRepositoryMock;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private MutableLiveData<List<Restaurant>> mListLiveData;
    private ArrayList<Restaurant> mDataList;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        mDoorDashRepositoryMock = Mockito.mock(DoorDashRepository.class);
        TestUtils.setMockedRepository(mDoorDashRepositoryMock);

        mViewModel = new RestaurantsListViewModel(mContext);
        mListLiveData = new MutableLiveData<>();
        mDataList = new ArrayList<>();
        mDataList.add(new Restaurant(1234, "Wonton", "22 min", "open", new Business(1234, "Wonton"), "https://image.wonton.com/cover.png."));
        mListLiveData.setValue(mDataList);
    }

    @After
    public void tearDown() throws Exception {
        mDoorDashRepositoryMock = null;
        mViewModel = null;
    }

    @Test
    public void getRestaurants_WhenValidRequest_ShouldReturnListOfRestaurants() {
        // setup
        Mockito.when(mDoorDashRepositoryMock.getRestaurants(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mListLiveData);

        // test
        LiveData<List<Restaurant>> listMutableLiveData = mViewModel.getRestaurants();


        // verify
        Assert.assertNotNull(listMutableLiveData);

        for (Restaurant restaurant : listMutableLiveData.getValue()) {
            Assert.assertEquals(mDataList.get(0).getName(), restaurant.getName());
            Assert.assertEquals(mDataList.get(0).getId(), restaurant.getId());
            Assert.assertEquals(mDataList.get(0).getStatus(), restaurant.getStatus());
            Assert.assertEquals(mDataList.get(0).getCoverImgUrl(), restaurant.getCoverImgUrl());
        }

    }

    @Test
    public void next_WhenValidRequest_ShouldReturnNextPageOfRestaurants() {
        // setup
        Mockito.when(mDoorDashRepositoryMock.getRestaurants(anyString(), anyString(), eq(20), eq(20))).thenReturn(mListLiveData);

        // test
        LiveData<List<Restaurant>> listMutableLiveData = mViewModel.next();

        // verify
        Mockito.verify(mDoorDashRepositoryMock).getRestaurants(anyString(), anyString(), eq(20), eq(20));
        for (Restaurant restaurant : listMutableLiveData.getValue()) {
            Assert.assertEquals(mDataList.get(0).getName(), restaurant.getName());
            Assert.assertEquals(mDataList.get(0).getId(), restaurant.getId());
            Assert.assertEquals(mDataList.get(0).getStatus(), restaurant.getStatus());
            Assert.assertEquals(mDataList.get(0).getCoverImgUrl(), restaurant.getCoverImgUrl());
        }

    }

    @Test
    public void previous_WhenValidRequest_ShouldReturnPreviousPageOfRestaurants() {

        // setup
        Mockito.when(mDoorDashRepositoryMock.getRestaurants(anyString(), anyString(), eq(0), eq(20))).thenReturn(mListLiveData);

        // test
        LiveData<List<Restaurant>> listMutableLiveData = mViewModel.previous();

        // verify
        Mockito.verify(mDoorDashRepositoryMock).getRestaurants(anyString(), anyString(), anyInt(), eq(20));
        for (Restaurant restaurant : listMutableLiveData.getValue()) {
            Assert.assertEquals(mDataList.get(0).getName(), restaurant.getName());
            Assert.assertEquals(mDataList.get(0).getId(), restaurant.getId());
            Assert.assertEquals(mDataList.get(0).getStatus(), restaurant.getStatus());
            Assert.assertEquals(mDataList.get(0).getCoverImgUrl(), restaurant.getCoverImgUrl());
        }
    }
}