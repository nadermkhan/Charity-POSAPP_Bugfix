package com.girls.ontop.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.girls.ontop.utils.AppConstants;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSellActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView tvSearchProducts;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    Spinner spinnerLocation;
    List<String> locations = new ArrayList<>();
    List<String> productsList;
    List<String> newProductsList = new ArrayList<>();
    private ArrayList<String> productSuggestions = new ArrayList<>();

    private Spinner spnSearchBusinessLocations;
    private RecyclerView rcCartList;
    private TextView tvDue, tvDiscount, tvSubtotal;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapter, locationAdapter;
    private AddSellAdapter cartAdapter;
    ArrayAdapter<String> suggestionAdapter;
    String locationId,locationname;
    List<String> ids = new ArrayList<>();

    private final ArrayList<Product> aryListOfAllProducts = new ArrayList<>();
    private final ArrayList<BusinessLocation> aryListOfAllBusinessLocations = new ArrayList<>();
    private final ArrayList<Product> selectedProductList = new ArrayList<>();

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
        setContentView(R.layout.activity_add_sell);

        setTitle("Pos Screen");


        tvSearchProducts = findViewById(R.id.tv_autoComplete);
        spnSearchBusinessLocations = findViewById(R.id.spn_business_location);
        tvDue = findViewById(R.id.tv_Due);
        tvDiscount = findViewById(R.id.tv_discount);
        tvSubtotal = findViewById(R.id.tv_subTotal);
        progressBar = findViewById(R.id.pb_cart_activity);
        rcCartList = findViewById(R.id.rv_cart_list_cart_activity);
        spinnerLocation = findViewById(R.id.spinnerLocation);

        rcCartList.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new AddSellAdapter(this, selectedProductList);
        suggestionAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,productSuggestions);

        rcCartList.setAdapter(cartAdapter);

        rcCartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSource.stop();
                surfaceView.setVisibility(View.GONE);
            }
        });




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

        locationId = "1";

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationId = ids.get(i);
                locationname = locations.get(i);
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

        // Set listeners
        tvSearchProducts.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tvSearchProducts.showDropDown();
//                startBarcodeScanner();
            }else{
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


        tvSearchProducts.setOnItemClickListener((parent, view, position, id) -> {
            String selectedProductWithSize = newProductsList.get(position);
            Log.d("AddSellActivity Selected Product with size", selectedProductWithSize);

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
            Intent intent = new Intent(AddSellActivity.this, AddSellPaymentDetailsActivity.class);
            intent.putExtra(AppConstants.subTotal, subTotal);
            intent.putExtra(AppConstants.discount, discount);
            intent.putExtra("locationId", locationId);
            intent.putExtra("locationname", locationname);
            intent.putExtra("business_name", locationname);
            intent.putParcelableArrayListExtra("productlist", selectedProductList);
            intent.putExtra(AppConstants.business_location_id, selectedBusinessLocation != null ? selectedBusinessLocation.getId() : 4);
            startActivity(intent);
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the barcode scanner
                startBarcodeScanner();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Camera permission is required to scan barcodes", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void startBarcodeScanner() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }





        // Initialize Barcode Scanner
        BarcodeScanner scanner = BarcodeScanning.getClient();

        // Create CameraSource
        cameraSource = new CameraSource.Builder(this, createBarcodeDetector())
                .setRequestedPreviewSize(1280, 1024)
                .setAutoFocusEnabled(true)
                .build();

        // Setup SurfaceView for camera preview
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(AddSellActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(holder);
                } catch (IOException e) {
                    Log.e("CameraSource", "Error starting camera source: ", e);
                    Toast.makeText(getApplicationContext(), "Error starting camera", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }



    private BarcodeDetector createBarcodeDetector() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();

      //  barcodeDetector.setProcessor(new MultiProcessor.Builder<>(new BarcodeTrackerFactory()).build());
        return barcodeDetector;
    }

    private class BarcodeTrackerFactory implements com.google.android.gms.vision.Detector.Processor<Barcode> {
        @Override
        public void release() {}

        @Override
        public void receiveDetections(com.google.android.gms.vision.Detector.Detections<Barcode> detections) {
            final SparseArray<Barcode> barcodes = detections.getDetectedItems();
            if (barcodes.size() != 0) {
                String barcodeValue = barcodes.valueAt(0).getDisplayValue();
                runOnUiThread(() -> tvSearchProducts.setText(barcodeValue));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop camera when activity is paused
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart camera when activity is resumed
        if (cameraSource != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            try {
                cameraSource.start(surfaceView.getHolder());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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

                    try {
                        String targetValue = "GOT FACTORY";
                        int index = locations.indexOf(targetValue);

                        if (index >= 0) {
                            spinnerLocation.setSelection(index);
                            locationId = ids.get(index);
                            locationname = targetValue;
                        } else {
                            //throw new Exception("Location not found: " + targetValue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        locationId = "-1";
                        locationname = "Unknown";
                    }


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
    public void updateCalculation() {
        float totalDiscount = 0.0f, subSubTotal = 0.0f;

        for (Product product : selectedProductList) {
            Log.d("Discount Value", String.valueOf((int) product.getDiscountType()));
            Log.d("Discount Value", String.valueOf(AppConstants.DiscountType.PERCENTAGE.ordinal()));
            float productTotal = product.getPriceInFloat() * product.getQuantity();
            float discountAmount = (int) product.getDiscountType() == AppConstants.DiscountType.PERCENTAGE.ordinal() ?
                    productTotal * product.getDiscount() / 100 : product.getDiscount();
            subSubTotal += productTotal;
            totalDiscount += discountAmount*product.getQuantity();
        }

        discount = totalDiscount;
        due = subSubTotal - totalDiscount;
        subTotal = subSubTotal;

        tvDiscount.setText(discount + " Taka");
        tvDue.setText(due + " Taka");
        tvSubtotal.setText(subTotal + " Taka");
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

    public void onIncrement(int position) {
        Product product = selectedProductList.get(position);
        if(product.getStock() >= product.getQuantity()+1) {
            product.setQuantity(product.getQuantity() + 1);
            updateCalculation();
            cartAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(getApplicationContext(),"Requested quantity not available",Toast.LENGTH_SHORT).show();
        }
    }

    public void onDecrement(int position) {
        Product product = selectedProductList.get(position);
        if (product.getQuantity() > 1) {
            product.setQuantity(product.getQuantity() - 1);
        }else{
            selectedProductList.remove(position);
        }
        updateCalculation();
        cartAdapter.notifyDataSetChanged();
    }
}