package com.girls.ontop.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.girls.ontop.R;
import com.girls.ontop.models.LoginResponse;
import com.girls.ontop.models.UserResponse;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;
import com.girls.ontop.utils.AppConstants;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);

        findViewById(R.id.loginButton).setOnClickListener(this::handleLogin);
    }

    private void handleLogin(View view) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        login(username, password);
    }

    private void login(String username, String password) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        apiService.login(
                "password",
                AppConstants.CLIENT_ID,
                AppConstants.CLIENT_SECRET,
                username,
                password,
                ""
        ).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Save token in SharedPreferences for future use
                    String token = response.body().getAccessToken();

                    runOnUiThread(() -> {
                        saveToken(token);
                        fetchUserData(token);
                    });
                    // Navigate to Dashboard
                } else {
                    Log.d("LoginData Response", String.valueOf(response));
                    Toast.makeText(LoginActivity.this, "Login Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "LoginData Error: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserData(String accessToken) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        Call<UserResponse> call = apiService.getLoggedInUser("Bearer " + accessToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    UserResponse userResponse = response.body();
                    UserResponse.UserData userData = userResponse.getData();
                    // Store user data in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_data", new Gson().toJson(userData)); // Save user data as JSON
                    editor.putBoolean("is_logged_in", true);
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d("Token",token);
        editor.putString("auth_token", token);
        editor.apply();
    }
}
