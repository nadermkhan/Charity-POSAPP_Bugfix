package com.girls.ontop.models;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// ViewModelFactory that allows passing a Context to the ViewModel
public class SellListViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    // Constructor to pass Context
    public SellListViewModelFactory(Context context) {
        this.context = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SellListViewModel.class)) {
            return (T) new SellListViewModel(context);  // Pass context to ViewModel
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
