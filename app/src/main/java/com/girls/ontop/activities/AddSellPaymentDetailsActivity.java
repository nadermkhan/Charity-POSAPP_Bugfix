package com.girls.ontop.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.girls.ontop.R;
import com.girls.ontop.adapters.SearchableSpinnerAdapter;
import com.girls.ontop.models.AccountResponse;
import com.girls.ontop.models.AreaResponse;
import com.girls.ontop.models.CityResponse;
import com.girls.ontop.models.ContactRequest;
import com.girls.ontop.models.CustomerResponse;
import com.girls.ontop.models.NewCustomerResponse;
import com.girls.ontop.models.OrderPayment;
import com.girls.ontop.models.OrderProduct;
import com.girls.ontop.models.OrderRequest;
import com.girls.ontop.models.Product;
import com.girls.ontop.models.SellsObject;
import com.girls.ontop.models.StoreResponse;
import com.girls.ontop.models.ZoneResponse;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;
import com.girls.ontop.utils.AppConstants;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSellPaymentDetailsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    List<String> cityIdData = new ArrayList<>();
    List<String> accountList = new ArrayList<>();
    List<AccountResponse.Accounts> accountListData = new ArrayList<>();
    List<CityResponse.City> cityData = new ArrayList<>();
    float totalAmount=0,discountValue = 0,shippingCharge = 0,advancePayment = 0,totalDue = totalAmount;

    private AlertDialog alertDialog;
    private AutoCompleteTextView tvAutoPhone, tvSelectDistrict, tvSelectArea, tvSelectZone;
    private MaterialAutoCompleteTextView etPaymentAccount,et_hierarchy,spnCity,spnZone,spnArea;
    private EditText etName, etNotes, etAddress,lv_carts, etShippingCharge, etAdvanceAmount, etDiscount,et_cart_total_discount, etTotalDiscount,et_payment_exp1_value;
    private Button btnPaymentConfirm;
    private Spinner spnDisCountType,spnPathaoStoreLocation;
    private float due,subTotal,discount,businessLocationID,expenseamount;
    private String businessName;
    String selectedItem,selectedStoreId,selectedCityId,selectedZoneId,selectedAreaId,city_name,zone_name,area_name,pathaoaccessstoken;
    private ArrayList<Product> selectedProductList = new ArrayList<>();
    public int contact_id=0,payment_account;
    private float shipping_charges;
    String locationId;
    List<OrderProduct> orderproducts = new ArrayList<>();
    List<OrderPayment> orderPayments = new ArrayList<>();
    private TextView tvDue, tvDisplayOtherExpance,tv_shpping_150,tv_shpping_80,tv_advance_500,tv_advance_200;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sell_payment_details);

        setTitle("Payment");

        // Initialize UI components using findViewById
        progressBar = new ProgressBar(this);
        tvAutoPhone = findViewById(R.id.auto_customer_phone_number);
        etName = findViewById(R.id.et_payment_full_name);
        etNotes = findViewById(R.id.et_payment_note);
        etAddress = findViewById(R.id.et_payment_address);
        etShippingCharge = findViewById(R.id.et_payment_shipping_charge);
        etAdvanceAmount = findViewById(R.id.et_payment_advance);
        etDiscount = findViewById(R.id.et_payment_discount);
        etTotalDiscount = findViewById(R.id.et_payment_total_discount);
        tvDue = findViewById(R.id.tota_due);
        btnPaymentConfirm = findViewById(R.id.btnPaymentConfirm);
        spnDisCountType = findViewById(R.id.spn_discount_type);
        etPaymentAccount = findViewById(R.id.et_payment_account);
