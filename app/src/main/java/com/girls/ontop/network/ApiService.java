package com.girls.ontop.network;

import com.girls.ontop.adapters.UpdatePathaoRequest;
import com.girls.ontop.models.AccountResponse;
import com.girls.ontop.models.AreaResponse;
import com.girls.ontop.models.CityResponse;
import com.girls.ontop.models.ContactRequest;
import com.girls.ontop.models.CustomerResponse;
import com.girls.ontop.models.ExpenseCategoryResponse;
import com.girls.ontop.models.ExpenseRequest;
import com.girls.ontop.models.ExpenseResponse;
import com.girls.ontop.models.LocationResponse;
import com.girls.ontop.models.LoginResponse;
import com.girls.ontop.models.NewCustomerResponse;
import com.girls.ontop.models.OrderUpdateRequest;
import com.girls.ontop.models.PaymentBody;
import com.girls.ontop.models.ProductResponse;
import com.girls.ontop.models.ProductionResponse;
import com.girls.ontop.models.SellListResponse;
import com.girls.ontop.models.SellsObject;
import com.girls.ontop.models.StoreResponse;
import com.girls.ontop.models.UpdateShippingStatusRequest;
import com.girls.ontop.models.UserResponse;
import com.girls.ontop.models.ZoneResponse;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<LoginResponse> login(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("username") String username,
            @Field("password") String password,
            @Field("scope") String scope
    );

    @POST("/connector/api/sell")
    Call<ResponseBody> submitOrder(
            @Header("Authorization") String authToken,
            @Body SellsObject sellsObject
    );


    @PUT("/connector/api/sell/{id}")
    Call<ResponseBody> updateOrder(
            @Header("Authorization") String authToken,
            @Body OrderUpdateRequest sellsObject,
            @Path("id") String id
    );

    @DELETE("/connector/api/sell/{id}")
    Call<Void> deleteOrder(
            @Header("Authorization") String authToken,
            @Path("id") String id
    );



    @POST("/connector/api/contactapi-payment")
    Call<ResponseBody> makepayment(
            @Header("Authorization") String authToken,
            @Body PaymentBody body
    );




    @GET("connector/api/user/loggedin")
    Call<UserResponse> getLoggedInUser(@Header("Authorization") String authHeader);

    @GET("sells") // Assuming this is the endpoint for fetching the list of sells
    Call<SellListResponse> getSells();

//    @GET("connector/api/sell")  // Ensure this is the correct endpoint
//    Call<SellListResponse> getSellList(@Header("Authorization") String authHeader
//    );

    @GET("connector/api/sell")
    Call<SellListResponse> getSellList(
            @Header("Authorization") String authHeader,
            @QueryMap Map<String, String> params
    );

    @GET("connector/api/business-location")  // Ensure this is the correct endpoint
    Call<LocationResponse> getLocations(@Header("Authorization") String authHeader
    );

    @GET("connector/api/payment-methods")  // Ensure this is the correct endpoint
    Call<Map<String, String>> getMethods(@Header("Authorization") String authHeader
    );

    @GET("connector/api/product")
    Call<ProductResponse> getProducts(@Header("Authorization") String authHeader, @Query("name") String query,@Query("location_id") String location_id
    );

    @GET("connector/api/product/{id}")
    Call<ProductResponse> getProduct(@Header("Authorization") String authHeader,@Path("id") String id);

    @GET("connector/api/contactapi")
    Call<CustomerResponse> getContact(@Header("Authorization") String authHeader, @Query("mobile_num") String query
    );

    @GET("connector/api/contactapi")
    Call<CustomerResponse> getContactId(@Header("Authorization") String authHeader, @Query("id") String query
    );

//    @POST("/connector/api/sell")
//    Call<NewCustomerResponse> createContact(
//            @Header("Authorization") String authToken,
//            @Body ContactRequest contactRequest
//    );

    @POST("/connector/api/contactapi")
    Call<NewCustomerResponse> createContact(
            @Header("Authorization") String authToken,
            @Body ContactRequest contactRequest
    );

    @POST("/connector/api/expense")
    Call<ExpenseResponse> createExpense(
            @Header("Authorization") String authToken,
            @Body ExpenseRequest expenseRequest
    );

    @GET("connector/api/payment-accounts")  // Ensure this is the correct endpoint
    Call<AccountResponse> getAccounts(@Header("Authorization") String authHeader
    );


    @GET("connector/api/expense-categories")  // Ensure this is the correct endpoint
    Call<ExpenseCategoryResponse> getExpenseCategory(@Header("Authorization") String authHeader
    );


    @GET("connector/api/sell/{id}")
    Call<SellListResponse> getSellById(@Header("Authorization") String authHeader,@Path("id") String id);


    @GET("connector/api/contactapi")
    Call<CustomerResponse> getContactData(
            @Header("Authorization") String authHeader,
            @Query("type") String type,
            @Query("page") int page
    );

    @GET("connector/api/production-list")
    Call<ProductionResponse> getmanufacturedata(
            @Header("Authorization") String authHeader,
            @Query("page") int page
    );



    @GET("connector/api/expense")
    Call<ExpenseResponse> getExpenseData(
            @Header("Authorization") String authHeader,
            @QueryMap Map<String, String> params,
            @Query("page") int page
    );

    @GET("connector/api/contactapi")
    Call<CustomerResponse> getContactDataSearch(
            @Header("Authorization") String authHeader,
            @Query("type") String type,
            @Query("name") String name,
            @Query("mobile_num") String mobile_num,
            @Query("contact_id") String contact_id,
            @Query("page") int page
    );

    @POST("/connector/api/update-shipping-status")
    Call<Void> updateShippingStatus(
            @Header("Authorization") String authHeader,
            @Body UpdateShippingStatusRequest request
    );

    @POST("/connector/api/update-pathao-status")
    Call<ResponseBody> updatePathao(
            @Header("Authorization") String authHeader,
            @Body UpdatePathaoRequest request
    );



    @POST("https://api-hermes.pathao.com/aladdin/api/v1/issue-token")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getToken(@Body RequestBody body);

    @GET("https://api-hermes.pathao.com/aladdin/api/v1/stores")
    @Headers("Content-Type: application/json")
    Call<StoreResponse> getStores(@Header("Authorization") String token);

    @GET("https://api-hermes.pathao.com/aladdin/api/v1/city-list")
    Call<CityResponse> getCities(@Header("Authorization") String token);

    @GET("https://api-hermes.pathao.com/aladdin/api/v1/cities/{city_id}/zone-list")
    Call<ZoneResponse> getZones(@Header("Authorization") String token, @Path("city_id") String cityId);

    @GET("https://api-hermes.pathao.com/aladdin/api/v1/zones/{zone_id}/area-list")
    Call<AreaResponse> getAreas(@Header("Authorization") String token, @Path("zone_id") String zoneId);




}
