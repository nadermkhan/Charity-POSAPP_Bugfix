package com.girls.ontop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.girls.ontop.R;
import com.girls.ontop.models.AccountResponse;
import com.girls.ontop.models.BusinessLocation;
import com.girls.ontop.models.Expense;
import com.girls.ontop.models.ExpenseCategoryResponse;
import com.girls.ontop.models.ExpenseRequest;
import com.girls.ontop.models.ExpenseResponse;
import com.girls.ontop.models.LocationResponse;
import com.girls.ontop.models.NewCustomerResponse;
import com.girls.ontop.models.OrderPayment;
import com.girls.ontop.models.PaymentMethodResponse;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateExpenseActivity extends AppCompatActivity {

    Spinner spinnerLocation,payment_method;
    private MaterialAutoCompleteTextView etPaymentAccount,et_expense_category,et_expense_sub_category,et_total_amount,et_payment_amount,et_note_holder;
    List<String> locations = new ArrayList<>();
    List<String> accountList = new ArrayList<>();
    List<String> expensecategorylist = new ArrayList<>();
    List<String> expenseSubcategorylist = new ArrayList<>();
    List<ExpenseCategoryResponse.ExpenseCategory> expensecategoryData = new ArrayList<>();
    List<ExpenseCategoryResponse.ExpenseSubCategory> expenseSubCategoryDataPlaceholder = new ArrayList<>();
    List<ExpenseCategoryResponse.ExpenseSubCategory> expenseSubCategoryData = new ArrayList<>();
    List<AccountResponse.Accounts> accountListData = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    private final ArrayList<BusinessLocation> aryListOfAllBusinessLocations = new ArrayList<>();
    private BusinessLocation selectedBusinessLocation = null;
    String locationId,locationname,selectedKey;
    public int contact_id=0,payment_account,expensecategory,expensesubcategory;
    List<OrderPayment> orderPayments = new ArrayList<>();
    Button btnPaymentConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);

        spinnerLocation = findViewById(R.id.location_id);
        et_expense_category = findViewById(R.id.expensecategory);
        et_expense_sub_category = findViewById(R.id.expensesubcategory);
        etPaymentAccount = findViewById(R.id.et_payment_account);
        btnPaymentConfirm = findViewById(R.id.btnPaymentConfirm);
        et_total_amount = findViewById(R.id.et_total_amount);
        payment_method = findViewById(R.id.payment_method);
        et_payment_amount = findViewById(R.id.et_payment_amount);
        et_note_holder = findViewById(R.id.et_note_holder);
        locationId = "1";

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationId = ids.get(i);
                locationname = locations.get(i);
                Log.d("Location ID", locationId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Optional: Handle the case where nothing is selected
            }
        });


        etPaymentAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                payment_account = accountListData.get(i).getId();
                Log.d("Payment Account", String.valueOf(payment_account));
            }
        });


        etPaymentAccount.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                etPaymentAccount.showDropDown();
            }
        });


        /*Expense Category*/
        et_expense_category.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                et_expense_category.showDropDown();
            }
        });

        et_expense_sub_category.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                et_expense_sub_category.showDropDown();
            }
        });

        et_expense_sub_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                expensesubcategory = expenseSubCategoryData.get(i).getId();
                Log.d("Expense Sub Category", String.valueOf(expensecategory));
            }
        });

        et_expense_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                expensecategory = expensecategoryData.get(i).getId();
                et_expense_sub_category.setText("");
                expenseSubcategorylist.clear();
                generatesubcategory(i);
                Log.d("Expense Category", String.valueOf(expensecategory));
                Log.d("Expense Sub Category", String.valueOf(expensecategory));
            }
        });