//        tvSelectDistrict = findViewById(R.id.et_payment_district);
//        tvSelectArea = findViewById(R.id.et_payment_sub_district);
//        tvSelectZone = findViewById(R.id.et_zone);
        tv_shpping_150 = findViewById(R.id.tv_shpping_150);
        tv_shpping_80 = findViewById(R.id.tv_shpping_80);
        tv_advance_200 = findViewById(R.id.tv_advance_200);
        tv_advance_500 = findViewById(R.id.tv_advance_500);
        et_payment_exp1_value = findViewById(R.id.et_payment_exp1_value);
        et_cart_total_discount = findViewById(R.id.et_cart_total_discount);
        lv_carts = findViewById(R.id.lv_carts);
        etTotalDiscount.setText("");

        spnDisCountType.setSelection(1);


        spnPathaoStoreLocation = findViewById(R.id.spn_pathao_store_location);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_hierarchy_selector, null);
        spnCity = findViewById(R.id.spinner_district);
        spnZone = findViewById(R.id.spinner_zone);
        spnArea = findViewById(R.id.spinner_area);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(dialogView);

        fetchTokenAndInitializeSpinners();


        spnDisCountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedItem = adapterView.getItemAtPosition(position).toString().toLowerCase();
                ;
                // You can call your calculation methods here or update UI accordingly
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case when no item is selected, if needed
            }
        });

        et_payment_exp1_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etShippingCharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etAdvanceAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTotalDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculation();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        fetchAccountsFromApi();

        // Alert dialog setup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false).setView(progressBar);
        alertDialog = builder.create();

        // Set up event listeners
        setupEventListeners();
        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();
        if (extras != null) {
            due = extras.getFloat(AppConstants.due, 0.0f);
            Log.d("Due", String.valueOf(due));
            //tvDue.setText((int) due);
            subTotal = extras.getFloat(AppConstants.subTotal, 0.0f);
            locationId = extras.getString("locationId", "1");
            totalAmount = subTotal;
            totalDue = due;
            Log.d("Due", String.valueOf(subTotal));
            tvDue.setText(String.valueOf(subTotal));
            if (intent != null && intent.hasExtra("productlist")) {
                selectedProductList = intent.getParcelableArrayListExtra("productlist");
            }
            String cart_string = "";
            if (selectedProductList != null) {
                for (Product product : selectedProductList) {
                    String discount_type;
                    if (product.getDiscountType() == AppConstants.DiscountType.PERCENTAGE.ordinal()) {
                        discount_type = "percentage";
                    } else {
                        discount_type = "fixed";
                    }
                    orderproducts.add(new OrderProduct(product.getId(), product.getQuantity(), product.getPriceInFloat(), product.getVariationid(), discount_type, product.getDiscount()));
                    cart_string = cart_string + "Name: " + product.getName() + ", Size: " + product.getVariationname() + ", Qty: " + product.getQuantity() + "\n"+", Price: " + (product.getPriceInFloat() - product.getDiscount()) + "\n";
                }
            }

            Log.d("MY CART", String.valueOf(selectedProductList));
            lv_carts.setText(cart_string);

            discount = extras.getFloat(AppConstants.discount, 0);
            et_cart_total_discount.setText(String.valueOf(discount));
            businessLocationID = extras.getInt(AppConstants.business_location_id, -1);
            businessName = extras.getString(AppConstants.business_name, "");
            TextView tvLocationBusiness = findViewById(R.id.tv_location_business);
            tvLocationBusiness.setText(businessName);
            calculation();
        }
    }

    private void calculation(){
        String selectedAccount = etPaymentAccount.getText().toString();
        int accountId = -1;
        for (AccountResponse.Accounts account : accountListData) {
            Log.d("data name", selectedAccount);
            Log.d("data", String.valueOf(account.getId()));
            if (account.getName().equals(selectedAccount)) {
                accountId = account.getId();
                break;
            }
        }

        orderPayments.clear();
        totalDue = totalAmount;
        expenseamount = 0;
        if(!et_payment_exp1_value.getText().toString().isEmpty()){
            expenseamount = Float.parseFloat(et_payment_exp1_value.getText().toString());
        }
        discountValue = 0;
        String discountInput = etTotalDiscount.getText().toString();
        String shippingChargeInput = etShippingCharge.getText().toString();
        String advancePaymentInput = etAdvanceAmount.getText().toString();
        Log.d("Payment Account", String.valueOf(accountId));
        orderPayments.add(new OrderPayment(advancePaymentInput,accountId,"custom_pay_6"));
        if (!discountInput.isEmpty()) {
            discountValue = Float.parseFloat(discountInput);
        }
        if ("Percentage".equalsIgnoreCase(selectedItem)) {
            totalDue = totalAmount - ((discountValue / 100) * totalAmount);
        }else if ("Fixed".equalsIgnoreCase(selectedItem)) {
            totalDue = totalAmount - discountValue;
        }
        if (!shippingChargeInput.isEmpty()) {
            shippingCharge = Float.parseFloat(shippingChargeInput);
            totalDue += shippingCharge;
        }
        if (!advancePaymentInput.isEmpty()) {
            advancePayment = Float.parseFloat(advancePaymentInput);
            totalDue -= advancePayment;
        }
        totalDue -= discount;
        totalDue += expenseamount;
        tvDue.setText(String.valueOf(totalDue));
    }

    private void setUpAutoComplete(List<String> accountList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                accountList
        );
        etPaymentAccount.setAdapter(adapter);
    }


    private void fetchAccountsFromApi() {
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<AccountResponse> call = apiService.getAccounts("Bearer " + accessToken);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("Response Body", String.valueOf(response.body().getData()));
                        List<AccountResponse.Accounts> accounts = response.body().getData();
                        for (AccountResponse.Accounts account : accounts) {
                            accountList.add(account.getName());
                            accountListData.add(account);
                        }
                        runOnUiThread(() -> setUpAutoComplete(accountList));

                        etPaymentAccount.setText(accountList.get(3), false);

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
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());
            }
        });
    }

    private void setupEventListeners() {

        etPaymentAccount.setOnFocusChangeListener((view, hasFocus) -> {
            etPaymentAccount.showDropDown();
            if (hasFocus) {
                etPaymentAccount.showDropDown();
            }
        });

        spnCity.setOnFocusChangeListener((view, hasFocus) -> {
            spnCity.showDropDown();
            if (hasFocus) {
                spnCity.showDropDown();
            }
        });

        spnZone.setOnFocusChangeListener((view, hasFocus) -> {
            spnZone.showDropDown();
            if (hasFocus) {
                spnZone.showDropDown();
            }
        });

        spnArea.setOnFocusChangeListener((view, hasFocus) -> {
            spnArea.showDropDown();
            if (hasFocus) {
                spnArea.showDropDown();
            }
        });



//        spnCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });



        etPaymentAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                payment_account = accountListData.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tv_shpping_150.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etShippingCharge.setText("150");
                shipping_charges = 150;
                calculation();
            }
        });
        tv_shpping_80.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etShippingCharge.setText("80");
                shipping_charges = 80;
                calculation();
            }
        });
        tv_advance_200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAdvanceAmount.setText("150");
                calculation();
            }
        });
        tv_advance_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAdvanceAmount.setText("200");
                calculation();
            }
        });
