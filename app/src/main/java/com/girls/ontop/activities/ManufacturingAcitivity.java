package com.girls.ontop.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.adapters.AddSellAdapter;
import com.girls.ontop.adapters.CartAdapter;
import com.girls.ontop.models.BusinessLocation;
import com.girls.ontop.models.LocationResponse;
import com.girls.ontop.models.Product;
import com.girls.ontop.models.ProductModelResponse;
import com.girls.ontop.models.ProductResponse;
import com.girls.ontop.models.ProductVariation;
import com.girls.ontop.models.Variation;
import com.girls.ontop.models.VariationLocationDetail;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;
import com.google.android.gms.vision.CameraSource;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManufacturingAcitivity extends AppCompatActivity {
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








    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;



    private Spinner spnSearchBusinessLocations;
    private RecyclerView rcCartList;
    private TextView tvDue, tvDiscount, tvSubtotal;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapter, locationAdapter;

    private final ArrayList<BusinessLocation> aryListOfAllBusinessLocations = new ArrayList<>();

    private final ArrayList<ProductModelResponse.ProductModel> suggestionProductList = new ArrayList<>();

    private MutableLiveData<Integer> isDeleted = new MutableLiveData<>();
    private float subTotal = 0.0f, due = 0.0f, discount = 0.0f;

    private String token = "";
    private int resetToBusinessLocationPos = -1;
    private BusinessLocation selectedBusinessLocation = null;
    private CameraSource cameraSource;
    SurfaceView surfaceView;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturing_acitivity);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        tvSearchProducts = findViewById(R.id.tv_autoComplete);
        locationId="1";
        fetchLocations();

        cartAdapter = new CartAdapter(this, selectedProductList);
        suggestionAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,productSuggestions);


        tvSearchProducts.setOnFocusChangeListener((v, hasFocus) -> {
                tvSearchProducts.showDropDown();
        });


        tvSearchProducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Data CHnage", String.valueOf(s));
                if (s.length() > 2) { // Fetch suggestions after 3 characters
                    fetchProductSuggestions(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvSearchProducts.showDropDown();
            }
        });


        tvSearchProducts.setOnItemClickListener((parent, view, position, id) -> {
            String selectedProductWithSize = newProductsList.get(position);
            Log.d("Selected Product with size", selectedProductWithSize);

            for (Product product : aryListOfAllProducts) {
                for (ProductVariation productVariation : product.getProduct_variations()) {
                    for (Variation variation : productVariation.getVariations()) {
                        String productWithSize = product.getName() + " size : " + variation.getName();

                        if (productWithSize.equals(selectedProductWithSize)) {
                            product.setSelectedVariation(variation);
                            product.setVariationname(variation.getName());

                            if (variation.getVariationLocationDetails() != null) {
                                boolean checker = false;

                                for (VariationLocationDetail variationLocationDetail : variation.getVariationLocationDetails()) {
                                    float qtyAvailable = Float.parseFloat(variationLocationDetail.getQtyAvailable());
                                    Log.d("Quantity available", String.valueOf(qtyAvailable));

                                    if (qtyAvailable > 0) {
                                        checker = true;
                                        break;
                                    } else {
                                        checker = false;
                                    }
                                }

                                if (checker) {
                                    // Check if the product with the same variation is already in the cart
                                    boolean alreadyExists = false;

                                    for (Product selectedProduct : selectedProductList) {
                                        if (selectedProduct.getName().equals(product.getName()) &&
                                                selectedProduct.getSelectedVariation().getName().equals(variation.getName())) {
                                            alreadyExists = true;
                                            break;
                                        }
                                    }

                                    if (alreadyExists) {
                                        Toast.makeText(getApplicationContext(), "Already added to cart", Toast.LENGTH_SHORT).show();
                                    } else {
                                        selectedProductList.add(product);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Out Of Stock", Toast.LENGTH_SHORT).show();
                                }
                            }

                            break;
                        }
                    }
                }
            }

            cartAdapter.notifyDataSetChanged();
            aryListOfAllProducts.clear();
            newProductsList.clear();
            tvSearchProducts.getText().clear();
            tvSearchProducts.clearFocus();
            tvSearchProducts.dismissDropDown();
        });


    }

    private void fetchProductSuggestions(String query) {
        String accessToken = getAuthTokenFromPreferences(); // Use the actual access token
        Log.d("Access Token",accessToken);
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ProductResponse> call = apiService.getProducts("Bearer " + accessToken,query,locationId);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("ResponseData Body", String.valueOf(response.body().getData()));
                        List<Product> products = response.body().getData();
                        List<ProductVariation> allProductVariations = new ArrayList<>();
                        for (Product product : products) {
                            product.setQuantity(1);
                            product.setDiscount(0);
                            product.setDiscountType(0);


                            for (ProductVariation productVariation : product.getProduct_variations()) {
                                allProductVariations.add(productVariation);
                            }


//                            Log.d("Response Variation Data", String.valueOf(product.getProduct_variations().get(0).getVariations().get(0).getId()));
//                            product.setVariationid(product.getProduct_variations().get(0).getVariations().get(0).getId());
//                            product.setPriceInFloat(product.getProduct_variations().get(0).getVariations().get(0).getDefault_sell_price());
                            productsList.add(product.getName());
                            aryListOfAllProducts.add(product);
                        }
                        Log.d("ResponseData Product List", String.valueOf(productsList));
                        runOnUiThread(() -> setUpAutoComplete(aryListOfAllProducts));
                        // tvSearchProducts.showDropDown();
                    } else {
                        Log.d("ResponseData Error", "Code: " + response.code() + ", Message: " + response.message());
                        if (response.errorBody() != null) {
                            Log.d("ResponseData Error Body", response.errorBody().string());
                        }
                    }
                } catch (Exception e) {
                    Log.d("ResponseData Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("ResponseData Make Error",t.getMessage());
            }
        });
    }

    private void setUpAutoComplete(List<Product> products) {


        for (Product product : products) {
            for (ProductVariation productVariation : product.getProduct_variations()) {
                for (Variation variation : productVariation.getVariations()) {
                    String productWithSize = product.getName() + " size : " + variation.getName();
                    newProductsList.add(productWithSize);
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                newProductsList
        );

        Log.d("Search Product List", String.valueOf(newProductsList));
        tvSearchProducts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tvSearchProducts.showDropDown();
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