//        et_expense_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                expensecategory = expensecategoryData.get(i).getId();
//
//                Log.d("Expense Category", String.valueOf(expensecategory));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        /*Expense Category*/

        fetchLocations();
        fetchMethods();
        fetchAccountsFromApi();
        fetchExpenseCategoryFromApi();

        btnPaymentConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createexpenseorder();
            }
        });


    }


    private void generatesubcategory(int id){

        expenseSubcategorylist.clear();
        expenseSubCategoryData.clear();

        List<ExpenseCategoryResponse.ExpenseSubCategory> subCategories = expensecategoryData.get(id).getSubCategories();

        if (subCategories != null && !subCategories.isEmpty()) {
            for (ExpenseCategoryResponse.ExpenseSubCategory expenseSubCategory : subCategories) {
                Log.d("Expense SUb Category",expenseSubCategory.getName());
                expenseSubcategorylist.add(expenseSubCategory.getName());
                expenseSubCategoryData.add(expenseSubCategory);
            }
        }
        runOnUiThread(() -> setUpAutoCompleteExpenseSubCategory(expenseSubcategorylist));
    }

    private void createexpenseorder() {
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        String total_amount = et_total_amount.getText().toString();
        String total_payment_amount = et_payment_amount.getText().toString();
        String notes = et_note_holder.getText().toString();
        if(total_amount.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please enter amount",Toast.LENGTH_SHORT);
            return;
        }
        orderPayments.clear();
        orderPayments.add(new OrderPayment(total_payment_amount,payment_account,selectedKey));
        ExpenseRequest expenseRequest = new ExpenseRequest(
                Integer.parseInt(locationId),
                total_amount,
                expensecategory,
                expensesubcategory,
                notes,
                orderPayments
        );


        Call<ExpenseResponse> call = apiService.createExpense("Bearer " + accessToken, expenseRequest);
        call.enqueue(new Callback<ExpenseResponse>() {
            @Override
            public void onResponse(@NonNull Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        ExpenseResponse responseBody = response.body();
                        if (responseBody != null) {
                            Toast.makeText(getApplicationContext(),"Expense Added",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),ExpenseActivity.class);
                            startActivity(intent);
                            finish();

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
            public void onFailure(Call<ExpenseResponse> call, Throwable t) {
                Log.d("Make Error", t.getMessage());
                Toast.makeText(getApplicationContext(),"Expense Added",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),ExpenseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchExpenseCategoryFromApi() {
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ExpenseCategoryResponse> call = apiService.getExpenseCategory("Bearer " + accessToken);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ExpenseCategoryResponse> call, Response<ExpenseCategoryResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("Response Body", String.valueOf(response.body().getData()));
                        List<ExpenseCategoryResponse.ExpenseCategory> expenseCategories = response.body().getData();
                        for (ExpenseCategoryResponse.ExpenseCategory expenseCategory : expenseCategories) {
                            expensecategorylist.add(expenseCategory.getName());
                            expensecategoryData.add(expenseCategory);
                        }
                        runOnUiThread(() -> setUpAutoCompleteExpenseCategory(expensecategorylist));

//                        etPaymentAccount.setText(accountList.get(3), false);

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
            public void onFailure(Call<ExpenseCategoryResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());
            }
        });
    }


    private void setUpAutoComplete(List<String> accountList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                accountList
        );
        etPaymentAccount.setAdapter(adapter);
    }

    private void setUpAutoCompleteExpenseCategory(List<String> expensecategorylist) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                expensecategorylist
        );
        et_expense_category.setAdapter(adapter);
    }

    private void setUpAutoCompleteExpenseSubCategory(List<String> expenseSubcategorylist) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                expenseSubcategorylist
        );
        et_expense_sub_category.setAdapter(adapter);
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

//                        etPaymentAccount.setText(accountList.get(3), false);

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

    private String getAuthTokenFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("auth_token", null);
        return sharedPreferences.getString("auth_token", null);  // Retrieve the token from SharedPreferences
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


    private void fetchMethods() {
        String accessToken = getAuthTokenFromPreferences(); // Fetch the access token
        Log.d("Access Token", accessToken);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Map<String, String>> call = apiService.getMethods("Bearer " + accessToken);

        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Extract payment method names
                    List<String> paymentMethods = new ArrayList<>();
                    List<String> paymentKeys = new ArrayList<>(); // Store keys if needed

                    for (Map.Entry<String, String> entry : response.body().entrySet()) {

                        paymentKeys.add(entry.getKey());
                        paymentMethods.add(entry.getValue());
                    }

                    // Set up the spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, paymentMethods);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    payment_method.setAdapter(adapter);


                    // Handle selection event
                    payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedMethod = paymentMethods.get(position);
                            selectedKey = paymentKeys.get(position);
                            Log.d("Selected Method", "Key: " + selectedKey + ", Value: " + selectedMethod);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to fetch payment methods", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch payment methods", t);
                Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}