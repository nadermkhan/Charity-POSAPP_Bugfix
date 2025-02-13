package com.girls.ontop.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.adapters.CartAdapter;
import com.girls.ontop.adapters.ContactAdapter;
import com.girls.ontop.adapters.ProductionAdapter;
import com.girls.ontop.models.CustomerResponse;
import com.girls.ontop.models.LocationResponse;
import com.girls.ontop.models.Product;
import com.girls.ontop.models.ProductionData;
import com.girls.ontop.models.ProductionResponse;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManuFacturingListActivity extends AppCompatActivity {

    private Spinner spinnerLocation;
    private MaterialAutoCompleteTextView tvSearchProducts;
    List<String> locations = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    private CartAdapter cartAdapter;
    String locationId,locationname;
    ArrayAdapter<String> suggestionAdapter;
    private ArrayList<String> productSuggestions = new ArrayList<>();
    private final ArrayList<Product> selectedProductList = new ArrayList<>();
    private final ArrayList<Product> aryListOfAllProducts = new ArrayList<>();
    List<String> productsList;
    List<String> newProductsList = new ArrayList<>();
    RecyclerView manufacturinglist;
    private ProductionAdapter productionAdapter;
    private List<ProductionData> productionDataList = new ArrayList<>();
    private int currentPage = 1;
    int pagecount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manu_facturing_list);

        spinnerLocation = findViewById(R.id.spinnerLocation);
        manufacturinglist = findViewById(R.id.manufacturinglist);
        manufacturinglist.setLayoutManager(new LinearLayoutManager(this));
        productionAdapter = new ProductionAdapter(productionDataList);
        manufacturinglist.setAdapter(productionAdapter);
        Button btnNext = findViewById(R.id.btnNext);
        Button btnPrevious = findViewById(R.id.btnPrevious);

        fetcManufactureList(currentPage);
        locationId="1";
        fetchLocations();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagecount = ++currentPage;
                fetcManufactureList(++pagecount);
                if(pagecount>1){
                    btnPrevious.setEnabled(true);
                }

            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage > 1){
                    fetcManufactureList(--currentPage);
                }else{
                    btnPrevious.setEnabled(false);
                }
            }
        });



    }

    private void fetcManufactureList(int currentPage) {
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ProductionResponse> call = apiService.getmanufacturedata("Bearer " + accessToken,currentPage);


        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProductionResponse> call, Response<ProductionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productionDataList.clear();
                    productionDataList.addAll(response.body().getData());
                    productionAdapter.notifyDataSetChanged();
                    // Extract location names from the response
                }
            }

            @Override
            public void onFailure(Call<ProductionResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());

            }
        });
    }

    private void fetchLocations() {
        String accessToken = getAuthTokenFromPreferences(); // Use the actual access token
        Log.d("Access Token",accessToken);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<LocationResponse> call = apiService.getLocations("Bearer " + accessToken);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                Log.d("Working Code", String.valueOf(response.body().getData()));
                if (response.isSuccessful() && response.body() != null) {
                    // Extract location names from the response

                    locations.add("All Locations"); // Default value
                    ids.add("");

                    for (LocationResponse.Location location : response.body().getData()) {
                        locations.add(location.getName());
                        ids.add(location.getId());
                    }

                    // Set up the spinner with location names
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, locations);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLocation.setAdapter(adapter);

                    String targetValue = "GOT FACTORY";
                    int index = locations.indexOf(targetValue);
                    spinnerLocation.setSelection(index);
                    locationId = ids.get(index);
                    locationname = "GOT FACTORY";

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch locations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getAuthTokenFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("auth_token", null);
        return sharedPreferences.getString("auth_token", null);  // Retrieve the token from SharedPreferences
    }

}