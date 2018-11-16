package com.nsoni.doordash.network;


import com.nsoni.doordash.model.Restaurant;
import com.nsoni.doordash.model.RestaurantDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DoorDashClient {

    @GET("/v2/restaurant/")
    Call<List<Restaurant>> getRestaurants(@Query("lat") String latitude,
                                          @Query("lng") String longitude,
                                          @Query("offset") int offset,
                                          @Query("limit") int limit);

    @GET("/v2/restaurant/{id}/")
    Call<RestaurantDetail> getRestaurantDetail(@Path("id") int id);
}
