package com.girls.ontop.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.adapters.CartAdapter;
import com.girls.ontop.adapters.CartEditAdapter;
import com.girls.ontop.models.BusinessLocation;
import com.girls.ontop.models.LocationResponse;
import com.girls.ontop.models.Product;
import com.girls.ontop.models.ProductModelResponse;
import com.girls.ontop.models.ProductResponse;
import com.girls.ontop.models.ProductVariation;
import com.girls.ontop.models.Sell;
import com.girls.ontop.models.SellLineModel;
import com.girls.ontop.models.SellListResponse;
import com.girls.ontop.models.Variation;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;
import com.girls.ontop.utils.AppConstants;
import com.girls.ontop.utils.iAdapterListeners;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PosEditActivity extends AppCompatActivity implements iAdapterListeners {
    private MaterialAutoCompleteTextView tvSearchProducts;
    List<String> productsList;
    List<Sell> invoiceselldata;
    List<String> locations = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    Spinner spinnerLocation;
    List<String> newProductsList = new ArrayList<>();
    private ArrayList<String> productSuggestions = new ArrayList<>();

    private Spinner spnSearchBusinessLocations;
    private RecyclerView rcCartList;
    private TextView tvDue, tvDiscount, tvSubtotal;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapter, locationAdapter;
    private CartEditAdapter cartAdapter;
    ArrayAdapter<String> suggestionAdapter;

    private final ArrayList<Product> aryListOfAllProducts = new ArrayList<>();
    private final ArrayList<BusinessLocation> aryListOfAllBusinessLocations = new ArrayList<>();
    private final ArrayList<Product> selectedProductList = new ArrayList<>();

    private final ArrayList<ProductModelResponse.ProductModel> suggestionProductList = new ArrayList<>();

    private MutableLiveData<Integer> isDeleted = new MutableLiveData<>();
    private float subTotal = 0.0f, due = 0.0f, discount = 0.0f;

    private String token = "",locationname;
    private String intent_invoice_id ;
    int location_postion;
    private int resetToBusinessLocationPos = -1;
    String locationId;
    private BusinessLocation selectedBusinessLocation = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_edit);

        setTitle("Pos Edit Screen");


        tvSearchProducts = findViewById(R.id.tv_autoComplete);
        spnSearchBusinessLocations = findViewById(R.id.spn_business_location);
        tvDue = findViewById(R.id.tv_Due);
        tvDiscount = findViewById(R.id.tv_discount);
        tvSubtotal = findViewById(R.id.tv_subTotal);
        progressBar = findViewById(R.id.pb_cart_activity);
        rcCartList = findViewById(R.id.rv_cart_list_cart_activity);

        rcCartList.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartEditAdapter(this, selectedProductList);
        suggestionAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,productSuggestions);
        spinnerLocation = findViewById(R.id.spinnerLocation);

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationId = ids.get(i);
                locationname = locations.get(i);
                location_postion = i;
                selectedProductList.clear();
                updateCalculation();
                cartAdapter.notifyDataSetChanged();
                Log.d("Location ID", locationId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Optional: Handle the case where nothing is selected
            }
        });


        fetchLocations();

        Intent intent = getIntent();
        intent_invoice_id= intent.getStringExtra("invoice_id");
        fetchinvoice(intent_invoice_id);
        Log.d("invoice_id",intent_invoice_id);

        rcCartList.setAdapter(cartAdapter);
//
        /**/
        productsList = new ArrayList<>();
        /**/

        // Observing deletion events
        isDeleted.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer pos) {
                if (pos >= 0) {
                    selectedProductList.remove(pos.intValue());
                    updateCalculation();
                    cartAdapter.notifyDataSetChanged();
                }
            }
        });

        // Set listeners
        tvSearchProducts.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tvSearchProducts.showDropDown();
            }
        });

        tvSearchProducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) { // Fetch suggestions after 3 characters
                    fetchProductSuggestions(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvSearchProducts.showDropDown();
            }
        });

