package com.girls.ontop.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.models.Expense;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    private List<Expense> expenseList;
    private OnDeleteClickListener onDeleteClickListener;

    // Constructor with a listener for delete click
    public ExpenseAdapter(Context context, List<Expense> expenseList, OnDeleteClickListener onDeleteClickListener) {
        this.context = context;
        this.expenseList = expenseList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.refNo.setText("Ref No: " + expense.getRefNo());
        holder.transactionDate.setText("Date: " + expense.getTransactionDate());
        holder.totalAmount.setText("Total: " + expense.getFinalTotal());
        holder.paymentStatus.setText(expense.getPaymentStatus());

        if ("paid".equalsIgnoreCase(expense.getPaymentStatus())) {
            holder.paymentStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
            holder.paymentStatus.setTextColor(context.getResources().getColor(android.R.color.white));
        } else {
            holder.paymentStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light));
            holder.paymentStatus.setTextColor(context.getResources().getColor(android.R.color.white));
        }

        // Set click listener for delete button
        holder.ivDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(expense); // Pass the expense object to the activity
            }
        });

        holder.ivEdit.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onEditClick(expense); // Pass the expense object to the activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // ViewHolder class to hold references to the UI components
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView refNo, transactionDate, totalAmount, paymentStatus;
        ImageView ivEdit, ivDelete;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            refNo = itemView.findViewById(R.id.refNo);
            transactionDate = itemView.findViewById(R.id.transactionDate);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            paymentStatus = itemView.findViewById(R.id.paymentStatus);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

    // Interface to handle delete click
    public interface OnDeleteClickListener {
        void onDeleteClick(Expense expense); // Pass the expense object for further calculations

        void onEditClick(Expense expense);
    }
}
