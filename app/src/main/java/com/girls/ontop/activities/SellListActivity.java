package com.girls.ontop.activities;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.adapters.UpdatePathaoRequest;
import com.girls.ontop.models.AreaResponse;
import com.girls.ontop.models.CityResponse;
import com.girls.ontop.models.LocationResponse;
import com.girls.ontop.models.Sell;
import com.girls.ontop.adapters.SellListAdapter;
import com.girls.ontop.models.SellListResponse;
import com.girls.ontop.models.SellListViewModel;
import com.girls.ontop.models.SellListViewModelFactory;
import com.girls.ontop.models.UpdateShippingStatusRequest;
import com.girls.ontop.models.ZoneResponse;
import com.girls.ontop.network.ApiClient;
import com.girls.ontop.network.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellListActivity extends AppCompatActivity {



    List<CityResponse.City> cityData = new ArrayList<>();
    AutoCompleteTextView spnCity,spnZone,spnArea;
    String selectedItem,selectedStoreId,selectedCityId,selectedZoneId,selectedAreaId;
    List<String> cityIdData = new ArrayList<>();


    private SellListViewModel sellListViewModel;
    ImageView ivShare,ivPay,ivEdit;
    private RecyclerView recyclerView;
    private SellListAdapter sellListAdapter;
    private EditText etStartDate, etEndDate,search_edit_text;
    Spinner spinnerLocation;
    String pathaoaccessstoken;
    List<String> locations = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    String locationId;
    String start_date = "",end_date = "";
    int pagenumber = 1;
    String search = "";
    int is_direct_sale=0;
    Button btnNext,btnPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_list);


        // Initialize RecyclerView
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        recyclerView = findViewById(R.id.recyclerViewSells);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sellListAdapter = new SellListAdapter(this,new SellListAdapter.OnItemClickListener() {
            @Override
            public void onShareClick(Sell sell) {
                fetchinvoice(sell.getId());
                Log.d("Share", "Share button clicked for: " + sell.getInvoiceNo());
            }

            @Override
            public void onEditClick(Sell sell) {
                fetchinvoiceEdit(sell.getId());
                Log.d("Pay", "Pay button clicked for: " + sell.getInvoiceNo());
            }

            @Override
            public void onChangeStatusClick(Sell sell, String newStatus,String statusnote) {
                String accessToken = getAuthTokenFromPreferences();
                ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
                UpdateShippingStatusRequest request = new UpdateShippingStatusRequest(sell.getId(), newStatus,statusnote);
                Call<Void> call = apiService.updateShippingStatus("Bearer " + accessToken, request);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("API Success", "Shipping status updated successfully");
                            sellListViewModel.fetchSellList(locationId, "",start_date,end_date,pagenumber,is_direct_sale,search);

                            // Handle success (e.g., show a success message or update UI)
                            Toast.makeText(SellListActivity.this, "Shipping status updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("API Failure", "Failed to update shipping status");
                            Toast.makeText(SellListActivity.this, "Failed to update shipping status", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("API Error", t.getMessage());
                        Toast.makeText(SellListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDeleteClick(Sell sell) {
                String accessToken = getAuthTokenFromPreferences();
                ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
                Call<Void> call = apiService.deleteOrder("Bearer " + accessToken, String.valueOf(sell.getId()));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("API Success", "Sell deleted successfully");
                            Toast.makeText(getApplicationContext(),"Sell deleted successfully",Toast.LENGTH_SHORT).show();
                            sellListViewModel.fetchSellList(locationId, "",start_date,end_date,pagenumber,is_direct_sale,search);

                            // Handle success (e.g., show a success message or update UI)

                        } else {
                            Log.d("API Failure", "Failed to update shipping status");
                            Toast.makeText(SellListActivity.this, "Failed to delete sell", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("API Error", t.getMessage());
                        Toast.makeText(SellListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onPathaoClick(Sell sell) {
                showPathaoDialog(getApplicationContext(),sell);
            }


            private void showPathaoDialog(Context context, Sell sell) {
                Context context1 = SellListActivity.this;
                AlertDialog.Builder builder = new AlertDialog.Builder(context1);
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.dummylayout, null);



                spnCity = view.findViewById(R.id.spinner_district);
                spnZone = view.findViewById(R.id.spinner_zone);
                spnArea = view.findViewById(R.id.spinner_area);
                Button btnSend = view.findViewById(R.id.btnSend);
                Button btnClose = view.findViewById(R.id.btnClose);

                spnCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                            spnCity.showDropDown();
                    }
                });

               fetchTokenAndInitializeSpinners();

                // Fetch data for AutoCompleteTextView from API


                builder.setView(view);
                AlertDialog dialog = builder.create();

                btnClose.setOnClickListener(v -> dialog.dismiss());

                btnSend.setOnClickListener(v -> {
                    if(!selectedCityId.isEmpty() && !selectedAreaId.isEmpty() && !selectedZoneId.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Please Wait",Toast.LENGTH_SHORT).show();
                        updatepathao(sell,dialog);
                    }else{
                        Toast.makeText(getApplicationContext(),"Select required locations",Toast.LENGTH_SHORT).show();
                    }


                });

                dialog.show();
            }



            @Override
            public void onPayClick(Sell sell) {
                fetchinvoicePay(sell.getId());
                Log.d("Pay", "Pay button clicked for: " + sell.getInvoiceNo());
            }


        });
        recyclerView.setAdapter(sellListAdapter);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        search_edit_text = findViewById(R.id.search_edit_text);
        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search = String.valueOf(charSequence);
                Log.d("Search Data",search);
                sellListViewModel.fetchSellList(locationId, "",start_date,end_date,pagenumber,is_direct_sale,search);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setDefaultDates();

        etStartDate.setOnClickListener(view -> showDatePickerDialogStart(etStartDate));
        etEndDate.setOnClickListener(view -> showDatePickerDialogEnd(etEndDate));

//        List<String> locations = new ArrayList<>();
//        locations.add("All Locations"); // Default value
//        locations.add("Location 1");
//        locations.add("Location 2");
//        locations.add("Location 3");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerLocation.setAdapter(adapter);
//
//        spinnerLocation.setSelection(0);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagenumber = pagenumber+1;
                btnPrevious.setEnabled(true);
                sellListViewModel.fetchSellList(locationId, "",start_date,end_date,pagenumber,is_direct_sale,search);
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pagenumber>1) {
                    pagenumber = pagenumber - 1;
                    sellListViewModel.fetchSellList(locationId, "",start_date,end_date,pagenumber,is_direct_sale,search);
                }else{
                    btnPrevious.setEnabled(false);
                }
            }
        });

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    locationId = ids.get(i);
                    sellListViewModel.fetchSellList(locationId, "",start_date,end_date,pagenumber,is_direct_sale,search);
                    Log.d("Location ID", locationId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Optional: Handle the case where nothing is selected
            }
        });


        fetchLocations();

        // Initialize ViewModel using custom factory with context
        sellListViewModel = new ViewModelProvider(this, new SellListViewModelFactory(this)).get(SellListViewModel.class);

        // Example parameters (You should pass the actual location and contact ID values)
        locationId = "1";  // Example location_id
        String contactId = "molestiae";  // Example contact_id

        Intent intent = getIntent();
        is_direct_sale = intent.getIntExtra("is_direct_sale", 0);

        // Fetch data from the API
        sellListViewModel.fetchSellList(locationId, contactId,start_date,end_date,pagenumber,is_direct_sale,search);

        // Observe the LiveData from the ViewModel
        sellListViewModel.getSellListLiveData().observe(this, new Observer<List<Sell>>() {
            @Override
            public void onChanged(List<Sell> sells) {
                if (sells != null) {
                    sellListAdapter.submitList(sells);  // Update the RecyclerView with the new list
                } else {
                    Toast.makeText(SellListActivity.this, "Failed to fetch sell data", Toast.LENGTH_SHORT).show();
                }
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
    private void updatepathao(Sell sell, AlertDialog dialog) {
        String accessToken = getAuthTokenFromPreferences();
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        UpdatePathaoRequest request = new UpdatePathaoRequest(sell.getId(), selectedCityId,selectedAreaId,selectedZoneId);
        Call<ResponseBody> call = apiService.updatePathao("Bearer " + accessToken, request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        String responseString = response.body().string();
                        Log.d("RAWRESPONSE", responseString);
                        JSONObject jsonResponse = new JSONObject(responseString);
                        String pathaoTrx = jsonResponse.getString("pathaotrx");
                        dialog.dismiss();
                        showPathaoDialog(pathaoTrx);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Log.d("API Success", "Pathao Send successfully");

                    // Handle success (e.g., show a success message or update UI)
                    Toast.makeText(SellListActivity.this, "Pathao Send successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("API Failure", "Failed to update shipping status");
                    Toast.makeText(SellListActivity.this, "Failed to update shipping status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("API Error", t.getMessage());
                Toast.makeText(SellListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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
                        fetchCities(accessToken);
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
                    ArrayAdapter<String> zoneAdapter = new ArrayAdapter<>(SellListActivity.this, android.R.layout.simple_spinner_dropdown_item, zoneNames);
                    spnZone.setAdapter(zoneAdapter);

                    spnZone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            spnZone.showDropDown();
                        }
                    });


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
                    ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(SellListActivity.this, android.R.layout.simple_spinner_dropdown_item, areaNames);
                    spnArea.setAdapter(areaAdapter);

                    spnArea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            spnArea.showDropDown();
                        }
                    });

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


    private void fetchinvoicePay(int invoice_id) {
        String accessToken = getAuthTokenFromPreferences(); // Use the actual access token


        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<SellListResponse> call = apiService.getSellById("Bearer " + accessToken, String.valueOf(invoice_id));

        call.enqueue(new Callback<SellListResponse>() {
            @Override
            public void onResponse(Call<SellListResponse> call, Response<SellListResponse> response) {
                SellListResponse sellresponse = response.body();
                Log.d("Sell response Full", String.valueOf(sellresponse));
                List<Sell> selldata = sellresponse.getSells();
                Log.d("Sell response", String.valueOf(selldata.get(0).getPaymenturl()));
                String invoiceUrl = selldata.get(0).getPaymenturl();
                if(!invoiceUrl.isEmpty()){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(invoiceUrl));
                    startActivity(browserIntent);
                }

            }

            @Override
            public void onFailure(Call<SellListResponse> call, Throwable t) {
                // Handle failure (e.g., no internet, server error, etc.)
//                Log.e(TAG, "Error fetching sell list: " + t.getMessage());

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
                    sellListViewModel.fetchSellList(locationId, "",start_date,end_date,pagenumber,is_direct_sale,search);
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
                    sellListViewModel.fetchSellList(locationId, "",start_date,end_date,pagenumber,is_direct_sale,search);
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }


    private void fetchinvoice(int invoice_id) {
        String accessToken = getAuthTokenFromPreferences(); // Use the actual access token


        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<SellListResponse> call = apiService.getSellById("Bearer " + accessToken, String.valueOf(invoice_id));

        call.enqueue(new Callback<SellListResponse>() {
            @Override
            public void onResponse(Call<SellListResponse> call, Response<SellListResponse> response) {
                SellListResponse sellresponse = response.body();
                Log.d("Sell response Full", String.valueOf(sellresponse));
                List<Sell> selldata = sellresponse.getSells();
                generateInvoiceView(selldata.get(0));
            }

            @Override
            public void onFailure(Call<SellListResponse> call, Throwable t) {
                // Handle failure (e.g., no internet, server error, etc.)
//                Log.e(TAG, "Error fetching sell list: " + t.getMessage());

            }
        });
    }


    private void fetchinvoiceEdit(int invoice_id) {
        Intent intent = new Intent(getApplicationContext(),PosEditActivity.class);
        intent.putExtra("invoice_id",String.valueOf(invoice_id));
        startActivity(intent);

//        String accessToken = getAuthTokenFromPreferences(); // Use the actual access token
//
//
//        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
//        Call<SellListResponse> call = apiService.getSellById("Bearer " + accessToken, String.valueOf(invoice_id));
//
//        call.enqueue(new Callback<SellListResponse>() {
//            @Override
//            public void onResponse(Call<SellListResponse> call, Response<SellListResponse> response) {
//                SellListResponse sellresponse = response.body();
//                Log.d("Sell response Full", String.valueOf(sellresponse));
//                List<Sell> selldata = sellresponse.getSells();
//                Log.d("Sell response", String.valueOf(selldata.get(0).getPaymenturl()));
//                String invoiceUrl = selldata.get(0).getPaymenturl();
//                if(!invoiceUrl.isEmpty()){
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(invoiceUrl));
//                    startActivity(browserIntent);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<SellListResponse> call, Throwable t) {
//                // Handle failure (e.g., no internet, server error, etc.)
////                Log.e(TAG, "Error fetching sell list: " + t.getMessage());
//
//            }
//        });

    }





    private void generateInvoiceView(Sell selldata) {
        Log.d("SELLSA DATA FINAL", String.valueOf(selldata.getInvoiceNo()));
        String invoiceUrl = selldata.getInvoiceurl();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(invoiceUrl));
        startActivity(browserIntent);


//        TextView tvInvoiceTitle = findViewById(R.id.tvInvoiceTitle);
//        TextView tvCustomerName = findViewById(R.id.tvCustomerName);
//        TextView tvTotalAmount = findViewById(R.id.tvTotalAmount);
//        RecyclerView itemRecyclerView = findViewById(R.id.itemRecyclerView);

//         Set the values to the views
//        tvInvoiceTitle.setText("Invoice: " + selldata.getInvoiceNo());
//        tvCustomerName.setText("Customer: " + selldata.getContact().getName());
//        tvTotalAmount.setText("Total Amount: " + selldata.getFinal_total());
//        tvInvoiceTitle.setText("Hello");

//        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
//        PrintDocumentAdapter printAdapter = new PrintDocumentAdapter() {
//            @Override
//            public void onStart() {
//                // This method will start the printing process
//            }
//
//            @Override
//            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle bundle) {
//                callback.onLayoutFinished(new PrintDocumentInfo.Builder("invoice.pdf")
//                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
//                        .setPageCount(1)
//                        .build(), true);
//            }
//
//            @Override
//            public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
//                // Generate and write the content of the invoice to PDF
//                // This part will depend on how you want to format the invoice
//            }
//        };

// Trigger the print
//        printManager.print("Invoice Print", printAdapter, null);


    }

    private void fetchLocations() {
        String accessToken = getAuthTokenFromPreferences(); // Use the actual access token
        Log.d("Access Token",accessToken);


        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<LocationResponse> call = apiService.getLocations("Bearer " + accessToken);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    // Extract location names from the response

                    locations.add("All Locations"); // Default value
                    ids.add("");

                    for (LocationResponse.Location location : response.body().getData()) {
                        locations.add(location.getName());
                        ids.add(location.getId());
                    }

                    // Set up the spinner with location names
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SellListActivity.this, android.R.layout.simple_spinner_item, locations);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLocation.setAdapter(adapter);
                } else {
                    Toast.makeText(SellListActivity.this, "Failed to fetch locations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Log.d("Make Error",t.getMessage());
                Toast.makeText(SellListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getAuthTokenFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("auth_token", null);
        return sharedPreferences.getString("auth_token", null);  // Retrieve the token from SharedPreferences
    }



    private void showDatePickerDialog(EditText editText) {
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
                    editText.setText(formattedDate);
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
}
