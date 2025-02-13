package com.girls.ontop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.activities.PosActivity;
import com.girls.ontop.models.Product;
import com.girls.ontop.models.ProductModelResponse;
import com.girls.ontop.utils.AppConstants;

import java.util.ArrayList;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.CartViewHolder> {
    private final Context context;
    private final ArrayList<ProductModelResponse.ProductModel> cartItems;

    public SuggestionAdapter(Context context, ArrayList<ProductModelResponse.ProductModel> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductName, tvItemQuantity, tvItemSize, tvItemUnitPrice, tvItemSubTotal;
        public ImageButton incrementOne, decrementOne;
        public TextView tvDiscountBtn, tvDiscountType, tvDiscountAmount;
        public ImageView ivProductImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_base_item_name);
            tvItemQuantity = itemView.findViewById(R.id.tv_base_item_quantity);
            tvItemSize = itemView.findViewById(R.id.tv_base_item_size);
            tvItemUnitPrice = itemView.findViewById(R.id.tv_base_item_price);
            tvItemSubTotal = itemView.findViewById(R.id.tv_base_item_unit_price);
            incrementOne = itemView.findViewById(R.id.btn_add_count_cart);
            decrementOne = itemView.findViewById(R.id.btn_substract_count_cart);
//            tvDiscountBtn = itemView.findViewById(R.id.tvDiscount);
            tvDiscountType = itemView.findViewById(R.id.tv_base_item_discount_type);
            tvDiscountAmount = itemView.findViewById(R.id.tv_base_item_discount_amount);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductModelResponse.ProductModel productData = cartItems.get(position);

        // Discount Details
        int discount =  0;
        holder.tvDiscountAmount.setText(String.valueOf(discount));

        int discountType = 0;
        if (discountType == AppConstants.DiscountType.NOTING.ordinal()) {
            holder.tvDiscountType.setText("");
        } else if (discountType == AppConstants.DiscountType.FIXED.ordinal()) {
            holder.tvDiscountType.setText("F");
        } else if (discountType == AppConstants.DiscountType.PERCENTAGE.ordinal()) {
            holder.tvDiscountType.setText("%");
        }

        // Product Details
        if (productData != null) {
            holder.tvProductName.setText(productData.getName());

            // If size info exists, display it; otherwise, leave blank
//            if (productData.getProductVariations() != null && !productData.getProductVariations().isEmpty()) {
//                holder.tvItemSize.setText(productData.getProductVariations().get(0).getName());
//            } else {
            holder.tvItemSize.setText("");
//            }



            // Load product image using Glide (optional)

        }

        // Button Click Listeners
        holder.incrementOne.setOnClickListener(v -> {
            if (context instanceof PosActivity) {
                ((PosActivity) context).onIncrement(position);
            }
        });

        holder.decrementOne.setOnClickListener(v -> {
            if (context instanceof PosActivity) {
                ((PosActivity) context).onDecrement(position);
            }
        });

        holder.tvDiscountBtn.setOnClickListener(v -> {
            if (context instanceof PosActivity) {
                // Uncomment when discount dialog functionality is needed
                // ((PosActivity) context).showCustomDialog(context, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
