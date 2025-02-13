package com.girls.ontop.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellListViewModel extends ViewModel {

    private MutableLiveData<List<Sell>> sellListLiveData = new MutableLiveData<>();
    private static final String TAG = "SellListViewModel";
    private Context context;  // Context reference

    // Constructor with context
    public SellListViewModel(Context context) {
        this.context = context;
    }

    // This exposes the LiveData to observe from the UI (Activity/Fragment)
    public LiveData<List<Sell>> getSellListLiveData() {
        return sellListLiveData;
    }

    // Function to fetch sell list from the API
    public void fetchSellList(String locationId, String contactId,String start_date,String end_date,int pagenumber,int is_direct_sale,String search) {
        // Get the saved token from SharedPreferences
        String authToken = getAuthTokenFromPreferences();


        if (authToken == null) {
            // If the token is not available, handle the error or redirect to login
            Log.e(TAG, "Auth token not found");
            sellListLiveData.setValue(null); // Optionally handle the error
            return;
        }

        // Get the ApiService instance from Retrofit
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // Create the query parameters
        Map<String, String> params = new HashMap<>();
        params.put("location_id", locationId);
//        params.put("contact_id", contactId);
        params.put("status", "final");
//        params.put("payment_status", "due,partial");
        params.put("start_date", start_date);
        params.put("end_date", end_date);
        params.put("search", search);
//        params.put("user_id", "quis");
//        params.put("service_staff_id", "ut");
//        params.put("shipping_status", "ordered");
//        params.put("source", "eos");
//        params.put("only_subscriptions", "dolor");
//        params.put("send_purchase_details", "quo");
        params.put("order_by_date", "desc");
        params.put("is_direct_sale", String.valueOf(is_direct_sale));
        params.put("per_page", "10");
        params.put("page", String.valueOf(pagenumber));
        // Make the API call with the Bearer token in the header
//        Call<SellListResponse> call = apiService.getSellList("Bearer " + authToken);
        Call<SellListResponse> call = apiService.getSellList("Bearer " + authToken, params);

        // Enqueue the call (asynchronous request)
        call.enqueue(new Callback<SellListResponse>() {
            @Override
            public void onResponse(Call<SellListResponse> call, Response<SellListResponse> response) {
                SellListResponse sellresponse = response.body();
                List<Sell> selldata = sellresponse.getSells();
                Log.d("Sell response", String.valueOf(selldata));

                if (response.isSuccessful() && response.body() != null) {
                    // If the response is successful, set the value to LiveData

                    sellListLiveData.setValue(response.body().getSells());
                } else {
                    // Handle unsuccessful response
                    Log.e(TAG, "Failed to fetch sell list: " + response.message());
                    sellListLiveData.setValue(null); // Optionally handle the error
                }
            }

            @Override
            public void onFailure(Call<SellListResponse> call, Throwable t) {
                // Handle failure (e.g., no internet, server error, etc.)
                Log.e(TAG, "Error fetching sell list: " + t.getMessage());
                sellListLiveData.setValue(null); // Optionally handle the error
            }
        });
    }

    // Function to retrieve the saved auth token from SharedPreferences
    private String getAuthTokenFromPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null);  // Retrieve the token from SharedPreferences
    }
}
