package com.pkg.tin_tin;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TiffinRequestViewHolder extends RecyclerView.ViewHolder {

    private View view;
    public Button done;
    public TiffinRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        done = view.findViewById(R.id.menudatacard_done);

    }
    public void setType(String type){
        TextView textView = view.findViewById(R.id.menudatacard_type);
        textView.setText("Type :"+ type);
    }
    public void setMenu(String menu){
        TextView textView = view.findViewById(R.id.menudatacard_menu);
        textView.setText("Menu : "+menu);
    }
    public void setCost(String cost){
        TextView textView = view.findViewById(R.id.menudatacard_cost);
        textView.setText("Cost : "+cost);
    }
    public void setMobileNo(String mobile){
        TextView textView = view.findViewById(R.id.menudatacard_mobileno);
        textView.setText(mobile);
    }
    public void setCustomerName(String name){
        TextView textView = view.findViewById(R.id.menudatacard_name);
        textView.setText(name);
    }

}
