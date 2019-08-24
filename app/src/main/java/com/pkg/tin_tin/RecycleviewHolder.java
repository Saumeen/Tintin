package com.pkg.tin_tin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleviewHolder extends RecyclerView.ViewHolder {
    private View view;
    public RecycleviewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setName(String name){
        TextView namefield = view.findViewById(R.id.card_name);
        namefield.setText(name);
    }

    public void setemail(String email){
        TextView emailfield = view.findViewById(R.id.card_emaiid);
        emailfield.setText(email);
    }

    public void setAddress(String address){
        TextView addressfield = view.findViewById(R.id.card_address);
        addressfield.setText(address);
    }
}
