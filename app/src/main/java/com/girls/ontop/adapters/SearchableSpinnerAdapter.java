package com.girls.ontop.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.girls.ontop.models.CityResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchableSpinnerAdapter extends ArrayAdapter<CityResponse.City> {

    private Context context;
    private List<CityResponse.City> cities;

    public SearchableSpinnerAdapter(Context context, int resource, List<CityResponse.City> cities) {
        super(context, resource, cities);
        this.context = context;
        this.cities = cities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(cities.get(position).getCityName());
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setText(cities.get(position).getCityName());
        return textView;
    }
}
