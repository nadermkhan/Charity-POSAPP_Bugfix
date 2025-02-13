package com.girls.ontop.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.adapters.ContactAdapter;
import com.girls.ontop.adapters.ExpenseAdapter;
import com.girls.ontop.models.CustomerResponse;
import com.girls.ontop.models.Expense;
import com.girls.ontop.models.ExpenseResponse;
import com.girls.ontop.models.ExpenseUser;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseActivity extends AppCompatActivity implements ExpenseAdapter.OnDeleteClickListener {

    private RecyclerView recyclerView;
    private SearchView searchViewExpense;
    String type;
    private ExpenseAdapter expenseAdapter;
    String start_date = "",end_date = "";
    private List<Expense> expenselist = new ArrayList<>();
    private EditText etStartDate, etEndDate,search_edit_text;
    int pagecount;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        recyclerView = findViewById(R.id.recyclerViewExpense);

        Button btnNext = findViewById(R.id.btnNext);
        Button btnPrevious = findViewById(R.id.btnPrevious);
        searchViewExpense = findViewById(R.id.searchViewExpense);
        searchViewExpense.setIconifiedByDefault(false);
        searchViewExpense.requestFocus();
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);

        setDefaultDates();

        etStartDate.setOnClickListener(view -> showDatePickerDialogStart(etStartDate));
        etEndDate.setOnClickListener(view -> showDatePickerDialogEnd(etEndDate));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter(this,expenselist,this);
        recyclerView.setAdapter(expenseAdapter);
        fetchExpense(currentPage,type);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagecount = ++currentPage;
                fetchExpense(++pagecount,type);
                if(pagecount>1){
                    btnPrevious.setEnabled(true);
                }

            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage > 1){
                    fetchExpense(--currentPage,type);
                }else{
                    btnPrevious.setEnabled(false);
                }
            }
        });
    }

    private void showDatePickerDialogEnd(EditText etEndDate) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format and set the selected date on the EditText
                    String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    etEndDate.setText(formattedDate);
                    end_date = formattedDate;
                    fetchExpense(0,"");
//                    sellListViewModel.fetchSellList(locationId, "",start_date,end_date,pagenumber,is_direct_sale,search);
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showDatePickerDialogStart(EditText etStartDate) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format and set the selected date on the EditText
                    String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    etStartDate.setText(formattedDate);
                    start_date = formattedDate;
                    fetchExpense(0,"");
//                    sellListViewModel.fetchSellList(locationId, "",start_date,end_date,pagenumber,is_direct_sale,search);
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void setDefaultDates() {
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String endDate = dateFormat.format(currentDate.getTime());
        etEndDate.setText(endDate);
        end_date = endDate;
        currentDate.add(Calendar.DAY_OF_MONTH, -30);
        String startDate = dateFormat.format(currentDate.getTime());
        etStartDate.setText(startDate);
        start_date = startDate;
    }

    private String getAuthTokenFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("auth_token", null);
        return sharedPreferences.getString("auth_token", null);  // Retrieve the token from SharedPreferences
    }



    private void fetchExpense(int currentPage, String type) {
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Map<String, String> params = new HashMap<>();
        params.put("start_date", start_date);
        params.put("end_date", end_date);
        Call<ExpenseResponse> call = apiService.getExpenseData("Bearer " + accessToken,params,pagecount);


        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                Log.d("Working Code", String.valueOf(response.body().getData()));
                if (response.isSuccessful() && response.body() != null) {
                    expenselist.clear();
                    expenselist.addAll(response.body().getData());
                    Log.d("Working Code", expenselist.get(0).getRefNo());
                    expenseAdapter.notifyDataSetChanged();
                    // Extract location names from the response
                }
            }

            @Override
            public void onFailure(Call<ExpenseResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());

            }
        });
    }



    @Override
    public void onDeleteClick(Expense expense) {
        Toast.makeText(getApplicationContext(),"Delete is not permitted",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClick(Expense expense) {
        Toast.makeText(getApplicationContext(),"Your are not permitted to edit expense",Toast.LENGTH_SHORT).show();
    }
}