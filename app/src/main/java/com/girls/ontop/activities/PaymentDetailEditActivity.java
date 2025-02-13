package com.girls.ontop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.girls.ontop.R;
import com.girls.ontop.models.AccountResponse;
import com.girls.ontop.models.Contact;
import com.girls.ontop.models.CustomerResponse;
import com.girls.ontop.models.OrderPayment;
import com.girls.ontop.models.OrderProduct;
import com.girls.ontop.models.OrderRequest;
import com.girls.ontop.models.OrderResponse;
import com.girls.ontop.models.OrderUpdateRequest;
import com.girls.ontop.models.PaymentLineModel;
import com.girls.ontop.models.Product;
import com.girls.ontop.models.ProductResponse;
import com.girls.ontop.models.Sell;
import com.girls.ontop.models.SellLineModel;
import com.girls.ontop.models.SellListResponse;
import com.girls.ontop.models.SellsObject;
import com.girls.ontop.models.SellsUpdateObject;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;
import com.girls.ontop.utils.AppConstants;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailEditActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    List<String> accountList = new ArrayList<>();
    List<Sell> invoiceselldata;
    List<AccountResponse.Accounts> accountListData = new ArrayList<>();
    float totalAmount=0,discountValue = 0,shippingCharge = 0,advancePayment = 0,totalDue = totalAmount;

    private AlertDialog alertDialog;
    private AutoCompleteTextView tvAutoPhone, tvSelectDistrict, tvSelectArea, tvSelectZone;
    private MaterialAutoCompleteTextView etPaymentAccount,et_hierarchy;
    private EditText etName, etNotes, etAddress, etShippingCharge, etAdvanceAmount, etDiscount, etTotalDiscount;
    private Button btnPaymentConfirm;
    private Spinner spnDisCountType;
    private float due,subTotal,discount,businessLocationID;
    private String businessName;
    String selectedItem;
    String invoice_id;
    String locationId;
    private ArrayList<Product> selectedProductList = new ArrayList<>();
    public int contact_id,payment_account;
    private float shipping_charges;
    List<OrderProduct> orderproducts = new ArrayList<>();
    List<OrderPayment> orderPayments = new ArrayList<>();
    private TextView tvDue, tvDisplayOtherExpance,tv_shpping_150,tv_shpping_80,tv_advance_500,tv_advance_200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail_edit);

        setTitle("Payment");

        // Initialize UI components using findViewById
        progressBar = new ProgressBar(this);
        etNotes = findViewById(R.id.et_payment_note);
        etShippingCharge = findViewById(R.id.et_payment_shipping_charge);
        etAdvanceAmount = findViewById(R.id.et_payment_advance);
        etDiscount = findViewById(R.id.et_payment_discount);
        etTotalDiscount = findViewById(R.id.et_payment_total_discount);
        tvDue = findViewById(R.id.tota_due);
        btnPaymentConfirm = findViewById(R.id.btnPaymentConfirm);
        spnDisCountType = findViewById(R.id.spn_discount_type);
        etPaymentAccount = findViewById(R.id.et_payment_account);

        tvAutoPhone = findViewById(R.id.auto_customer_phone_number);
        etName = findViewById(R.id.et_payment_full_name);
        etAddress = findViewById(R.id.et_payment_address);



