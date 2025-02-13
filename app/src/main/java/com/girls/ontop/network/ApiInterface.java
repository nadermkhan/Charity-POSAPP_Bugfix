package com.girls.ontop.network;

import com.girls.ontop.models.SellListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("sells") // Assuming this is the endpoint for fetching the list of sells
    Call<SellListResponse> getSells();
}
