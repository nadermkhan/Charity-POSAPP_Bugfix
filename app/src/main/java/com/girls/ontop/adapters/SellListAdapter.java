package com.girls.ontop.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.models.PaymentLineModel;
import com.girls.ontop.models.Sell;

import java.util.Objects;

public class SellListAdapter extends ListAdapter<Sell, SellListAdapter.SellViewHolder> {

    private static OnItemClickListener listener;
    private static Context context;

    public SellListAdapter(Context context, OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener; // Initialize listener
    }

    @NonNull
    @Override
    public SellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sell, parent, false);
        return new SellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellViewHolder holder, int position) {
        Sell sell = getItem(position);
        holder.bind(sell);
    }

    static class SellViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderCode, tvDate, tvAmount, tvStatus,tvShippingStatus, tvCustomerName,tvDue,customerphone,tvLocation,tvPathao,tvNotes;
        ImageView ivEdit, ivShare, ivPay,ivChangeStatus,ivDelete,pathao;
        public SellViewHolder(View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvShippingStatus = itemView.findViewById(R.id.tvShippingStatus);
            tvCustomerName = itemView.findViewById(R.id.customername);
            customerphone = itemView.findViewById(R.id.customerphone);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivShare = itemView.findViewById(R.id.ivShare);
            ivPay = itemView.findViewById(R.id.ivPay);
            tvDue = itemView.findViewById(R.id.tvDue);
            ivChangeStatus = itemView.findViewById(R.id.ivChangeStatus);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPathao = itemView.findViewById(R.id.tvPathao);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            pathao = itemView.findViewById(R.id.pathao);
        }
        public void bind(Sell sell) {
            tvOrderCode.setText(String.format("Invoice: %s", sell.getInvoiceNo()));
            tvDate.setText(String.format("Date: %s", sell.getTransactionDate()));
            tvAmount.setText(String.format("Amount: %s", sell.getFinal_total()));
            tvPathao.setText(String.format("Pathao: %s", sell.getPathao_delivery_status()));
            tvNotes.setText(String.format("Notes: %s", sell.getAdditional_notes()));

            String status = sell.getPathao_delivery_status();





            String paymentStatus = sell.getPaymentStatus();

            if (paymentStatus == null || paymentStatus.equals("null")) {
                paymentStatus = "";
            }

            if (!paymentStatus.isEmpty()) {
                paymentStatus = paymentStatus.substring(0, 1).toUpperCase() + paymentStatus.substring(1).toLowerCase();
            }

            tvStatus.setText(String.format(paymentStatus));
            if ("paid".equalsIgnoreCase(paymentStatus)) {
                tvStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
                tvStatus.setTextColor(context.getResources().getColor(android.R.color.white));
            }else{
                tvStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
                tvStatus.setTextColor(context.getResources().getColor(android.R.color.white));
            }

            String shippingstatus = sell.getShipping_status();

            if (shippingstatus == null || shippingstatus.equals("null")) {
                shippingstatus = "";
            }

            if (!shippingstatus.isEmpty()) {
                shippingstatus = shippingstatus.substring(0, 1).toUpperCase() + shippingstatus.substring(1).toLowerCase();
            }

            tvShippingStatus.setText(String.format(shippingstatus));
            if ("delivered".equalsIgnoreCase(shippingstatus)) {
                tvShippingStatus.setBackgroundColor(context.getColor(android.R.color.holo_green_light));
                tvShippingStatus.setTextColor(context.getColor(android.R.color.white));
            }else{
                tvShippingStatus.setBackgroundColor(context.getColor(android.R.color.holo_orange_light));
                tvShippingStatus.setTextColor(context.getColor(android.R.color.white));
            }

            tvLocation.setText(String.format("Location: %s", sell.getLocation_name()));


            float totalPaid = 0;

            for (PaymentLineModel paymentLine : sell.getPayment_lines()) {
                totalPaid = totalPaid+paymentLine.getAmount();
            }
            tvDue.setText("Due: " + (Float.parseFloat(sell.getFinal_total()) - totalPaid));
            if (sell.getContact() != null && sell.getContact().getName() != null) {
                tvCustomerName.setText(String.format("Customer: %s", sell.getContact().getName()));
                customerphone.setText(String.format("Phone: %s", sell.getContact().getMobile()));
            } else {
                tvCustomerName.setText("Customer: Unknown");
            }

            // Set click listeners for Share and Pay buttons
            ivShare.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShareClick(sell);
                }
            });

            pathao.setOnClickListener(v -> {
                if(listener!=null){
                    listener.onPathaoClick(sell);
                }
            });

            ivPay.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPayClick(sell);
                }
            });

            tvStatus.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPayClick(sell);
                }
            });

            ivDelete.setOnClickListener(view -> {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to perform this action?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (listener != null) {
                                listener.onDeleteClick(sell);
                            }
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            });



            ivEdit.setOnClickListener(view -> {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to perform this action?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (listener != null) {
                                listener.onEditClick(sell);
                            }
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            });


            ivChangeStatus.setOnClickListener(v -> {
                showChangeStatusDialog(sell);
            });

            tvShippingStatus.setOnClickListener(v -> {
                showChangeStatusDialog(sell);
            });



        }
    }

    private static final DiffUtil.ItemCallback<Sell> DIFF_CALLBACK = new DiffUtil.ItemCallback<Sell>() {
        @Override
        public boolean areItemsTheSame(@NonNull Sell oldItem, @NonNull Sell newItem) {
            return oldItem.getInvoiceNo().equals(newItem.getInvoiceNo());
        }
        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Sell oldItem, @NonNull Sell newItem) {
            return oldItem.equals(newItem);
        }
    };

    private static void showChangeStatusDialog(Sell sell) {
        // Inflate the dialog view
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_shipping_status, null);

        // Find spinner and button in the dialog
        Spinner spinner = dialogView.findViewById(R.id.spinnerShippingStatus);
        Button btnApply = dialogView.findViewById(R.id.btnApply);

        // Add the EditText for the note
        EditText etStatusNote = dialogView.findViewById(R.id.etStatusNote);

        // Set up the spinner with the status options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.shipping_status_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set the current shipping status in the spinner
        String shippingStatus = sell.getShipping_status();
        if (shippingStatus != null && !shippingStatus.isEmpty()) {
            int position = adapter.getPosition(shippingStatus.toLowerCase());
            spinner.setSelection(position);
        }

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Set the apply button action
        btnApply.setOnClickListener(v -> {
            String selectedStatus = spinner.getSelectedItem().toString();
            String statusNote = etStatusNote.getText().toString();  // Get the note input
            listener.onChangeStatusClick(sell, selectedStatus, statusNote);  // Pass the note as well
            dialog.dismiss();
        });

        // Show the dialog
        dialog.show();
    }



    // Interface for handling button clicks in the activity or fragment
    public interface OnItemClickListener {
        void onShareClick(Sell sell);
        void onPayClick(Sell sell);

        void onEditClick(Sell sell);
        void onChangeStatusClick(Sell sell, String newStatus,String statusNote);

        void onDeleteClick(Sell sell);

        void onPathaoClick(Sell sell);
    }
}

