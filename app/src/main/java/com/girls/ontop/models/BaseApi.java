package com.girls.ontop.models;

import android.provider.ContactsContract;

import com.girls.ontop.utils.AppConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BaseApi {

    @POST(AppConstants.locationWiseProducts)
    Call<ProductStatus> getAllProducts(
            @Header(AppConstants.authorization) String token,
            @Query(AppConstants.location_id) int location_id
    );

    @GET(AppConstants.businessLocation)
    Call<BusinessLocationWithStatus> getAllBusinessLocationsWithStatus(
            @Header(AppConstants.authorization) String token
    );

    @GET(AppConstants.all_location_end_point)
    Call<RedXLocationStatus> getAllLocations(
            @Header(AppConstants.authorization) String token
    );






    @GET(AppConstants.ProfileEndPoint)
    Call<ContactsContract.Profile> getProfile(
            @Header(AppConstants.authorization) String token
    );
}
