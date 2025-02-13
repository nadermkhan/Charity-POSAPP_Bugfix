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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.girls.ontop.R;
import com.girls.ontop.models.AccountResponse;
import com.girls.ontop.models.CustomerResponse;
import com.girls.ontop.models.OrderRequest;
import com.girls.ontop.models.PaymentBody;
import com.girls.ontop.models.SellsObject;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;
import com.girls.ontop.utils.AppConstants;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private TextInputEditText etAmount;
    private MaterialAutoCompleteTextView etPaymentAccount;
    private List<AccountResponse.Accounts> accountListData = new ArrayList<>();
    private List<String> accountList = new ArrayList<>();
    private String contactId;
    private int payment_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        etAmount = findViewById(R.id.et_amount);
        etPaymentAccount = findViewById(R.id.et_payment_account);
        Button btnSubmitPayment = findViewById(R.id.btn_submit_payment);
        contactId = getIntent().getStringExtra("CONTACT_ID");
        fetchAccountsFromApi();




        btnSubmitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPayment();
            }
        });

        etPaymentAccount.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
            } else {
                etPaymentAccount.showDropDown();
            }
        });

        etPaymentAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                payment_account = accountListData.get(i).getId();
                Log.d("Account Id", String.valueOf(payment_account));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    private void fetchAccountsFromApi() {
        String accessToken = getAuthTokenFromPreferences(); // Implement this method to get the token
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<AccountResponse> call = apiService.getAccounts("Bearer " + accessToken);
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AccountResponse.Accounts> accounts = response.body().getData();
                    List<String> accountNames = new ArrayList<>();
                    for (AccountResponse.Accounts account : accounts) {
                        accountNames.add(account.getName());
                        accountListData.add(account);
                    }
                    runOnUiThread(() -> setUpAutoComplete(accountNames));
                } else {
                    Log.d("Response Error", "Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private String getAuthTokenFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("auth_token", null);
        return sharedPreferences.getString("auth_token", null);  // Retrieve the token from SharedPreferences
    }

    private void setUpAutoComplete(List<String> accountNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, accountNames);
        etPaymentAccount.setAdapter(adapter);
    }


    private void submitPayment() {
        String accessToken = getAuthTokenFromPreferences(); // Fetch the saved token
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Log.d("ACCESS TOKEN",accessToken);
        String selectedAccount = etPaymentAccount.getText().toString();
        String amount = etAmount.getText().toString();

        if (selectedAccount.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Find account ID from selected account name
        int accountId = -1;
        for (AccountResponse.Accounts account : accountListData) {
            Log.d("data",selectedAccount);
            if (account.getName().equals(selectedAccount)) {
                accountId = account.getId();
                break;
            }
        }


        if (accountId == -1) {
            Toast.makeText(this, "Invalid account selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("Contact ID",contactId);
        // Create body for the payment
        PaymentBody paymentBody = new PaymentBody(contactId, Double.parseDouble(amount), "cash", accountId);

        Call<ResponseBody> call = apiService.makepayment("Bearer " + accessToken, paymentBody);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        // Log or show the raw response
                        String rawResponse = response.body().string();
                        Log.d("Raw Response", rawResponse);
                        Toast.makeText(getApplicationContext(),"Payment Successfull",Toast.LENGTH_SHORT).show();
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
}