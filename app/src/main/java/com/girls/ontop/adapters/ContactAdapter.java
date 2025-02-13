package com.girls.ontop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.girls.ontop.R;
import com.girls.ontop.models.CustomerResponse;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<CustomerResponse.Customer> contactList;
    private OnContactPayClickListener listener;

    public ContactAdapter(List<CustomerResponse.Customer> contactList, OnContactPayClickListener listener) {
        this.contactList = contactList;
        this.listener = listener;
    }

    public interface OnContactPayClickListener {
        void onContactPayClick(String contactId);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        CustomerResponse.Customer contact = contactList.get(position);
        holder.contactname.setText(contact.getName());
        holder.contactmobile.setText(contact.getMobile());
        holder.contactaddress.setText(contact.getAddress_line_1());
        holder.contact_id.setText(contact.getContact_id());

        holder.ivContactPay.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContactPayClick(String.valueOf(contact.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contact_id, contactname, contactmobile, contactaddress;
        ImageView ivContactPay;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contact_id = itemView.findViewById(R.id.contact_id);
            contactname = itemView.findViewById(R.id.contactname);
            contactmobile = itemView.findViewById(R.id.contactmobile);
            contactaddress = itemView.findViewById(R.id.contactaddress);
            ivContactPay = itemView.findViewById(R.id.ivContactPay);
        }
    }
}
