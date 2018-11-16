package com.nsoni.doordash.network;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;

import com.nsoni.doordash.TestUtils;
import com.nsoni.doordash.model.Business;
import com.nsoni.doordash.model.Restaurant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DoorDashRepositoryTest {

    @Mock
    Application mContext;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private Call<List<Restaurant>> mListLiveData;
    private ArrayList<Restaurant> mDataList;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        mListLiveData = mock(Call.class);
        mDataList = new ArrayList<>();
        mDataList.add(new Restaurant(1234, "Wonton", "22 min", "open", new Business(1234, "Wonton"), "https://image.wonton.com/cover.png."));
    }

    @Test
    public void getInstance_shouldReturnSingletonObject() {

        DoorDashRepository doorDashRepository1 = DoorDashRepository.getInstance(mContext);
        DoorDashRepository doorDashRepository2 = DoorDashRepository.getInstance(mContext);

        Assert.assertEquals(doorDashRepository1, doorDashRepository2);
    }

    @Test
    public void getRestaurants_WhenSuccessResponse_ShouldReturnListOfRestaurants() throws Exception {
        // setup
        ServiceGenerator serviceGeneratorMock = mock(ServiceGenerator.class);
        TestUtils.setMockedRepository(serviceGeneratorMock);

        DoorDashClient doorDashClientMock = mock(DoorDashClient.class);

        doReturn(doorDashClientMock).when(serviceGeneratorMock).createService(DoorDashClient.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Callback<List<Restaurant>> callback = (Callback<List<Restaurant>>) invocation.getArguments()[0];
                Response response = Response.success(mDataList);
                callback.onResponse(null, response);
                return null;
            }
        }).when(mListLiveData).enqueue(Matchers.<Callback<List<Restaurant>>>any());

        when(doorDashClientMock.getRestaurants(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mListLiveData);

        // test
        LiveData<List<Restaurant>> listMutableLiveData = DoorDashRepository.getInstance(mContext).
                getRestaurants("37.422740", "-122.139956", 0, 20);

        // verify
        Assert.assertNotNull(listMutableLiveData);
        verify(doorDashClientMock).getRestaurants(anyString(), anyString(), anyInt(), anyInt());

        for (Restaurant restaurant : listMutableLiveData.getValue()) {
            Assert.assertEquals(mDataList.get(0).getName(), restaurant.getName());
            Assert.assertEquals(mDataList.get(0).getId(), restaurant.getId());
            Assert.assertEquals(mDataList.get(0).getStatus(), restaurant.getStatus());
            Assert.assertEquals(mDataList.get(0).getCoverImgUrl(), restaurant.getCoverImgUrl());
        }
    }

    @Test
    public void getRestaurants_WhenErrorResponse_ShouldReturnEmptyList() throws Exception {
        // setup
        ServiceGenerator serviceGeneratorMock = mock(ServiceGenerator.class);
        TestUtils.setMockedRepository(serviceGeneratorMock);

        DoorDashClient doorDashClientMock = mock(DoorDashClient.class);

        doReturn(doorDashClientMock).when(serviceGeneratorMock).createService(DoorDashClient.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Callback<List<Restaurant>> callback = (Callback<List<Restaurant>>) invocation.getArguments()[0];
                callback.onFailure(null, new Throwable());
                return null;
            }
        }).when(mListLiveData).enqueue(Matchers.<Callback<List<Restaurant>>>any());

        when(doorDashClientMock.getRestaurants(anyString(), anyString(), anyInt(), anyInt())).thenReturn(mListLiveData);

        // test
        LiveData<List<Restaurant>> listMutableLiveData = DoorDashRepository.getInstance(mContext).
                getRestaurants("37.422740", "-122.139956", 0, 20);

        // verify
        Assert.assertNotNull(listMutableLiveData);
        verify(doorDashClientMock).getRestaurants(anyString(), anyString(), anyInt(), anyInt());

        Assert.assertEquals(0, listMutableLiveData.getValue().size());
    }
}