package com.girls.ontop.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.adapters.ContactAdapter;
import com.girls.ontop.models.CustomerResponse;
import com.girls.ontop.models.LocationResponse;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity implements ContactAdapter.OnContactPayClickListener {

    private RecyclerView recyclerView;
    private SearchView searchViewContacts;
    ImageView ivContactPay;
    String type;
    private ContactAdapter contactAdapter;
    private List<CustomerResponse.Customer> contactList = new ArrayList<>();
    TextView allcontacttitle;
    int pagecount;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        allcontacttitle = findViewById(R.id.allcontacttitle);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if(type.equals("customer")){
            allcontacttitle.setText("All Customer");
        }else{
            allcontacttitle.setText("All Supplier");
        }

        recyclerView = findViewById(R.id.recyclerViewContacts);
        Button btnNext = findViewById(R.id.btnNext);
        Button btnPrevious = findViewById(R.id.btnPrevious);
        searchViewContacts = findViewById(R.id.searchViewContacts);
        searchViewContacts.setIconifiedByDefault(false);
        searchViewContacts.requestFocus();


        searchViewContacts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the final search when the user submits the query
                searchContactsBackend(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform a search as the user types
                searchContactsBackend(newText);
                return true;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(contactList,this);
        recyclerView.setAdapter(contactAdapter);

        fetchContacts(currentPage,type);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagecount = ++currentPage;
                fetchContacts(++pagecount,type);
                if(pagecount>1){
                    btnPrevious.setEnabled(true);
                }

            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage > 1){
                    fetchContacts(--currentPage,type);
                }else{
                    btnPrevious.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onContactPayClick(String contactId) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("CONTACT_ID", contactId);
        startActivity(intent);
    }

    private String getAuthTokenFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("auth_token", null);
        return sharedPreferences.getString("auth_token", null);  // Retrieve the token from SharedPreferences
    }

    private void searchContactsBackend(String query){
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<CustomerResponse> call = apiService.getContactDataSearch("Bearer " + accessToken,type,query,query,query,pagecount);


        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                Log.d("Working Code", String.valueOf(response.body().getData()));
                if (response.isSuccessful() && response.body() != null) {
                    contactList.clear();
                    contactList.addAll(response.body().getData());
                    contactAdapter.notifyDataSetChanged();
                    // Extract location names from the response
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());

            }
        });
    }

    private void fetchContacts(int page,String type) {
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<CustomerResponse> call = apiService.getContactData("Bearer " + accessToken,type,page);


        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contactList.clear();
                    contactList.addAll(response.body().getData());
                    contactAdapter.notifyDataSetChanged();
                    // Extract location names from the response
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());

            }
        });
    }
}