//        tvAutoPhone.setOnFocusChangeListener((view, hasFocus) -> {
//            if (!hasFocus) {
//                String phone = tvAutoPhone.getText().toString();
//                fetchcustomerdata(phone);
//                if (phone.isEmpty() || !isValidPhoneNumber(phone)) {
//                    tvAutoPhone.setError("Enter a valid number");
//                }
//            } else {
//                tvAutoPhone.setError(null);
//            }
//        });

        tvAutoPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String phone = charSequence.toString();
                if (phone.length() > 10) {
                    fetchcustomerdata(phone);
                }else{
                    etName.setText("");
                    etAddress.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnPaymentConfirm.setOnClickListener(view -> {
            String phoneNumber = tvAutoPhone.getText().toString();
            String name = etName.getText().toString();
            String address = etAddress.getText().toString();

            if (!phoneNumber.isEmpty()) {
                if(contact_id==0) {
                    createcontact(phoneNumber, name, address);
                }else{
                    submitorder();
                }
            }else{
                submitorder();
            }
            Toast.makeText(this, "Order Placed", Toast.LENGTH_SHORT).show();
        });
    }

    private void createcontact(String phoneNumber,String name,String address){
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        ContactRequest contactRequest = new ContactRequest(
                "customer",
                "",
                name,
                phoneNumber,
                address,
                address,
                "individual",
                "1"
        );

        Call<NewCustomerResponse> call = apiService.createContact("Bearer " + accessToken, contactRequest);
        call.enqueue(new Callback<NewCustomerResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewCustomerResponse> call, Response<NewCustomerResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        NewCustomerResponse responseBody = response.body();
                        if (responseBody != null) {
                            NewCustomerResponse.Data customer = responseBody.getData();
                            int contactId = customer.getId();
                            Log.d("Contact ID", String.valueOf(contactId));
                            contact_id = contactId;
                            submitorder();
                        } else {
                            Log.e("Response", "Response body is null");
                        }
                    }  else {
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
            public void onFailure(Call<NewCustomerResponse> call, Throwable t) {
                Log.d("Make Error", t.getMessage());
            }
        });



    }
    private void submitorder() {
        Log.d("Submit Order","Submit Order");
        calculation();
        String temp_contact_id;
        String accessToken = getAuthTokenFromPreferences(); // Fetch the saved token
        Log.d("ACCESS TOKEN",accessToken);
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        if(contact_id==0){
            temp_contact_id = "1";
        }else{
            temp_contact_id = String.valueOf(contact_id);
        }
        OrderRequest orderRequest = new OrderRequest(
                locationId,
                temp_contact_id,
                etAddress.getText().toString(),
                etNotes.getText().toString(),
                shipping_charges,
                orderproducts,
                etTotalDiscount.getText().toString(),
                selectedItem,
                orderPayments,
                expenseamount,
                1,
                "ordered",
                "fixed",
                selectedAreaId,
                selectedCityId,
                selectedZoneId
        );

        SellsObject sellsObject = new SellsObject(orderRequest);

        // Make the API call
        Call<ResponseBody> call = apiService.submitOrder("Bearer " + accessToken, sellsObject);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        // Log or show the raw response
                        String rawResponse = response.body().string();
                        Log.d("OrderResponse Response", rawResponse);
                        JSONArray jsonResponse = new JSONArray(rawResponse);
                        JSONObject firstElement = jsonResponse.getJSONObject(0);
                        String invoiceUrl = firstElement.getString("invoice_url");
                        CheckBox cbPrintRecit;
                        cbPrintRecit = findViewById(R.id.cb_print_recit);
                        if (cbPrintRecit.isChecked()) {
                            // Open the invoice URL in the browser
                            Intent dashboardIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivity(dashboardIntent);

                            // Delay the transition to the DashboardActivity by 2 seconds
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Navigate to the DashboardActivity after the delay
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(invoiceUrl));
                                    startActivity(browserIntent);
                                }
                            }, 500); // 2000 milliseconds = 2 seconds delay
                        } else {
                            // Navigate directly to the DashboardActivity if the checkbox is not checked
                            String pathaotrx = firstElement.optString("pathaotrx",null);

                            if (pathaotrx != null) {
                                Log.d("Pathao URL",pathaotrx);
                                showPathaoDialog(pathaotrx);
                            } else {
                                Intent dashboardIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(dashboardIntent);
                            }
                        }

                    } else {
                        Log.d("OrderResponse Error", "Code: " + response.code() + ", Message: " + response.message());
                        if (response.errorBody() != null) {
                            Log.d("Error Body", response.errorBody().string());
                        }
                    }
                } catch (Exception e) {
                    Log.d("OrderResponse Exception", String.valueOf(e));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OrderResponse Body Order", t.getMessage());
            }
        });
    }

    private void showPathaoDialog(String pathaotrx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Transaction ID");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        // Transaction ID TextView
        TextView textView = new TextView(this);
        textView.setText(pathaotrx);
        textView.setTextSize(18);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextColor(Color.BLACK); // Ensure text is visible

        // Copy Button (Green)
        Button copyButton = new Button(this);
        copyButton.setText("Copy");
        copyButton.setPadding(10, 10, 10, 10);
        copyButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip;
        copyButton.setOnClickListener(v -> {
            ClipData clipdata = ClipData.newPlainText("Transaction ID", pathaotrx);
            clipboard.setPrimaryClip(clipdata);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });


        clip = ClipData.newPlainText("Transaction ID", pathaotrx);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();

        // Close Button (Red)
        Button closeButton = new Button(this);
        closeButton.setText("Close");
        closeButton.setPadding(10, 10, 10, 10);
        closeButton.setBackgroundColor(Color.RED); // Set red background
        closeButton.setTextColor(Color.WHITE); // White text for contrast
        closeButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        AlertDialog dialog = builder.create();
        closeButton.setOnClickListener(v -> {
            dialog.dismiss();
            Intent dashboardIntent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(dashboardIntent);
        });

        // Add views to layout
        layout.addView(textView);
        layout.addView(copyButton);
        layout.addView(closeButton);

        // Set layout to AlertDialog
        builder.setView(layout);

        // Show the dialog
        builder.show();
    }



    /*Pathao Code*/
    private void fetchTokenAndInitializeSpinners() {
        // Fetch token
        String body = "{\"client_id\": \"YqaQW7ldnj\", \"client_secret\": \"2ymeUJ1T2B5YKNlTq459wEyuknHIeQpSJBERqwaT\", \"grant_type\": \"password\", \"username\": \"amitashley46@gmail.com\", \"password\": \"H7TPR9Uqt\"}";
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
        Call<ResponseBody> tokenCall = apiService.getToken(requestBody);

        tokenCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseString);
                        String accessToken = jsonResponse.getString("access_token");
                        pathaoaccessstoken = accessToken;
                        fetchStores(accessToken);
                    } catch (Exception e) {
                        Log.e("", e.getMessage());
                    }
                } else {
                    Log.e("Token Response Error", String.valueOf(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Token Failure", t.getMessage());
            }
        });
    }

    private void fetchStores(String accessToken) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Log.d("Pathao Token",accessToken);
        Call<StoreResponse> storeCall = apiService.getStores("Bearer " + accessToken);

        storeCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<StoreResponse> call, Response<StoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<StoreResponse.Store> stores = response.body().getData().getData();
                    List<String> storeNames = new ArrayList<>();
                    for (StoreResponse.Store store : stores) {
                        Log.d("Store_name",store.getStoreName());
                        storeNames.add(store.getStoreName());
                    }
                    ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(AddSellPaymentDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, storeNames);
                    spnPathaoStoreLocation.setAdapter(storeAdapter);
                    storeAdapter.notifyDataSetChanged();
                    spnPathaoStoreLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedStoreId = stores.get(position).getStoreId();
                            fetchCities(accessToken);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                } else {
                    Log.e("Store Response Error", String.valueOf(response));
                }
            }

            @Override
            public void onFailure(Call<StoreResponse> call, Throwable t) {
                Log.e("Store Failure", t.getMessage());
            }
        });
    }

