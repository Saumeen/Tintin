package com.pkg.tin_tin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecycleviewHolder> {

    HomeActivity mainActivity;
    ArrayList<UserData> userDataArrayList;

    public MyRecyclerViewAdapter(HomeActivity mainActivity, ArrayList<UserData> userDataArrayList) {
        this.mainActivity = mainActivity;
        this.userDataArrayList = userDataArrayList;
    }


    @NonNull
    @Override
    public RecycleviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(mainActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.card,parent,false);
        return new RecycleviewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecycleviewHolder holder, int position) {

        holder.setName(userDataArrayList.get(position).getName());
        holder.setAddress(userDataArrayList.get(position).getAddress());
        holder.setemail(userDataArrayList.get(position).getEmailid());

    }

    @Override
    public int getItemCount() {
        return userDataArrayList.size();
    }
}
