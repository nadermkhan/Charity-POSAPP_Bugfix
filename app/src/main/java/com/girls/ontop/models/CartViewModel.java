package com.girls.ontop.models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.girls.ontop.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<Product>> _productList = new MutableLiveData<>();
    public LiveData<List<Product>> getProductList() {
        return _productList;
    }
    private ArrayList<Product> productAry = new ArrayList<>();

    private MutableLiveData<List<BusinessLocation>> _locationList = new MutableLiveData<>();
    public LiveData<List<BusinessLocation>> getLocationList() {
        return _locationList;
    }
    private ArrayList<BusinessLocation> locationAry = new ArrayList<>();

    private BaseApiRepo baseApiRepo;

    public CartViewModel(BaseApiRepo baseApiRepo) {
        this.baseApiRepo = baseApiRepo;
    }

    public void getAllBusinessLocations(String token) {
        Call<BusinessLocationWithStatus> locationStatusCall = baseApiRepo.getAllBusinessLocationsWithStatus(token);
        if (locationStatusCall != null) {
            locationAry.clear();
            locationStatusCall.enqueue(new Callback<BusinessLocationWithStatus>() {
                @Override
                public void onResponse(Call<BusinessLocationWithStatus> call, Response<BusinessLocationWithStatus> response) {
                    Log.d(AppConstants.TAG, "onResponse: listOfLocations " + response.body());
                    if (response.code() == AppConstants.result_ok && response.body() != null) {
                        BusinessLocationWithStatus body = response.body();
                        if (AppConstants.success.equals(body.getStatus()) && body.getData() != null && !body.getData().isEmpty()) {
                            locationAry.clear();
                            locationAry.addAll(body.getData());
                            _locationList.postValue(locationAry);
                        } else {
                            _locationList.postValue(null);
                        }
                    } else {
                        _locationList.postValue(null);
                    }
                }

                @Override
                public void onFailure(Call<BusinessLocationWithStatus> call, Throwable t) {
                    Log.d(AppConstants.TAG, "onFailure: listOfLocations " + t.getMessage());
                    _locationList.postValue(null);
                }
            });
        } else {
            _locationList.postValue(null);
        }
    }

    public void getAllProducts(String token, int businessLocationId) {
        Log.d(AppConstants.TAG, "getAllProducts: Fetching products " + businessLocationId);
        Call<ProductStatus> productStatusCall = baseApiRepo.getAllProducts(token, businessLocationId);
        if (productStatusCall != null) {
            productAry.clear();
            productStatusCall.enqueue(new Callback<ProductStatus>() {
                @Override
                public void onResponse(Call<ProductStatus> call, Response<ProductStatus> response) {
                    Log.d(AppConstants.TAG, "onResponse: listOfProducts " + response.body());
                    if (response.code() == AppConstants.result_ok && response.body() != null) {
                        ProductStatus body = response.body();
                        if (AppConstants.success.equals(body.getStatus()) && body.getData() != null && !body.getData().isEmpty()) {
                            productAry.clear();
                            for (Product product : body.getData()) {
//                                if (product.getDiscountitem() != null) {
//                                    if (product.getDiscountitem().getDiscount() != null) {
//                                        float discountAmount = product.getDiscountitem().getDiscountAmount();
//                                        String discountType = product.getDiscountitem().getDiscountType();
//                                        Log.d("Test_discount", "onResponse: N " + product.getName() +
//                                                " T " + discountType + " Amount " + discountAmount);
//                                    }
//                                }
                                productAry.add(product);
                            }
                            Log.d(AppConstants.TAG, "getAllProducts: Fetching products count -> " + productAry.size());
                            _productList.postValue(productAry);
                        } else {
                            _productList.postValue(null);
                        }
                    } else {
                        _productList.postValue(null);
                    }
                }

                @Override
                public void onFailure(Call<ProductStatus> call, Throwable t) {
                    Log.d(AppConstants.TAG, "onFailure: listOfProducts " + t.getMessage());
                    _productList.postValue(null);
                }
            });
        } else {
            _productList.postValue(null);
        }
    }

    public void clearProductList() {
        _productList.postValue(new ArrayList<>(0));
    }
}
