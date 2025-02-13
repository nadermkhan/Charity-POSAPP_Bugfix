package com.girls.ontop.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.activities.PosActivity;
import com.girls.ontop.activities.PosEditActivity;
import com.girls.ontop.models.Product;
import com.girls.ontop.models.Variation;
import com.girls.ontop.utils.AppConstants;

import java.util.ArrayList;

public class CartEditAdapter extends RecyclerView.Adapter<CartEditAdapter.CartViewHolder> {
    private final Context context;
    private final ArrayList<Product> cartItems;

    public CartEditAdapter(Context context, ArrayList<Product> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductName, tvItemQuantity, tvItemSize, tvItemUnitPrice, tvItemSubTotal;
        public ImageButton incrementOne, decrementOne;
        public TextView  tvDiscountType, tvDiscountAmount;
        public Button tvDiscountBtn;
        public ImageView ivProductImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_base_item_edit_name);
            tvItemQuantity = itemView.findViewById(R.id.tv_base_item_quantity);
            tvItemSize = itemView.findViewById(R.id.tv_base_item_size);
            tvItemUnitPrice = itemView.findViewById(R.id.tv_base_item_price);
            tvItemSubTotal = itemView.findViewById(R.id.tv_base_item_unit_price);
            incrementOne = itemView.findViewById(R.id.btn_add_edit_count_cart);
            decrementOne = itemView.findViewById(R.id.btn_substract_edit_count_cart);
            tvDiscountBtn = itemView.findViewById(R.id.tvDiscount);
            tvDiscountType = itemView.findViewById(R.id.tv_base_item_discount_type);
            tvDiscountAmount = itemView.findViewById(R.id.tv_base_item_discount_amount);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_edit_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product productData = cartItems.get(position);
        Log.d("Inside Adapter", productData.getName());

        // Discount Details
        //int discount = productData != null ? productData.getDiscount() : 0;
        holder.tvDiscountAmount.setText(String.valueOf(productData.getDiscount()));

        // int discountType = productData != null ? productData.getDiscountType() : 0;
        int discountType = 0;
        if (discountType == AppConstants.DiscountType.NOTING.ordinal()) {
            holder.tvDiscountType.setText("");
        } else if (discountType == AppConstants.DiscountType.FIXED.ordinal()) {
            holder.tvDiscountType.setText("F");
        } else if (discountType == AppConstants.DiscountType.PERCENTAGE.ordinal()) {
            holder.tvDiscountType.setText("%");
        }

        Variation selectedVariation = productData.getSelectedVariation();
        if (selectedVariation != null) {
            holder.tvProductName.setText(productData.getName());
            holder.tvItemQuantity.setText(String.valueOf(productData.getQuantity()));
            holder.tvItemSize.setText(selectedVariation.getName());
            holder.tvItemUnitPrice.setText(String.valueOf((selectedVariation.getDefault_sell_price()-productData.getDiscount()) * productData.getQuantity()));
            holder.tvItemSubTotal.setText(String.valueOf(selectedVariation.getDefault_sell_price()));
        } else {
            holder.tvItemSize.setText("");
            holder.tvItemUnitPrice.setText("0");
            holder.tvItemSubTotal.setText("0");
        }

        // Product Details
//        if (productData != null) {
//            holder.tvProductName.setText(productData.getName());
//            holder.tvItemQuantity.setText(String.valueOf(productData.getQuantity()));
//
//            if (productData.getProduct_variations() != null && !productData.getProduct_variations().isEmpty()) {
//                holder.tvItemSize.setText(productData.getProduct_variations().get(0).getVariations().get(0).getName());
//           } else {
//                holder.tvItemSize.setText("");
//            }
//
//            holder.tvItemUnitPrice.setText(String.valueOf(productData.getProduct_variations().get(0).getVariations().get(0).getDefault_sell_price()* productData.getQuantity()));
//       //     holder.tvItemSubTotal.setText(String.valueOf(productData.getSubTotal()));
//            holder.tvItemSubTotal.setText(String.valueOf(productData.getProduct_variations().get(0).getVariations().get(0).getDefault_sell_price()));
//
//            // Load product image using Glide (optional)
//
//        }

        // Button Click Listeners
        holder.incrementOne.setOnClickListener(v -> {
            if (context instanceof PosEditActivity) {
                ((PosEditActivity) context).onIncrement(position);
            }
        });

        holder.decrementOne.setOnClickListener(v -> {
            if (context instanceof PosEditActivity) {
                ((PosEditActivity) context).onDecrement(position);
            }
        });

        holder.tvDiscountBtn.setOnClickListener(v -> {
            if (context instanceof PosEditActivity) {
                showDiscountModal(holder,position);
            }
        });

    }

    private void showDiscountModal(CartViewHolder holder, int position) {
        // Inflate the modal layout
        View modalView = LayoutInflater.from(context).inflate(R.layout.discount_modal, null);

        // Initialize views
        RadioGroup discountTypeGroup = modalView.findViewById(R.id.discount_type_group);
        EditText discountValue = modalView.findViewById(R.id.discount_value);
        Button applyButton = modalView.findViewById(R.id.btn_apply_discount);

        // Show an AlertDialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(modalView);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        // Handle apply button click
        applyButton.setOnClickListener(v -> {
            int selectedTypeId = discountTypeGroup.getCheckedRadioButtonId();
            boolean isFixed = selectedTypeId == R.id.discount_fixed;

            String discountValueText = discountValue.getText().toString();
            if (discountValueText.isEmpty()) {
                discountValue.setError("Enter a value");
                return;
            }

            double discount = Double.parseDouble(discountValueText);

            // Update the discount type and value in the model
            Product product = cartItems.get(position);
            product.setDiscount((float) discount);
            product.setDiscountType(isFixed ? AppConstants.DiscountType.FIXED.ordinal() : AppConstants.DiscountType.PERCENTAGE.ordinal());

            // Recalculate and update the views
            double discountedPrice = calculateDiscountedPrice(product, isFixed, discount);
            holder.tvDiscountAmount.setText(String.valueOf(discount));
            holder.tvDiscountType.setText(isFixed ? "F" : "%");
            holder.tvItemUnitPrice.setText(String.valueOf(discountedPrice));

            dialog.dismiss();
            ((PosEditActivity) context).updateCalculation();
        });
    }

    private double calculateDiscountedPrice(Product product, boolean isFixed, double discount) {
        Variation variation = product.getSelectedVariation();
        if (variation == null) return 0;

        double unitPrice = variation.getDefault_sell_price();
        double quantity = product.getQuantity();
        double totalPrice = unitPrice * quantity;

        if (isFixed) {
            // Subtract fixed discount
            totalPrice -= discount;
        } else {
            // Apply percentage discount
            totalPrice -= (totalPrice * discount / 100);
        }

        return totalPrice;
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