//    private void fetchCities(String accessToken) {
//        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
//        Call<CityResponse> cityCall = apiService.getCities("Bearer " + accessToken);
//
//        cityCall.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<CityResponse.City> cities = response.body().getData().getData();
//                    List<String> cityNames = new ArrayList<>();
//                    for (CityResponse.City city : cities) {
//                        cityNames.add(city.getCityName());
//                    }
//                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(AddSellPaymentDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, cityNames);
//                    spnCity.setAdapter(cityAdapter);
//
//                    spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            selectedCityId = String.valueOf(cities.get(position).getCityId());
//                            city_name = cities.get(position).getCityName();
//                            et_hierarchy.setText(city_name+"->"+zone_name+"->"+area_name);
//                            fetchZones(accessToken);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {}
//                    });
//                } else {
//                    Log.e("City Response Error", response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CityResponse> call, Throwable t) {
//                Log.e("City Failure", t.getMessage());
//            }
//        });
//    }


    private void fetchCities(String accessToken) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<CityResponse> cityCall = apiService.getCities("Bearer " + accessToken);

        cityCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cityData = response.body().getData().getData();
                    List<String> cityNames = new ArrayList<>();
                    for (CityResponse.City city : cityData) {
                        cityIdData.add(String.valueOf(city.getCityId()));
                        cityNames.add(city.getCityName());
                    }

                    runOnUiThread(() -> setUpAutoCompleteCity(cityData));





