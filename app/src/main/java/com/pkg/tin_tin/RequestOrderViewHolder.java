package com.pkg.tin_tin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestOrderViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public RequestOrderViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }
    public void setCustname(String custname){
        TextView textView = view.findViewById(R.id.requestorder_custname);
        textView.setText(custname);
    }
    public void setCustphone(String custphone){
        TextView textView = view.findViewById(R.id.requestorder_custphoneno);
        textView.setText(custphone);
    }
    public void settype(String type){
        TextView textView = view.findViewById(R.id.requestorder_type);
        textView.setText(type);
    }
}
