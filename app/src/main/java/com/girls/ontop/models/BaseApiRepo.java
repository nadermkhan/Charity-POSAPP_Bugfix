package com.girls.ontop.models;

import retrofit2.Call;

public interface BaseApiRepo {

    Call<ProductStatus> getAllProducts(String token, int location_id);

    Call<BusinessLocationWithStatus> getAllBusinessLocationsWithStatus(String token);

    Call<RedXLocationStatus> getAllLocations(String token);

}