//                    spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            selectedCityId = String.valueOf(cities.get(position).getCityId());
//                            city_name = cities.get(position).getCityName();
//                            et_hierarchy.setText(city_name + "->" + zone_name + "->" + area_name);
//                            fetchZones(accessToken);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {}
//                    });
                } else {
                    Log.e("City Response Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Log.e("City Failure", t.getMessage());
            }
        });
    }

//    private void setUpAutoCompleteCity(List<String> cityNames) {
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                    this,
//                    android.R.layout.simple_dropdown_item_1line,
//                    cityNames
//            );
//            spnCity.setAdapter(adapter);
//    }

    // Set up MaterialAutoCompleteTextView Adapter
    private void setUpAutoCompleteCity(List<CityResponse.City> cities) {
        List<String> cityNames = new ArrayList<>();
        for (CityResponse.City city : cities) {
            cityNames.add(city.getCityName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                cityNames);
        spnCity.setAdapter(adapter);

        // Handle item click for MaterialAutoCompleteTextView
        spnCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Since position might not be accurate after filtering, get the corresponding city from the list
                String selectedCityName = (String) parent.getItemAtPosition(position);

                for (int i = 0; i < cityData.size(); i++) {
                    if (cityData.get(i).getCityName().equals(selectedCityName)) {
                        selectedCityId = String.valueOf(cityData.get(i).getCityId());
                        Log.d("SPANCITY", selectedCityId);
                        fetchZones(pathaoaccessstoken); // Fetch zones
                        break;
                    }
                }
            }
        });
    }





    private void fetchZones(String accessToken) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ZoneResponse> zoneCall = apiService.getZones("Bearer " + accessToken, selectedCityId);

        zoneCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ZoneResponse> call, Response<ZoneResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ZoneResponse.Zone> zones = response.body().getData().getData();
                    List<String> zoneNames = new ArrayList<>();
                    for (ZoneResponse.Zone zone : zones) {
                        zoneNames.add(zone.getZoneName());
                    }
                    ArrayAdapter<String> zoneAdapter = new ArrayAdapter<>(AddSellPaymentDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, zoneNames);
                    spnZone.setAdapter(zoneAdapter);


                    spnZone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedzonename = (String) parent.getItemAtPosition(position);

                            for (int i = 0; i < zones.size(); i++) {
                                if (zones.get(i).getZoneName().equals(selectedzonename)) {
                                    selectedZoneId = String.valueOf(zones.get(i).getZoneId());
                                    Log.d("SPANCITY", selectedZoneId);
                                    fetchAreas(pathaoaccessstoken); // Fetch zones
                                    break;
                                }
                            }
                        }
                    });