//        tvSelectDistrict = findViewById(R.id.et_payment_district);
//        tvSelectArea = findViewById(R.id.et_payment_sub_district);
//        tvSelectZone = findViewById(R.id.et_zone);
        tv_shpping_150 = findViewById(R.id.tv_shpping_150);
        tv_shpping_80 = findViewById(R.id.tv_shpping_80);
        tv_advance_200 = findViewById(R.id.tv_advance_200);
        tv_advance_500 = findViewById(R.id.tv_advance_500);

        et_hierarchy = findViewById(R.id.et_hierarchy);

        spnDisCountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedItem = adapterView.getItemAtPosition(position).toString().toLowerCase();;
                // You can call your calculation methods here or update UI accordingly
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case when no item is selected, if needed
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

        et_hierarchy.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_hierarchy_selector, null);
            bottomSheetDialog.setContentView(dialogView);

            Spinner districtSpinner = dialogView.findViewById(R.id.spinner_district);
            Spinner zoneSpinner = dialogView.findViewById(R.id.spinner_zone);
            Spinner areaSpinner = dialogView.findViewById(R.id.spinner_area);

            // Populate District Spinner
            List<String> districts = Arrays.asList("District 1", "District 2", "District 3");
            ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districts);
            districtSpinner.setAdapter(districtAdapter);

            // Set Listener for District Spinner
            districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Populate Zone Spinner based on District
                    if (position == 0) { // District 1
                        //loadZonesForDistrict1(zoneSpinner);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            // Similar setup for Zone and Area

            bottomSheetDialog.show();
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
            //  tvDue.setText((int) due);

            subTotal = extras.getFloat(AppConstants.subTotal, 0.0f);
            invoice_id = extras.getString("invoice_id",null);
            locationId = extras.getString("locationId", "1");

            totalAmount = subTotal;
            totalDue = due;
            Log.d("Due", String.valueOf(subTotal));
            tvDue.setText(String.valueOf(subTotal));
            if (intent != null && intent.hasExtra("productlist")) {
                selectedProductList = intent.getParcelableArrayListExtra("productlist");
            }

            if (selectedProductList != null) {
                for (Product product : selectedProductList) {
                    String discount_type;
                    if(product.getDiscountType()==AppConstants.DiscountType.PERCENTAGE.ordinal()){
                        discount_type = "percentage";
                    }else{
                        discount_type = "fixed";
                    }
                    orderproducts.add(new OrderProduct(product.getId(),product.getQuantity(),product.getPriceInFloat(),product.getVariationid(),discount_type,product.getDiscount()));
                    Log.d("Product", "ID: " + product.getId() + ", Name: " + product.getName()+", Quantity: " + product.getVariationid());
                }
            }

            Log.d("MY CART", String.valueOf(selectedProductList));

            discount = extras.getFloat(AppConstants.discount, 0.0f);
            etDiscount.setText(String.valueOf(discount));
            etTotalDiscount.setText(String.valueOf(discount));

            businessLocationID = extras.getInt(AppConstants.business_location_id, -1);
            businessName = extras.getString(AppConstants.business_name, "");
            TextView tvLocationBusiness = findViewById(R.id.tv_location_business);
            tvLocationBusiness.setText(businessName);
            fetchinvoice(invoice_id);
            calculation();
        }
    }

    private void fetchinvoice(String invoice_id) {
        String accessToken = getAuthTokenFromPreferences(); // Use the actual access token


        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<SellListResponse> call = apiService.getSellById("Bearer " + accessToken, String.valueOf(invoice_id));

        call.enqueue(new Callback<SellListResponse>() {
            @Override
            public void onResponse(Call<SellListResponse> call, Response<SellListResponse> response) {
                SellListResponse sellresponse = response.body();
                Log.d("Sell response Full", String.valueOf(sellresponse));
                invoiceselldata = sellresponse.getSells();
                for (Sell sell : invoiceselldata) {
                    Log.d("Sell response Full", String.valueOf(sell.getShipping_charges()));
                    etShippingCharge.setText(sell.getShipping_charges());
                    etNotes.setText(sell.getSell_line_note());
                    etTotalDiscount.setText(sell.getDiscount_amount());
                    locationId = sell.getLocation_id();


                    contact_id = sell.getContact_id();
                    fetchcustomerdata(String.valueOf(contact_id));

                    if(sell.getDiscount_type().equals("fixed")) {
                        spnDisCountType.setSelection(1);
                    }
                    if(sell.getDiscount_type().equals("percentage")){
                        spnDisCountType.setSelection(2);
                    }
                    for(PaymentLineModel paymentLineModel : sell.getPayment_lines()){
                        etAdvanceAmount.setText(String.valueOf(paymentLineModel.getAmount()));
                        if (accountListData != null && paymentLineModel != null) {
                            for (AccountResponse.Accounts accounts : accountListData) {
                                if (accounts.getId() == paymentLineModel.getAccount_id()) {
                                    etPaymentAccount.setText(accounts.getName(), false);
                                    break; // Exit the loop once a match is found
                                }
                            }
                        }

                    }

//                    for(Contact contact : sell.getContacts()){
//                        tvAutoPhone.setText(contact.getMobile());
//                        etName.setText(contact.getName());
//                        etAddress.setText(contact.getAddress_line_1());
//                    }
                }
            }

            @Override
            public void onFailure(Call<SellListResponse> call, Throwable t) {
                // Handle failure (e.g., no internet, server error, etc.)
//                Log.e(TAG, "Error fetching sell list: " + t.getMessage());

            }
        });
    }



    private void re_calculation(){
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

        String discountInput = etTotalDiscount.getText().toString();
        String shippingChargeInput = etShippingCharge.getText().toString();
        String advancePaymentInput = etAdvanceAmount.getText().toString();
        Log.d("shippingChargeInput",shippingChargeInput);
        Log.d("advancePaymentInput",advancePaymentInput);
        Log.d("totalAmount", String.valueOf(totalAmount));
        Log.d("discountInput", String.valueOf(discountInput));
        orderPayments.add(new OrderPayment(advancePaymentInput,accountId));
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
        Log.d("totalDue", String.valueOf(totalDue));
        tvDue.setText(String.valueOf(totalDue));
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

        String discountInput = etTotalDiscount.getText().toString();
        String shippingChargeInput = etShippingCharge.getText().toString();
        String advancePaymentInput = etAdvanceAmount.getText().toString();
        Log.d("shippingChargeInput",shippingChargeInput);
        Log.d("advancePaymentInput",advancePaymentInput);
        Log.d("totalAmount", String.valueOf(totalAmount));
        Log.d("discountInput", String.valueOf(discountInput));
        orderPayments.add(new OrderPayment(advancePaymentInput,accountId));
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
        Log.d("totalDue", String.valueOf(totalDue));
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
            if (!hasFocus) {
            } else {
                etPaymentAccount.showDropDown();
            }
        });

        etShippingCharge.addTextChangedListener(new TextWatcher() {
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

        etAdvanceAmount.addTextChangedListener(new TextWatcher() {
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
                etAdvanceAmount.setText("200");
                calculation();
            }
        });
        tv_advance_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAdvanceAmount.setText("500");
                calculation();
            }
        });



        btnPaymentConfirm.setOnClickListener(view -> {
                // Handle payment confirmation logic here
                submitorder();
//                Intent intent  = new Intent(getApplicationContext(),DashboardActivity.class);
//                startActivity(intent);
                Toast.makeText(this, "Payment Confirmed", Toast.LENGTH_SHORT).show();
        });
    }

    private void submitorder() {
        String accessToken = getAuthTokenFromPreferences(); // Fetch the saved token
        Log.d("ACCESS TOKEN",accessToken);
        shipping_charges = Float.parseFloat(etShippingCharge.getText().toString());
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Log.d("OrderUpdateRequest", "Location ID: " + locationId);
        Log.d("OrderUpdateRequest", "Contact ID: " + contact_id);
        Log.d("OrderUpdateRequest", "Shipping Charges: " + shipping_charges);
        Log.d("OrderUpdateRequest", "Order Products: " + orderproducts.toString());
        Log.d("OrderUpdateRequest", "Total Discount: " + etTotalDiscount.getText().toString());
        Log.d("OrderUpdateRequest", "Selected Item: " + selectedItem);
        Log.d("OrderUpdateRequest", "Order Payments: " + orderPayments.get(0).getAmount());
        OrderUpdateRequest orderRequest = new OrderUpdateRequest(
                locationId,
                contact_id,
                shipping_charges,
                orderproducts,
                etTotalDiscount.getText().toString(),
                selectedItem,
                orderPayments
        );

//        SellsUpdateObject sellsObject = new SellsUpdateObject(orderRequest);

        // Make the API call
        Call<ResponseBody> call = apiService.updateOrder("Bearer " + accessToken, orderRequest,invoice_id);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        // Log or show the raw response
                        String rawResponse = response.body().string();
                        Log.d("Raw Response", rawResponse);
                        Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                        startActivity(intent);
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Response Body Order", t.getMessage());
            }
        });
    }


    private void fetchcustomerdata(String query) {
        Log.d("Query",query);
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<CustomerResponse> call = apiService.getContactId("Bearer " + accessToken,query);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("Customer Response Body", String.valueOf(response.body().getData()));
                        List<CustomerResponse.Customer> customers = response.body().getData();
                        for (CustomerResponse.Customer customer : customers) {
                            contact_id = customer.getId();
                            tvAutoPhone.setText(customer.getMobile());
                            etName.setText(customer.getName());
                            Log.d("Customer Response Body", String.valueOf(customer.getId()));
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