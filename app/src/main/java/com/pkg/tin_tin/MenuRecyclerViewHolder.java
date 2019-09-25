package com.pkg.tin_tin;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuRecyclerViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public MenuRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setMenu(String menu){
        TextView textView = view.findViewById(R.id.menu_card_menu);
        textView.setText(menu);
    }
    public void setCost(String cost){
        TextView textView = view.findViewById(R.id.menu_card_cost);
        textView.setText(cost);
    }
    public void setType(String type){
        TextView textView = view.findViewById(R.id.menu_card_type);
        textView.setText(type);
    }
    public void setQuantity(String quantity){
        TextView textView = view.findViewById(R.id.menu_card_quant);
        textView.setText(quantity);
    }
}