//                    spnZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            selectedZoneId = String.valueOf(zones.get(position).getZoneId());
//                            zone_name = zones.get(position).getZoneName();
//                            et_hierarchy.setText(city_name+"->"+zone_name+"->"+area_name);
//                            fetchAreas(accessToken);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {}
//                    });
                } else {
                    Log.e("Zone Response Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ZoneResponse> call, Throwable t) {
                Log.e("Zone Failure", t.getMessage());
            }
        });
    }

    private void fetchAreas(String accessToken) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<AreaResponse> areaCall = apiService.getAreas("Bearer " + accessToken, selectedZoneId);

        areaCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AreaResponse> call, Response<AreaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AreaResponse.Area> areas = response.body().getData().getData();
                    List<String> areaNames = new ArrayList<>();
                    for (AreaResponse.Area area : areas) {
                        areaNames.add(area.getAreaName());
                    }
                    ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(AddSellPaymentDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, areaNames);
                    spnArea.setAdapter(areaAdapter);


                    spnArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedzonename = (String) parent.getItemAtPosition(position);

                            for (int i = 0; i < areas.size(); i++) {
                                if (areas.get(i).getAreaName().equals(selectedzonename)) {
                                    selectedAreaId = String.valueOf(areas.get(i).getAreaId());
                                    Log.d("SPANCITY", selectedAreaId);
                                    break;
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AreaResponse> call, Throwable t) {
                Log.e("Area Failure", t.getMessage());
            }
        });
    }

    /*Pathao Code*/


    private void fetchcustomerdata(String query) {
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<CustomerResponse> call = apiService.getContact("Bearer " + accessToken,query);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("Response Body", String.valueOf(response.body().getData()));
                        List<CustomerResponse.Customer> customers = response.body().getData();
                        for (CustomerResponse.Customer customer : customers) {
                            contact_id = customer.getId();
                            etName.setText(customer.getName());
                            etAddress.setText(customer.getAddress_line_1());
                        }
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
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());
            }
        });
    }

    private String getAuthTokenFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("auth_token", null);
        return sharedPreferences.getString("auth_token", null);  // Retrieve the token from SharedPreferences
    }
    private boolean isValidPhoneNumber(String number) {
        return number.matches("^01[0-9]{9}$");
    }
}