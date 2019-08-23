package com.pkg.tin_tin;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class HomeMenuRecyclerViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public Button remove,edit;
    public HomeMenuRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setMenu(String menu){
        TextView textView = view.findViewById(R.id.menu_card_menu);
        textView.setText("Menu : "+menu);
    }
    public void setCost(String cost){
        TextView textView = view.findViewById(R.id.menu_card_cost);
        textView.setText("Cost : "+cost);
    }
    public void setType(String type){
        TextView textView = view.findViewById(R.id.menu_card_type);
        textView.setText("Type : "+type);
    }
    public void setQuantity(String quantity){
        TextView textView = view.findViewById(R.id.menu_card_quant);
        textView.setText("Quantity : "+quantity);
    }

}
