package com.girls.ontop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.models.ProductionData;

import java.text.MessageFormat;
import java.util.List;

public class ProductionAdapter extends RecyclerView.Adapter<ProductionAdapter.ProductionViewHolder> {

    private List<ProductionData> productionDataList;

    public ProductionAdapter(List<ProductionData> productionDataList) {
        this.productionDataList = productionDataList;
    }

    @NonNull
    @Override
    public ProductionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_production, parent, false);
        return new ProductionViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ProductionViewHolder holder, int position) {
        ProductionData productionData = productionDataList.get(position);

        holder.date.setText(MessageFormat.format("Date: {0}", productionData.getTransactionDate()));
        holder.contactname.setText(MessageFormat.format("Name: {0}", productionData.getCreatedBy()));
        holder.mfg_production_cost.setText(MessageFormat.format("Mfg Cost: {0}", productionData.getMfgProductionCost()));
        holder.ref_no.setText(MessageFormat.format("Ref: {0}", productionData.getRefNo()));
        holder.location_name.setText(MessageFormat.format("Location: {0}", productionData.getLocationName()));
        holder.product_name.setText(MessageFormat.format("Product: {0}", productionData.getProductName()));
        holder.quantity.setText(MessageFormat.format("Qty: {0}", productionData.getQuantity()));
        holder.final_total.setText(MessageFormat.format("Final: {0}", productionData.getFinalTotal()));
        holder.production_cost.setText(MessageFormat.format("Production Cost: {0}", productionData.getProductionCost()));
    }

    @Override
    public int getItemCount() {
        return productionDataList.size();
    }

    public static class ProductionViewHolder extends RecyclerView.ViewHolder {
        TextView date, contactname, mfg_production_cost, ref_no,location_name,product_name,quantity,final_total,production_cost;

        public ProductionViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            contactname = itemView.findViewById(R.id.contactname);
            mfg_production_cost = itemView.findViewById(R.id.mfg_production_cost);
            ref_no = itemView.findViewById(R.id.ref_no);
            location_name = itemView.findViewById(R.id.location_name);
            product_name = itemView.findViewById(R.id.product_name);
            quantity = itemView.findViewById(R.id.quantity);
            final_total = itemView.findViewById(R.id.final_total);
            production_cost = itemView.findViewById(R.id.production_cost);
        }
    }

}