//        tvSearchProducts.setOnItemClickListener((parent, view, position, id) -> {
//            String selectedProductName = productsList.get(position);
//            // Find the product object from your fetched product list
//            for (Product product : aryListOfAllProducts) {
//                if (product.getName().equals(selectedProductName)) {
//                    selectedProductList.add(product);
//                    break;
//                }
//            }
//            cartAdapter.notifyDataSetChanged();
//            tvSearchProducts.getText().clear();
//        });



        tvSearchProducts.setOnItemClickListener((parent, view, position, id) -> {
            String selectedProductWithSize = newProductsList.get(position);
            Log.d("Selected Product with size",selectedProductWithSize);
            for (Product product : aryListOfAllProducts) {
                for (ProductVariation productVariation : product.getProduct_variations()) {
                    for (Variation variation : productVariation.getVariations()) {
                        String productWithSize = product.getName() + " size : " + variation.getName();
                        if (productWithSize.equals(selectedProductWithSize)) {
                            product.setSelectedVariation(variation);
                            selectedProductList.add(product);
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


        spnSearchBusinessLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBusinessLocation = aryListOfAllBusinessLocations.get(position);
                resetToBusinessLocationPos = 0;
                selectedProductList.clear();
                updateCalculation();
                cartAdapter.notifyDataSetChanged();
                // Reload products
                // Fetch and update products based on selected business location
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        findViewById(R.id.btn_card_checkout).setOnClickListener(v -> {
            Intent editintent = new Intent(PosEditActivity.this, PaymentDetailEditActivity.class);
            editintent.putExtra(AppConstants.subTotal, due);
            editintent.putExtra("invoice_id", intent_invoice_id);
            editintent.putExtra(AppConstants.discount, discount);
            editintent.putExtra("locationId", locationId);
            editintent.putExtra("locationname", locationname);
            editintent.putExtra("business_name", locationname);
            editintent.putParcelableArrayListExtra("productlist", selectedProductList);
            editintent.putExtra(AppConstants.business_location_id, selectedBusinessLocation != null ? selectedBusinessLocation.getId() : 4);
            startActivity(editintent);
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
        tvSearchProducts.showDropDown();
    }


    private void fetchinvoice(String invoice_id) {
        String accessToken = getAuthTokenFromPreferences(); // Use the actual access token
        Log.d("Access Token",accessToken);
        Log.d("Invoice_id",invoice_id);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<SellListResponse> call = apiService.getSellById("Bearer " + accessToken, String.valueOf(invoice_id));

        call.enqueue(new Callback<SellListResponse>() {
            @Override
            public void onResponse(Call<SellListResponse> call, Response<SellListResponse> response) {
                SellListResponse sellresponse = response.body();
                Log.d("Sell response Full", String.valueOf(sellresponse));
                invoiceselldata = sellresponse.getSells();
                for (Sell sell : invoiceselldata) {
                    List<SellLineModel> sellsproductdata = sell.getSell_lines();
                    for (SellLineModel product : sellsproductdata) {

                        Log.d("Product Response", String.valueOf(product.getProduct_id()));
                        fetchInvoiceProduct(product,product.getVariation_id(),product.getLine_discount_amount());
                    }

                    locationId = sell.getLocation_id();
                    int position = ids.indexOf(locationId);

                    if (position != -1) {
                        spinnerLocation.setSelection(position);
                        location_postion = position;
                        locationname = locations.get(position);
                    }
                }

            }

            @Override
            public void onFailure(Call<SellListResponse> call, Throwable t) {
                // Handle failure (e.g., no internet, server error, etc.)
//                Log.e(TAG, "Error fetching sell list: " + t.getMessage());

            }
        });
    }


    private String getAuthTokenFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("auth_token", null);
        return sharedPreferences.getString("auth_token", null);  // Retrieve the token from SharedPreferences
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
                        Log.d("Response Body", String.valueOf(response.body().getData()));
                        productsList.clear();
                        aryListOfAllProducts.clear();
                        newProductsList.clear();
                        List<Product> products = response.body().getData();
                        List<ProductVariation> allProductVariations = new ArrayList<>();
                        for (Product product : products) {
                            product.setQuantity(1);
                            product.setDiscount(0);
                            product.setDiscountType(0);


                            for (ProductVariation productVariation : product.getProduct_variations()) {
                                allProductVariations.add(productVariation);
                            }


                            Log.d("Variation Data", String.valueOf(product.getProduct_variations().get(0).getVariations().get(0).getId()));
                            product.setVariationid(product.getProduct_variations().get(0).getVariations().get(0).getId());
                            product.setPriceInFloat(product.getProduct_variations().get(0).getVariations().get(0).getDefault_sell_price());
                            productsList.add(product.getName());
                            aryListOfAllProducts.add(product);
                        }
                        Log.d("Product List", String.valueOf(productsList));
                        runOnUiThread(() -> setUpAutoComplete(aryListOfAllProducts));
                        updateCalculation();
                        findViewById(R.id.btn_card_checkout).setEnabled(true);
                        // tvSearchProducts.showDropDown();
                    } else {
                        Log.d("Response Error", "Code: " + response.code() + ", Message: " + response.message());
                        if (response.errorBody() != null) {
                            Log.d("Error Body", response.errorBody().string());
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());
            }
        });
    }

    private void fetchInvoiceProduct(SellLineModel product_data, int variation_id,float discount) {
        String accessToken = getAuthTokenFromPreferences(); // Use the actual access token
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ProductResponse> call = apiService.getProduct("Bearer " + accessToken, String.valueOf(product_data.getProduct_id()));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("Response Body", String.valueOf(response.body().getData()));
                        productsList.clear();
                        aryListOfAllProducts.clear();
                        newProductsList.clear();
                        List<Product> products = response.body().getData();
                        List<ProductVariation> allProductVariations = new ArrayList<>();
                        for (Product product : products) {
                            product.setQuantity(product_data.getQuantity());
                            product.setDiscount(discount);
                            product.setDiscountType(1);

                            Log.d("Last Product", String.valueOf(product.getName()));

                            for (ProductVariation productVariation : product.getProduct_variations()) {
                                List<Variation> variations = productVariation.getVariations();
                                for (Variation variation : variations) {
                                if (variation.getId()== product_data.getVariation_id()) {
                                        product.setSelectedVariation(variation);
                                        product.setStock(Float.parseFloat(variation.getVariationLocationDetails().get(0).getQtyAvailable()));
                                    }
                                }
                            }



                            Log.d("Variation Data", String.valueOf(product.getName()));
                            product.setVariationid(product_data.getVariation_id());
                            product.setPriceInFloat(product_data.getUnit_price());

                            selectedProductList.add(product);
                            productsList.add(product.getName());
                        }
                        Log.d("Product List", String.valueOf(selectedProductList.get(0).getName()));
                        updateCalculation();
                        cartAdapter.notifyDataSetChanged();
                        findViewById(R.id.btn_card_checkout).setEnabled(true);
                        // tvSearchProducts.showDropDown();
                    } else {
                        Log.d("Response Error", "Code: " + response.code() + ", Message: " + response.message());
                        if (response.errorBody() != null) {
                            Log.d("Error Body", response.errorBody().string());
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());
            }
        });
    }
    public void updateCalculation() {
        float totalDiscount = 0.0f, subSubTotal = 0.0f;

        for (Product product : selectedProductList) {
            float productTotal = product.getPriceInFloat()*product.getQuantity();
            Log.d("Price In Float", String.valueOf(productTotal));
            float discountAmount = product.getDiscountType() == AppConstants.DiscountType.PERCENTAGE.ordinal() ?
                    productTotal * product.getDiscount() / 100 : product.getDiscount();
            subSubTotal += productTotal;
            totalDiscount += discountAmount*product.getQuantity();
        }

        discount = totalDiscount;
        due = subSubTotal;
        subTotal = subSubTotal+discount;

        tvDiscount.setText(discount + " Taka");
        tvDue.setText(due + " Taka");
        tvSubtotal.setText(subTotal + " Taka");
    }

    @Override
    public void onIncrement(int pos) {
        Product product = selectedProductList.get(pos);
        if( product.getStock() >= product.getQuantity()+1){
            product.setQuantity(product.getQuantity() + 1);
            updateCalculation();
            cartAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(getApplicationContext(),"Requested Stock not avaialble",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDecrement(int pos) {
        Product product = selectedProductList.get(pos);
        if (product.getQuantity() > 1) {
            product.setQuantity(product.getQuantity() - 1);
        }else{
            selectedProductList.remove(pos);
        }
        updateCalculation();
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateDue(Float due) {

    }

    @Override
    public void updateDiscount(Float totalDiscount) {

    }

    @Override
    public void updateSubTotal(Float let) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_reset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.rv_cart_list_cart_activity) {
            selectedProductList.clear();
            updateCalculation();
            cartAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
