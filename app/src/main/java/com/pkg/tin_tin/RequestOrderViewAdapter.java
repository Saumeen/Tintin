package com.pkg.tin_tin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestOrderViewAdapter extends RecyclerView.Adapter<RequestOrderViewHolder> {
    private ArrayList<RequestOrderModel> orderModelArrayList;

    public RequestOrderViewAdapter(ArrayList<RequestOrderModel> orderModelArrayList) {
        this.orderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public RequestOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.home_menu_card,parent,false);
        return new RequestOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestOrderViewHolder holder, int position) {
        holder.setCustname(orderModelArrayList.get(position).getCustname());
        holder.setCustphone(orderModelArrayList.get(position).getCustphone());
        holder.settype(orderModelArrayList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }
}
