package com.girls.ontop.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.girls.ontop.R;
import com.girls.ontop.models.UserResponse;
import com.google.gson.Gson;

public class DashboardActivity extends AppCompatActivity {

    Button seemore,seeless;
    TextView expenseCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        

        TextView addSellButton = findViewById(R.id.addSellButton);
        TextView posButton = findViewById(R.id.posButton);
        TextView customerButton = findViewById(R.id.customerButton);
        TextView supplierButton = findViewById(R.id.supplierButton);
        TextView allSellsButton = findViewById(R.id.allSellsButton);
        TextView logoutButton = findViewById(R.id.logout);
        TextView listSellsButton = findViewById(R.id.listSellsButton);

        LinearLayout addselllayout = findViewById(R.id.addselllayout);
        LinearLayout addposlayout = findViewById(R.id.addposlayout);
        LinearLayout customerlayout = findViewById(R.id.customerlayout);
        LinearLayout supplierlayout = findViewById(R.id.supplierlayout);
        LinearLayout allsellslayout = findViewById(R.id.allsellslayout);
        LinearLayout listposlayout = findViewById(R.id.listposlayout);
        LinearLayout expenselayout = findViewById(R.id.expenselayout);
        LinearLayout logoutlayout = findViewById(R.id.logoutlayout);
        LinearLayout seemorelayout = findViewById(R.id.seemorelayout);
        LinearLayout manufacturinglayout = findViewById(R.id.manufacturinglayout);
        LinearLayout expenseCreateLayout = findViewById(R.id.expenseCreateLayout);
        LinearLayout addmanufacturing = findViewById(R.id.addmanufacturing);




        LinearLayout second = findViewById(R.id.second);
        LinearLayout third = findViewById(R.id.third);
        LinearLayout fourth = findViewById(R.id.fourth);
        LinearLayout fifth = findViewById(R.id.fifth);
        LinearLayout sixth = findViewById(R.id.sixth);

        seemore = findViewById(R.id.seemore);
        seeless = findViewById(R.id.seeless);
        expenseCreate = findViewById(R.id.expenseCreate);

        seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seemore.setVisibility(View.GONE);
                seeless.setVisibility(View.VISIBLE);
                second.setVisibility(View.VISIBLE);
                third.setVisibility(View.VISIBLE);
                fourth.setVisibility(View.VISIBLE);
                fifth.setVisibility(View.VISIBLE);
                sixth.setVisibility(View.VISIBLE);
            }
        });

        seeless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seemore.setVisibility(View.VISIBLE);
                seeless.setVisibility(View.GONE);
                second.setVisibility(View.GONE);
                third.setVisibility(View.GONE);
                fourth.setVisibility(View.GONE);
                fifth.setVisibility(View.GONE);
                sixth.setVisibility(View.GONE);

            }
        });


        addselllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent posIntent = new Intent(DashboardActivity.this, AddSellActivity.class);
                startActivity(posIntent);
            }
        });

        manufacturinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent posIntent = new Intent(DashboardActivity.this, ManuFacturingListActivity.class);
                startActivity(posIntent);
            }
        });

        expenseCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent posIntent = new Intent(DashboardActivity.this, CreateExpenseActivity.class);
                startActivity(posIntent);
            }
        });

        addposlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent posIntent = new Intent(DashboardActivity.this, PosActivity.class);
                startActivity(posIntent);
            }
        });

        addmanufacturing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent posIntent = new Intent(DashboardActivity.this, ManufacturingAcitivity.class);
                startActivity(posIntent);
            }
        });

        customerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customerIntent = new Intent(DashboardActivity.this, ContactActivity.class);
                customerIntent.putExtra("type","customer");
                startActivity(customerIntent);
            }
        });

        supplierlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customerIntent = new Intent(DashboardActivity.this, ContactActivity.class);
                customerIntent.putExtra("type","supplier");
                startActivity(customerIntent);
            }
        });

        allsellslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allSellsIntent = new Intent(DashboardActivity.this, SellListActivity.class);
                allSellsIntent.putExtra("is_direct_sale",1);
                startActivity(allSellsIntent);
            }
        });

        listposlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allSellsIntent = new Intent(DashboardActivity.this, SellListActivity.class);
                allSellsIntent.putExtra("is_direct_sale",0);
                startActivity(allSellsIntent);
            }
        });

        expenselayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent posIntent = new Intent(DashboardActivity.this, ExpenseActivity.class);
                startActivity(posIntent);
            }
        });




        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user_data", null); // Get the JSON string from SharedPreferences

        if (userJson != null) {
            UserResponse.UserData userData = new Gson().fromJson(userJson, UserResponse.UserData.class);
            String firstName = userData.getFirstName();
            if (firstName != null) {
                TextView welcomeMessage = findViewById(R.id.welcomeMessage);
                welcomeMessage.setText(String.format("Welcome, %s", firstName));
            }


        } else {
            Log.d("User Data", "No user data found");
        }



        // Set click listeners for each button
        addSellButton.setOnClickListener(v -> {
            // Handle "Add Sell" button click
            Intent posIntent = new Intent(DashboardActivity.this, AddSellActivity.class);
            startActivity(posIntent);
        });

        logoutlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
            }
        });

        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
        });

        posButton.setOnClickListener(v -> {
            // Handle "POS" button click
            Intent posIntent = new Intent(DashboardActivity.this, PosActivity.class);
            startActivity(posIntent);
        });

        customerButton.setOnClickListener(v -> {
            // Handle "Customer" button click
            Intent customerIntent = new Intent(DashboardActivity.this, ContactActivity.class);
            customerIntent.putExtra("type","customer");
            startActivity(customerIntent);
        });

        supplierButton.setOnClickListener(v -> {
            // Handle "Supplier" button click
            Intent customerIntent = new Intent(DashboardActivity.this, ContactActivity.class);
            customerIntent.putExtra("type","supplier");
            startActivity(customerIntent);
        });

        allSellsButton.setOnClickListener(v -> {
            Intent allSellsIntent = new Intent(DashboardActivity.this, SellListActivity.class);
            allSellsIntent.putExtra("is_direct_sale",1);
            startActivity(allSellsIntent);
        });

        listSellsButton.setOnClickListener(v -> {
            Intent allSellsIntent = new Intent(DashboardActivity.this, SellListActivity.class);
            allSellsIntent.putExtra("is_direct_sale",0);
            startActivity(allSellsIntent);
        });


    }
}